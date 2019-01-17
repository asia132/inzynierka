#include "MultiParameterRegression.h"
#include <gsl/gsl_fit.h>
#include <gsl/gsl_multifit.h>
#include <math.h>
#include <gsl/gsl_errno.h>

//	Getting an object double state value from provided class
	void get_array_parameters_values_d(JNIEnv * env, jobject object, int n, double * a, const char * txt){

		jclass method_class = (*env)->GetObjectClass(env, object);

		jfieldID id_a = (*env)->GetFieldID(env, method_class, txt, "[D");
		jobject yobj = (*env)->GetObjectField(env, object, id_a);
		(*env)->GetDoubleArrayRegion(env, yobj, 0, n, a);

	}
//	gslMultiFitLinearW
	JNIEXPORT jdoubleArray JNICALL Java_least_1squares_1fitting_MultiParameterRegression_gslMultiFitLinearW(JNIEnv * env, jobject object, jdoubleArray java_x, jint m){
	// setting values
		double * x_temp = (*env) -> GetDoubleArrayElements(env, java_x, NULL);
		size_t s = (*env)->GetArrayLength(env, java_x);
		size_t n = (int)(s/m);

		double y_temp[n], w_temp[n];
		get_array_parameters_values_d(env, object, n, y_temp, "y");
		get_array_parameters_values_d(env, object, n, w_temp, "w");

		int i, j, k;
		double chisq;

		gsl_matrix *X, *cov;
		gsl_vector *y, *w, *c;

		X = gsl_matrix_alloc (n, m);
		y = gsl_vector_alloc (n);
		w = gsl_vector_alloc (n);

		c = gsl_vector_alloc (m);
		cov = gsl_matrix_alloc (m, m);

		for (i = 0, k = 0; i < n; i++) {
			for (j = 0; j < m; j++, k++) {
				gsl_matrix_set (X, i, j, x_temp[k]);
			}
		}

		for (i = 0; i < n; i ++) {
			gsl_vector_set (y, i, y_temp[i]);
			gsl_vector_set (w, i, w_temp[i]);
		}
	// calculating
		{
			gsl_multifit_linear_workspace * work = gsl_multifit_linear_alloc (n, m);
			gsl_multifit_wlinear (X, w, y, c, cov, &chisq, work);
			gsl_multifit_linear_free (work);
		}
	// saving results
		jclass method_class = (*env)->GetObjectClass(env, object);
		// save chisq
		jfieldID chisqId = (*env)->GetFieldID(env, method_class, "chisq", "D");
		(*env)->SetDoubleField(env, object, chisqId, chisq);
		// save c
		double c_temp[m];

		for (i = 0; i < m; i++) {
			c_temp[i] = gsl_vector_get (c, i);
		}

		jfieldID cId = (*env)->GetFieldID(env, method_class, "c", "[D");
		jdoubleArray C = (*env)->NewDoubleArray(env, m);
		(*env)->SetDoubleArrayRegion(env, C, 0, m, c_temp);
		(*env)->SetObjectField(env, object, cId, C);
	// preparing returned results
		double temp[s];
		for (i = 0, k = 0; i < m; i++) {
			for (j = 0; j < m; j++, k++) {
				temp[k] = gsl_matrix_get (cov, i, j);
			}
		}

		jdoubleArray cov_array = (*env)->NewDoubleArray(env, s);
		(*env)->SetDoubleArrayRegion(env, cov_array, 0, s, temp);

		return cov_array;
	}
//	gslMultiFitLinear
	JNIEXPORT jdoubleArray JNICALL Java_least_1squares_1fitting_MultiParameterRegression_gslMultiFitLinear(JNIEnv * env, jobject object, jdoubleArray java_x, jint m){
	// setting values
		double * x_temp = (*env) -> GetDoubleArrayElements(env, java_x, NULL);
		size_t s = (*env)->GetArrayLength(env, java_x);
		size_t n = (int)(s/m);

		double y_temp[n];
		get_array_parameters_values_d(env, object, n, y_temp, "y");

		int i, j, k;
		double chisq;

		gsl_matrix *X, *cov;
		gsl_vector *y, *c;

		X = gsl_matrix_alloc (n, m);
		y = gsl_vector_alloc (n);

		c = gsl_vector_alloc (m);
		cov = gsl_matrix_alloc (m, m);

		for (i = 0, k = 0; i < n; i++) {
			for (j = 0; j < m; j++, k++) {
				gsl_matrix_set (X, i, j, x_temp[k]);
			}
			gsl_vector_set (y, i, y_temp[i]);
		}
	// calculating
		{
			gsl_multifit_linear_workspace * work = gsl_multifit_linear_alloc (n, m);
			gsl_multifit_linear (X, y, c, cov, &chisq, work);
			gsl_multifit_linear_free (work);
		}
	// saving results
		jclass method_class = (*env)->GetObjectClass(env, object);
		// save chisq
		jfieldID chisqId = (*env)->GetFieldID(env, method_class, "chisq", "D");
		(*env)->SetDoubleField(env, object, chisqId, chisq);
		// save c
		double c_temp[m];

		for (i = 0; i < m; i++) {
			c_temp[i] = gsl_vector_get (c, i);
		}

		jfieldID cId = (*env)->GetFieldID(env, method_class, "c", "[D");
		jdoubleArray C = (*env)->NewDoubleArray(env, m);
		(*env)->SetDoubleArrayRegion(env, C, 0, m, c_temp);
		(*env)->SetObjectField(env, object, cId, C);
	// preparing returned results
		double temp[s];
		for (i = 0, k = 0; i < m; i++) {
			for (j = 0; j < m; j++, k++) {
				temp[k] = gsl_matrix_get (cov, i, j);
			}
		}

		jdoubleArray cov_array = (*env)->NewDoubleArray(env, s);
		(*env)->SetDoubleArrayRegion(env, cov_array, 0, s, temp);

		
		(*env)->ReleaseDoubleArrayElements(env, java_x, x_temp, JNI_ABORT);

		return cov_array;
	}
//	gslMultiFitLinearEst
	JNIEXPORT jdouble JNICALL Java_least_1squares_1fitting_MultiParameterRegression_gslMultiFitLinearEst(JNIEnv * env, jobject object, jdoubleArray java_x, jdoubleArray java_cov){

		double * cov_temp = (*env) -> GetDoubleArrayElements(env, java_cov, NULL);
		size_t c_s = (*env)->GetArrayLength(env, java_cov);

		double * x_temp = (*env) -> GetDoubleArrayElements(env, java_x, NULL);
		size_t n = (*env)->GetArrayLength(env, java_x);

		double y, y_err;

		double c_temp[n];

		get_array_parameters_values_d(env, object, n, c_temp, "c");

		gsl_matrix * cov;
		gsl_vector * x, *c;

		cov = gsl_matrix_alloc (sqrt(c_s), sqrt(c_s));
		x = gsl_vector_alloc (n);
		c = gsl_vector_alloc (n);

		int i, j, k;

		for (i = 0, k = 0; i < sqrt(c_s); i++) {
			for (j = 0; j < sqrt(c_s); j++, k++) {
				gsl_matrix_set (cov, i, j, cov_temp[k]);
			}
		}

		for (i = 0; i < n; i ++) {
			gsl_vector_set (x, i, x_temp[i]);
			gsl_vector_set (c, i, c_temp[i]);
		}

		gsl_multifit_linear_est (x, c, cov, &y, &y_err);

	// save y_err
		jclass method_class = (*env)->GetObjectClass(env, object);
		jfieldID id_y_err = (*env)->GetFieldID(env, method_class, "y_err", "D");
		(*env)->SetDoubleField(env, object, id_y_err, y_err);

		(*env)->ReleaseDoubleArrayElements(env, java_cov, cov_temp, JNI_ABORT);
		(*env)->ReleaseDoubleArrayElements(env, java_x, x_temp, JNI_ABORT);
		
	 	return y;
	}