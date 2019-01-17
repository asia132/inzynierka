#include  "RegularizedLinearRegression.h"
#include <gsl/gsl_multifit.h>
#include <gsl/gsl_math.h>
#define nL 200 

//	Getting an object double array state value from provided class
	void get_array_parameters_values_d(JNIEnv * env, jobject object, int n, double * a, const char * txt){

		jclass method_class = (*env)->GetObjectClass(env, object);

		jfieldID id_a = (*env)->GetFieldID(env, method_class, txt, "[D");
		jobject yobj = (*env)->GetObjectField(env, object, id_a);
		(*env)->GetDoubleArrayRegion(env, yobj, 0, n, a);

	}
// run solution for provided lambda
JNIEXPORT void JNICALL Java_least_1squares_1fitting_RegularizedLinearRegression_gslRegularizedLinear___3DID
  (JNIEnv * env, jobject object, jdoubleArray java_x, jint m, jdouble lambda){
	// przygotowanie macierzy X i wektora y
		double * x_temp = (*env) -> GetDoubleArrayElements(env, java_x, NULL);
		size_t s = (*env)->GetArrayLength(env, java_x);
		size_t n = (int)(s/m);
		double y_temp[n];
		get_array_parameters_values_d(env, object, n, y_temp, "y");
		int i, j, k;
		gsl_matrix *X = gsl_matrix_alloc(n, m);
		gsl_vector *y = gsl_vector_alloc(n);
		for (i = 0, k = 0; i < n; ++i)
			for (j = 0; j < m; ++j, k++)	{
				gsl_matrix_set (X, i, j, x_temp[k]);
			gsl_vector_set (y, i, y_temp[i]);
		}
	// zmienne, do których zostanie zapisany wynik
		double rnorm, snorm;
		gsl_vector * c = gsl_vector_alloc(m);;
	// przeprowadzenie regresji
		gsl_multifit_linear_workspace *w = gsl_multifit_linear_alloc(n, m);
		gsl_multifit_linear_svd(X, w);
		gsl_multifit_linear_solve(lambda, X, y, c, &rnorm, &snorm, w);
		gsl_multifit_linear_free(w);
	// zapisanie współczynników c
		double c_temp[m];
		for (i = 0; i < m; i++) {
			c_temp[i] = gsl_vector_get (c, i);
		}
		jclass method_class = (*env)->GetObjectClass(env, object);
		jfieldID cId = (*env)->GetFieldID(env, method_class, "c", "[D");
		jdoubleArray C = (*env)->NewDoubleArray(env, m);
		(*env)->SetDoubleArrayRegion(env, C, 0, m, c_temp);
		(*env)->SetObjectField(env, object, cId, C);
	// zapisanie rnorm
		jfieldID id_rnorm = (*env)->GetFieldID(env, method_class, "rnorm", "D");
		(*env)->SetDoubleField(env, object, id_rnorm, rnorm);
	// zapisanie snorm
		jfieldID id_snorm = (*env)->GetFieldID(env, method_class, "snorm", "D");
		(*env)->SetDoubleField(env, object, id_snorm, snorm);
	// zwolnienie pamięci
		gsl_matrix_free(X);
		gsl_vector_free(y);
		gsl_vector_free(c);
		(*env)->ReleaseDoubleArrayElements(env, java_x, x_temp, JNI_ABORT);
}

// calc lambda and run solution
JNIEXPORT void JNICALL Java_least_1squares_1fitting_RegularizedLinearRegression_gslRegularizedLinear___3DI
  (JNIEnv * env, jobject object, jdoubleArray java_x, jint m){
  	// przygotowanie macierzy X i wektora y
		double * x_temp = (*env) -> GetDoubleArrayElements(env, java_x, NULL);
		size_t s = (*env)->GetArrayLength(env, java_x);
		size_t n = (int)(s/m);
		double y_temp[n];
		get_array_parameters_values_d(env, object, n, y_temp, "y");
		int i, j, k;
		gsl_matrix *X = gsl_matrix_alloc(n, m);
		gsl_vector *y = gsl_vector_alloc(n);
		for (i = 0, k = 0; i < n; ++i)	{
			for (j = 0; j < m; ++j, k++)
				gsl_matrix_set (X, i, j, x_temp[k]);
			gsl_vector_set (y, i, y_temp[i]);
		}
	// zmienne, do których zostanie zapisany wynik
		double rnorm, snorm;
		gsl_vector * c = gsl_vector_alloc(m);;
	// obliczenie parametru lambda
		gsl_multifit_linear_workspace *w = gsl_multifit_linear_alloc(n, m);
		gsl_multifit_linear_svd(X, w);
		gsl_vector *reg_param = gsl_vector_alloc(nL);
		gsl_vector *rho = gsl_vector_alloc(nL);  /* residual norms */
		gsl_vector *eta = gsl_vector_alloc(nL);  /* solution norms */
		double lambda;                           /* optimal regularization parameter */
		size_t reg_idx; 
		gsl_multifit_linear_lcurve(y, reg_param, rho, eta, w);
		gsl_multifit_linear_lcorner(rho, eta, &reg_idx);
		lambda = gsl_vector_get(reg_param, reg_idx);
	// przeprowadzenie regresji
		gsl_multifit_linear_solve(lambda, X, y, c, &rnorm, &snorm, w);
		gsl_multifit_linear_free(w);
	// zapisanie współczynników c
		double c_temp[m];
		for (i = 0; i < m; i++) {
			c_temp[i] = gsl_vector_get (c, i);
		}
		jclass method_class = (*env)->GetObjectClass(env, object);
		jfieldID cId = (*env)->GetFieldID(env, method_class, "c", "[D");
		jdoubleArray C = (*env)->NewDoubleArray(env, m);
		(*env)->SetDoubleArrayRegion(env, C, 0, m, c_temp);
		(*env)->SetObjectField(env, object, cId, C);
	// zapisanie rnorm
		jfieldID id_rnorm = (*env)->GetFieldID(env, method_class, "rnorm", "D");
		(*env)->SetDoubleField(env, object, id_rnorm, rnorm);
	// zapisanie snorm
		jfieldID id_snorm = (*env)->GetFieldID(env, method_class, "snorm", "D");
		(*env)->SetDoubleField(env, object, id_snorm, snorm);
	// zapisanie lambda
		jfieldID id_lambda = (*env)->GetFieldID(env, method_class, "lambda", "D");
		(*env)->SetDoubleField(env, object, id_lambda, lambda);
	// zwolnienie pamięci
		gsl_matrix_free(X);
		gsl_vector_free(y);
		gsl_vector_free(c);
		gsl_vector_free(reg_param);
		gsl_vector_free(rho);
		gsl_vector_free(eta);
		(*env)->ReleaseDoubleArrayElements(env, java_x, x_temp, JNI_ABORT);
  }