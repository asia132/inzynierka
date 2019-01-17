#include "RobustLinearRegression.h"
#include <gsl/gsl_multifit.h>

const gsl_multifit_robust_type * chooseRobust(int i){
	
	const gsl_multifit_robust_type * robus_array[7] = {
		gsl_multifit_robust_bisquare,
		gsl_multifit_robust_cauchy,
		gsl_multifit_robust_fair,
		gsl_multifit_robust_huber,
		gsl_multifit_robust_ols,
		gsl_multifit_robust_welsch
	};

	return i < 6 ? robus_array[i] : gsl_multifit_robust_default;
}
//	Getting an object double state value from provided class
	void get_array_component_values_d(JNIEnv * env, jobject object, int n, double * a, const char * txt){

		jclass method_class = (*env)->GetObjectClass(env, object);

		jfieldID id_a = (*env)->GetFieldID(env, method_class, txt, "[D");
		jobject yobj = (*env)->GetObjectField(env, object, id_a);
		(*env)->GetDoubleArrayRegion(env, yobj, 0, n, a);

	}
// run Regression
	JNIEXPORT jdoubleArray JNICALL Java_least_1squares_1fitting_RobustLinearRegression_gslRobustLinear(JNIEnv * env, jobject object, jdoubleArray java_x, jint m, jint robType){
	// przygotowanie macierzy X i wektora y
		double * x_temp = (*env) -> GetDoubleArrayElements(env, java_x, NULL);
		size_t s = (*env)->GetArrayLength(env, java_x);
		size_t n = (int)(s/m);
		double y_temp[n];
		get_array_component_values_d(env, object, n, y_temp, "y");
		int i, j, k;
		gsl_matrix *X;
		gsl_vector *y;
		X = gsl_matrix_alloc (n, m);
		y = gsl_vector_alloc (n);
		for (i = 0, k = 0; i < n; i++)
			for (j = 0; j < m; j++, k++)
				gsl_matrix_set (X, i, j, x_temp[k]);
		for (i = 0; i < n; i ++)
			gsl_vector_set (y, i, y_temp[i]);
	// zmienne, do których zostanie zapisany wynik
		gsl_matrix *cov;
		gsl_vector *c;
		c = gsl_vector_alloc (m);
		cov = gsl_matrix_alloc (m, m);
	// wyłączenie domyślnego systemu zgłaszania błędów biblioteki GSL
		gsl_set_error_handler_off();
	// przeprowadzenie regresji
		gsl_multifit_robust_workspace * work = gsl_multifit_robust_alloc (chooseRobust(robType), X->size1, X->size2);
		int status = gsl_multifit_robust (X, y, c, cov, work);
	// sprawdzenie, czy wystąpił błąd
		if (status != GSL_SUCCESS)
			(*env)->ThrowNew(env, (*env)->FindClass(env, "Integration/GSLErrors"), gsl_strerror (status));
	// zapisanie współczynników c
		double c_temp[m];
		for (i = 0; i < m; i++)
			c_temp[i] = gsl_vector_get (c, i);
		jclass method_class = (*env)->GetObjectClass(env, object);
		jfieldID cId = (*env)->GetFieldID(env, method_class, "c", "[D");
		jdoubleArray C = (*env)->NewDoubleArray(env, m);
		(*env)->SetDoubleArrayRegion(env, C, 0, m, c_temp);
		(*env)->SetObjectField(env, object, cId, C);
	// Przygotowanie tablicy, która zostanie zwrócona macierz kowariancji
		double temp[s];
		for (i = 0, k = 0; i < m; i++) {
			for (j = 0; j < m; j++, k++) {
				temp[k] = gsl_matrix_get (cov, i, j);
			}
		}
		jdoubleArray cov_array = (*env)->NewDoubleArray(env, s);
		(*env)->SetDoubleArrayRegion(env, cov_array, 0, s, temp);
	// Zwolnienie pamięci
		(*env)->ReleaseDoubleArrayElements(env, java_x, x_temp, JNI_ABORT);
		gsl_multifit_robust_free (work);
	// Zwrócenie macierzy kowariancji
		return cov_array;
	}
// run est
	JNIEXPORT jdouble JNICALL Java_least_1squares_1fitting_RobustLinearRegression_gslRobustLinearEst(JNIEnv * env, jobject object, jdoubleArray java_x, jdoubleArray java_cov){
	// przygotowanie wektora x, c oraz macierzy kowariancji
		double * cov_temp = (*env) -> GetDoubleArrayElements(env, java_cov, NULL);
		size_t c_s = (*env)->GetArrayLength(env, java_cov);
		double * x_temp = (*env) -> GetDoubleArrayElements(env, java_x, NULL);
		size_t n = (*env)->GetArrayLength(env, java_x);
		double c_temp[n];
		get_array_component_values_d(env, object, n, c_temp, "c");
		gsl_matrix * cov;
		gsl_vector * x, *c;
		cov = gsl_matrix_alloc (sqrt(c_s), sqrt(c_s));
		x = gsl_vector_alloc (n);
		c = gsl_vector_alloc (n);
		int i, j, k;
		for (i = 0, k = 0; i < sqrt(c_s); i++)
			for (j = 0; j < sqrt(c_s); j++, k++)
				gsl_matrix_set (cov, i, j, cov_temp[k]);
		for (i = 0; i < n; i ++) {
			gsl_vector_set (x, i, x_temp[i]);
			gsl_vector_set (c, i, c_temp[i]);
		}
	// zmienne, do których zostanie zapisany wynik	
		double y, y_err;
	// wyłączenie domyślnego systemu zgłaszania błędów biblioteki GSL
		gsl_set_error_handler_off();
	// Przeprowadzenie obliczeń
		int status = gsl_multifit_robust_est (x, c, cov, &y, &y_err);
	// sprawdzenie, czy wystąpił błąd
		if (status != GSL_SUCCESS)
			(*env)->ThrowNew(env, (*env)->FindClass(env, "Integration/GSLErrors"), gsl_strerror (status));
	// zapisanie wartości y_err
		jclass method_class = (*env)->GetObjectClass(env, object);
		jfieldID id_y_err = (*env)->GetFieldID(env, method_class, "y_err", "D");
		(*env)->SetDoubleField(env, object, id_y_err, y_err);
	// Zwolnienie pamięci
		(*env)->ReleaseDoubleArrayElements(env, java_x, x_temp, JNI_ABORT);
	// Zwrócenie wyniku
	 	return y;
	}