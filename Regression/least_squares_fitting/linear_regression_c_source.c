#include "LinearRegression.h"
#include <stdlib.h>
#include <gsl/gsl_errno.h>

//	Getting an object double state value from provided class
	void get_parameters_values_d(JNIEnv * env, jobject object, double * a, const char * txt){

		jclass method_class = (*env)->GetObjectClass(env, object);

		jfieldID id_a = (*env)->GetFieldID(env, method_class, txt, "D");
		*a = (*env)->GetDoubleField(env, object, id_a);
	}
//	Extract datas from one dimention array to x[], y[] and w[]
	void extract(const int s, const double * datas, double * x, double * y, double * w){
		int i;
		for (i = 0; i < s; i += 3){
			x[(int)(i/3)] = datas[i];
			y[(int)(i/3)] = datas[i + 1];
			w[(int)(i/3)] = datas[i + 2];
		}
	}
//	FitLinear
	JNIEXPORT jdoubleArray JNICALL Java_least_1squares_1fitting_LinearRegression_gslFitLinear (JNIEnv * env, jobject object, jdoubleArray java_y){
	// pobranie wartosci przekazanej do funkcji tablicy javy
		double * datas = (*env) -> GetDoubleArrayElements(env, java_y, NULL);
		size_t s = (*env)->GetArrayLength(env, java_y);
	// wypełnienie tablic x, y i z, danymi z tablicy sekwencji danych
		double * x = (double *)malloc(sizeof(double) * (int)(s / 3));
		double * y = (double *)malloc(sizeof(double) * (int)(s / 3));
		double * w = (double *)malloc(sizeof(double) * (int)(s / 3));
		extract(s, datas, x, y, w);
	// przygotowanie zmiennych, które zostaną nadpisane
		double c0, c1, cov00, cov01, cov11, chisq;
	// wyłączenie domyślnego systemu zgłaszania błędów biblioteki GSL
		gsl_set_error_handler_off();
	// przeprowadzenie regresji liniowej
		int status = gsl_fit_linear (x, 1, y, 1, (int)(s / 3), &c0, &c1, &cov00, &cov01, &cov11, &chisq);
	// sprawdzenie, czy wystąpił błąd
		if (status != GSL_SUCCESS)
			(*env)->ThrowNew(env, (*env)->FindClass(env, "Integration/GSLErrors"), gsl_strerror (status));
	// zapisanie wartosci c0
		jclass method_class = (*env)->GetObjectClass(env, object);
		jfieldID id_c0 = (*env)->GetFieldID(env, method_class, "c0", "D");
		(*env)->SetDoubleField(env, object, id_c0, c0);
	// zapisanie wartosci c1
		jfieldID id_c1 = (*env)->GetFieldID(env, method_class, "c1", "D");
		(*env)->SetDoubleField(env, object, id_c1, c1);
	// zapisanie wartosci chisq
		jfieldID id_chisq = (*env)->GetFieldID(env, method_class, "chisq", "D");
		(*env)->SetDoubleField(env, object, id_chisq, chisq);
	// Przygotowanie tablicy, która zostanie zwrócona
		double temp[] = {cov00, cov01, cov01, cov11};
		jdoubleArray cov_array = (*env)->NewDoubleArray(env, 4);
		(*env)->SetDoubleArrayRegion(env, cov_array, 0, 4, temp);
	// zwolnienie zaalokowanej pamięci
		(*env)->ReleaseDoubleArrayElements(env, java_y, datas, JNI_ABORT);
	// zwrócenie tablicy, z wartosciami kowariancji współczynników
		return cov_array;
	}
//	FitLinear
	JNIEXPORT jdoubleArray JNICALL Java_least_1squares_1fitting_LinearRegression_gslFitWeigthLinear (JNIEnv * env, jobject object, jdoubleArray java_y){
	// pobranie wartosci przekazanej do funkcji tablicy javy
		double * datas = (*env) -> GetDoubleArrayElements(env, java_y, NULL);
		size_t s = (*env)->GetArrayLength(env, java_y);
	// wypełnienie tablic x, y i z, danymi z tablicy sekwencji danych
		double * x = (double *)malloc(sizeof(double) * (int)(s / 3));
		double * y = (double *)malloc(sizeof(double) * (int)(s / 3));
		double * w = (double *)malloc(sizeof(double) * (int)(s / 3));
		extract(s, datas, x, y, w);
	// przygotowanie zmiennych, które zostaną nadpisane
		double c0, c1, cov00, cov01, cov11, chisq;
	// wyłączenie domyślnego systemu zgłaszania błędów biblioteki GSL
		gsl_set_error_handler_off();
	// przeprowadzenie regresji liniowej
		int status = gsl_fit_wlinear (x, 1, w, 1, y, 1, (int)(s / 3), &c0, &c1, &cov00, &cov01, &cov11, &chisq);
	// sprawdzenie, czy wystąpił błąd
		if (status != GSL_SUCCESS)
			(*env)->ThrowNew(env, (*env)->FindClass(env, "Integration/GSLErrors"), gsl_strerror (status));
	// zapisanie wartosci c0
		jclass method_class = (*env)->GetObjectClass(env, object);
		jfieldID id_c0 = (*env)->GetFieldID(env, method_class, "c0", "D");
		(*env)->SetDoubleField(env, object, id_c0, c0);
	// zapisanie wartosci c1
		jfieldID id_c1 = (*env)->GetFieldID(env, method_class, "c1", "D");
		(*env)->SetDoubleField(env, object, id_c1, c1);
	// zapisanie wartosci chisq
		jfieldID id_chisq = (*env)->GetFieldID(env, method_class, "chisq", "D");
		(*env)->SetDoubleField(env, object, id_chisq, chisq);
	// Przygotowanie tablicy, która zostanie zwrócona
		double temp[] = {cov00, cov01, cov01, cov11};
		jdoubleArray cov_array = (*env)->NewDoubleArray(env, 4);
		(*env)->SetDoubleArrayRegion(env, cov_array, 0, 4, temp);
	// zwolnienie zaalokowanej pamięci
		(*env)->ReleaseDoubleArrayElements(env, java_y, datas, JNI_ABORT);
	// zwrócenie tablicy, z wartosciami kowariancji współczynników
		return cov_array;
	}
// Estimation
	JNIEXPORT jdouble JNICALL Java_least_1squares_1fitting_LinearRegression_gslFitLinearEst(JNIEnv * env, jobject object, jdouble x, jdouble cov00, jdouble cov01, jdouble cov11){
	// przygotowanie zmiennych, które zostaną nadpisane
		double yf, yf_err;
	// pobranie wartości c0 i c1
		double c0, c1;
		get_parameters_values_d(env, object, &c0, "c0");
		get_parameters_values_d(env, object, &c1, "c1");
	// wyłączenie domyślnego systemu zgłaszania błędów biblioteki GSL
		gsl_set_error_handler_off();
	// przeprowadzenie obliczeń na podstawie współczynników funkcji i ich macierzy kowariancji
		int status = gsl_fit_linear_est (x, c0, c1, cov00, cov01, cov11, &yf, &yf_err);
	// sprawdzenie, czy wystąpił błąd
		if (status != GSL_SUCCESS)
			(*env)->ThrowNew(env, (*env)->FindClass(env, "Integration/GSLErrors"), gsl_strerror (status));
	// zapisanie wartosci błędu yf_err
		jclass method_class = (*env)->GetObjectClass(env, object);
		jfieldID id_yf_err = (*env)->GetFieldID(env, method_class, "yf_err", "D");
		(*env)->SetDoubleField(env, object, id_yf_err, yf_err);
	// zwrócenie obliczonej wartości współrzędnej y
		return yf;
	}