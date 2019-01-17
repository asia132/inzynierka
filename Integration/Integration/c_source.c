#include "NumericalIntegration.h"

#include <gsl/gsl_integration.h>
#include <gsl/gsl_errno.h>
#define LIMIT 1000
#define N 1000

//	a struct, which can contain methods datas, necessary to call it
	typedef struct java_method_datas{

		JNIEnv * env;
		jobject java_object;
		jmethodID java_method_id;
	} Method_Datas;
//	Function, which respondes to gsl_function indicator
	double function(double x, void * params){

		Method_Datas * method = (Method_Datas *)(params);

		return (*(method->env))->CallDoubleMethod(method->env, method->java_object, method->java_method_id, x);
	}
//	Getting a class mettod datas and save it into Method_Datas struct object
	Method_Datas create_method_object(JNIEnv * env, jobject object){

		jclass class_functions = (*env)->GetObjectClass(env, object);
		jmethodID id_method = (*env)->GetMethodID(env, class_functions, "integrableFunction", "(D)D");
		Method_Datas method = {env, object, id_method};

		return method;
	}
//	Getting an object double state value from provided class
	void get_component_value_d(JNIEnv * env, jobject object, double * a, const char * txt){

		jclass method_class = (*env)->GetObjectClass(env, object);

		jfieldID id_a = (*env)->GetFieldID(env, method_class, txt, "D");
		*a = (*env)->GetDoubleField(env, object, id_a);
	}
//	Getting an object integer state value from provided class
	void get_parameters_values_i(JNIEnv * env, jobject object, int * a, const char * txt){

		jclass method_class = (*env)->GetObjectClass(env, object);

		jfieldID id_a = (*env)->GetFieldID(env, method_class, txt, "I");
		*a = (*env)->GetIntField(env, object, id_a);
	}
// -- body of java native method gslQNG
	JNIEXPORT jdouble JNICALL Java_Integration_NumericalIntegration_gslQNG (JNIEnv * env, jobject object){
				// pobranie danych całkowanej funkcji
		Method_Datas method = create_method_object(env, object);
		// pobranie wartości pól składowych, odpowiedzialnych za przedział (a, b) i granicę dopuszczalnego błędu
		double a, b, epsabs, epsrel;

		get_component_value_d(env, object, &a, "a");
		get_component_value_d(env, object, &b, "b");
		get_component_value_d(env, object, &epsabs, "epsabs");
		get_component_value_d(env, object, &epsrel, "epsrel");
		// zadeklarowanie wartości, które zostaną nadpisane przez gsl_integration
		double result, error;
		size_t neval;
		// zapisanie danych funkcji całkowanej do obiektu struktury gsl_function
		gsl_function F;
		F.function = &function;
		F.params = &method;
		// wyłączenie domyślnego systemu zgłaszania błędów biblioteki GSL
		gsl_set_error_handler_off();
		// wywołanie właściwej funkcji całkującej
		int status = gsl_integration_qng(&F, a, b, epsabs, epsrel, &result, &error, &neval);
		// sprawdzenie, czy wystąpił błąd
		if (status != GSL_SUCCESS)
			(*env)->ThrowNew(env, (*env)->FindClass(env, "Integration/GSLErrors"), gsl_strerror (status));
		// zwrócenie wyniku całkowania		
		return result;

	}
// -- body of java native method gslQAG
	JNIEXPORT jdouble JNICALL Java_Integration_NumericalIntegration_gslQAG (JNIEnv * env, jobject object, jint key){
		// pobranie danych całkowanej funkcji
		Method_Datas method = create_method_object(env, object);
		// pobranie wartości pól składowych, odpowiedzialnych za przedział (a, b) i granicę dopuszczalnego błędu
		double a, b, epsabs, epsrel;
		
		get_component_value_d(env, object, &a, "a");
		get_component_value_d(env, object, &b, "b");
		get_component_value_d(env, object, &epsabs, "epsabs");
		get_component_value_d(env, object, &epsrel, "epsrel");
		// zadeklarowanie wartości, które zostaną nadpisane przez gsl_integration
		double result, error;
		// zapisanie danych funkcji całkowanej do obiektu struktury gsl_function
		gsl_function F;
		F.function = &function;
		F.params = &method;
		// Zaalokowanie pamięci dla przestrzeni do obliczeń	
		gsl_integration_workspace * w = gsl_integration_workspace_alloc(N);
		// wyłączenie domyślnego systemu zgłaszania błędów biblioteki GSL
		gsl_set_error_handler_off();
		// wywołanie właściwej funkcji całkującej
		int status = gsl_integration_qag(&F, a, b, epsabs, epsrel, LIMIT, key, w, &result, &error);
		// sprawdzenie, czy wystąpił błąd
		if (status != GSL_SUCCESS)
			(*env)->ThrowNew(env, (*env)->FindClass(env, "Integration/GSLErrors"), gsl_strerror (status));
		// zwolnienie wcześniej zaalokowanej pamięci
		gsl_integration_workspace_free(w);
		// zwrócenie wyniku całkowania		
		return result;

	}
// -- body of java native method gslQAGS
	JNIEXPORT jdouble JNICALL Java_Integration_NumericalIntegration_gslQAGS (JNIEnv * env, jobject object){
		// pobranie danych całkowanej funkcji
		Method_Datas method = create_method_object(env, object);
		// pobranie wartości pól składowych, odpowiedzialnych za przedział (a, b) i granicę dopuszczalnego błędu
		double a, b, epsabs, epsrel;
		get_component_value_d(env, object, &a, "a");
		get_component_value_d(env, object, &b, "b");
		get_component_value_d(env, object, &epsabs, "epsabs");
		get_component_value_d(env, object, &epsrel, "epsrel");
		// zadeklarowanie wartości, które zostaną nadpisane przez gsl_integration
		double result, error;
		// Zaalokowanie pamięci dla przestrzeni do obliczeń	
		gsl_integration_workspace * w = gsl_integration_workspace_alloc(N);
		// zapisanie danych funkcji całkowanej do obiektu struktury gsl_function
		gsl_function F;
		F.function = &function;
		F.params = &method;
		// wyłączenie domyślnego systemu zgłaszania błędów biblioteki GSL
		gsl_set_error_handler_off();
		// wywołanie właściwej funkcji całkującej
		int status = gsl_integration_qags(&F, a, b, epsabs, epsrel, LIMIT, w, &result, &error);
		// sprawdzenie, czy wystąpił błąd
		if (status != GSL_SUCCESS)
			(*env)->ThrowNew(env, (*env)->FindClass(env, "Integration/GSLErrors"), gsl_strerror (status));
		// zwolnienie wcześniej zaalokowanej pamięci
		gsl_integration_workspace_free(w);
		// zwrócenie wyniku całkowania		
		return result;

	}
// -- body of java native method gslQAGP
	JNIEXPORT jdouble JNICALL Java_Integration_NumericalIntegration_gslQAGP (JNIEnv * env, jobject object, jdoubleArray java_pts){
		// pobranie danych całkowanej funkcji
		Method_Datas method = create_method_object(env, object);
		// pobranie wartości pól składowych, odpowiedzialnych za granicę dopuszczalnego błędu
		double epsabs, epsrel;
		get_component_value_d(env, object, &epsabs, "epsabs");
		get_component_value_d(env, object, &epsrel, "epsrel");
		// zadeklarowanie wartości, które zostaną nadpisane przez gsl_integration
		double result, error;
		// wyłączenie domyślnego systemu zgłaszania błędów biblioteki GSL
		gsl_set_error_handler_off();
		// Zaalokowanie pamięci dla przestrzeni do obliczeń	
		gsl_integration_workspace * w = gsl_integration_workspace_alloc(N);
		// zapisanie danych funkcji całkowanej do obiektu struktury gsl_function
		gsl_function F;
		F.function = &function;
		F.params = &method;
		// pobranie elementów i rozmiaru tablicy Javy do wskaźnika języka C
		double * pts = (*env) -> GetDoubleArrayElements(env, java_pts, NULL);
		size_t npts = (*env)->GetArrayLength(env, java_pts);
		// wywołanie właściwej funkcji całkującej
		int status = gsl_integration_qagp (&F, pts, npts, epsabs, epsrel, LIMIT, w, &result, &error);
		// sprawdzenie, czy wystąpił błąd
		if (status != GSL_SUCCESS)
			(*env)->ThrowNew(env, (*env)->FindClass(env, "Integration/GSLErrors"), gsl_strerror (status));
		// zwolnienie wcześniej zaalokowanej pamięci
		gsl_integration_workspace_free(w);
		// zwrócenie wyniku całkowania		
		return result;

	}
// -- body of java native method gslQAGI
	JNIEXPORT jdouble JNICALL Java_Integration_NumericalIntegration_gslQAGI (JNIEnv * env, jobject object){
		// pobranie danych całkowanej funkcji
		Method_Datas method = create_method_object(env, object);
		// zadeklarowanie wartości, które zostaną nadpisane przez gsl_integration
		double result, error;
		// pobranie wartości pól składowych, odpowiedzialnych za granicę dopuszczalnego błędu
		double epsabs, epsrel;
		get_component_value_d(env, object, &epsabs, "epsabs");
		get_component_value_d(env, object, &epsrel, "epsrel");
		// Zaalokowanie pamięci dla przestrzeni do obliczeń	
		gsl_integration_workspace * w = gsl_integration_workspace_alloc(N);
		// zapisanie danych funkcji całkowanej do obiektu struktury gsl_function
		gsl_function F;
		F.function = &function;
		F.params = &method;
		// wyłączenie domyślnego systemu zgłaszania błędów biblioteki GSL
		gsl_set_error_handler_off();
		// wywołanie właściwej funkcji całkującej
		int status = gsl_integration_qagi (&F, epsabs, epsrel, LIMIT, w, &result, &error);
		// sprawdzenie, czy wystąpił błąd
		if (status != GSL_SUCCESS)
			(*env)->ThrowNew(env, (*env)->FindClass(env, "Integration/GSLErrors"), gsl_strerror (status));
		// zwolnienie wcześniej zaalokowanej pamięci
		gsl_integration_workspace_free(w);
		// zwrócenie wyniku całkowania		
		return result;

	}
// -- body of java native method gslQAGIU
	JNIEXPORT jdouble JNICALL Java_Integration_NumericalIntegration_gslQAGIU (JNIEnv * env, jobject object){
		// pobranie danych całkowanej funkcji
		Method_Datas method = create_method_object(env, object);
		// pobranie wartości pól składowych, odpowiedzialnych za granicę dopuszczalnego błędu oraz wartości a, definiującej obszar całkowania
		double a, epsabs, epsrel;
		get_component_value_d(env, object, &a, "a");
		get_component_value_d(env, object, &epsabs, "epsabs");
		get_component_value_d(env, object, &epsrel, "epsrel");
		// zadeklarowanie wartości, które zostaną nadpisane przez gsl_integration
		double result, error;
		// Zaalokowanie pamięci dla przestrzeni do obliczeń	
		gsl_integration_workspace * w = gsl_integration_workspace_alloc(N);
		// zapisanie danych funkcji całkowanej do obiektu struktury gsl_function
		gsl_function F;
		F.function = &function;
		F.params = &method;
		// wyłączenie domyślnego systemu zgłaszania błędów biblioteki GSL
		gsl_set_error_handler_off();
		// wywołanie właściwej funkcji całkującej
		int status = gsl_integration_qagiu (&F, a, epsabs, epsrel, LIMIT, w, &result, &error);
		// sprawdzenie, czy wystąpił błąd
		if (status != GSL_SUCCESS)
			(*env)->ThrowNew(env, (*env)->FindClass(env, "Integration/GSLErrors"), gsl_strerror (status));
		// zwolnienie wcześniej zaalokowanej pamięci
		gsl_integration_workspace_free(w);
		// zwrócenie wyniku całkowania		
		return result;

	}
// -- body of java native method gslQAGIL
	JNIEXPORT jdouble JNICALL Java_Integration_NumericalIntegration_gslQAGIL (JNIEnv * env, jobject object){
		// pobranie danych całkowanej funkcji
		Method_Datas method = create_method_object(env, object);
		// pobranie wartości pól składowych, odpowiedzialnych za granicę dopuszczalnego błędu oraz wartości b, definiującej obszar całkowania
		double b, epsabs, epsrel;
		get_component_value_d(env, object, &b, "b");
		get_component_value_d(env, object, &epsabs, "epsabs");
		get_component_value_d(env, object, &epsrel, "epsrel");
		// zadeklarowanie wartości, które zostaną nadpisane przez gsl_integration
		double result, error;
		// Zaalokowanie pamięci dla przestrzeni do obliczeń	
		gsl_integration_workspace * w = gsl_integration_workspace_alloc(N);
		// zapisanie danych funkcji całkowanej do obiektu struktury gsl_function
		gsl_function F;
		F.function = &function;
		F.params = &method;
		// wyłączenie domyślnego systemu zgłaszania błędów biblioteki GSL
		gsl_set_error_handler_off();
		// wywołanie właściwej funkcji całkującej
		int status = gsl_integration_qagil (&F, b, epsabs, epsrel, LIMIT, w, &result, &error);
		// sprawdzenie, czy wystąpił błąd
		if (status != GSL_SUCCESS)
			(*env)->ThrowNew(env, (*env)->FindClass(env, "Integration/GSLErrors"), gsl_strerror (status));
		// zwolnienie wcześniej zaalokowanej pamięci
		gsl_integration_workspace_free(w);
		// zwrócenie wyniku całkowania		
		return result;

	}
// -- body of java native method gslQAWC
	JNIEXPORT jdouble JNICALL Java_Integration_NumericalIntegration_gslQAWC (JNIEnv * env, jobject object, jdouble c){
		// pobranie danych całkowanej funkcji
		Method_Datas method = create_method_object(env, object);
		// pobranie wartości pól składowych, odpowiedzialnych za przedział (a, b) i granicę dopuszczalnego błędu
		double a, b, epsabs, epsrel;
		get_component_value_d(env, object, &a, "a");
		get_component_value_d(env, object, &b, "b");
		get_component_value_d(env, object, &epsabs, "epsabs");
		get_component_value_d(env, object, &epsrel, "epsrel");
		// zadeklarowanie wartości, które zostaną nadpisane przez gsl_integration
		double result, error;
		// Zaalokowanie pamięci dla przestrzeni do obliczeń	
		gsl_integration_workspace * w = gsl_integration_workspace_alloc(N);
		// zapisanie danych funkcji całkowanej do obiektu struktury gsl_function
		gsl_function F;
		F.function = &function;
		F.params = &method;
		// wyłączenie domyślnego systemu zgłaszania błędów biblioteki GSL
		gsl_set_error_handler_off();
		// wywołanie właściwej funkcji całkującej
		int status = gsl_integration_qawc  (&F, a, b, c, epsabs, epsrel, LIMIT, w, &result, &error);
		// sprawdzenie, czy wystąpił błąd
		if (status != GSL_SUCCESS)
			(*env)->ThrowNew(env, (*env)->FindClass(env, "Integration/GSLErrors"), gsl_strerror (status));
		// zwolnienie wcześniej zaalokowanej pamięci
		gsl_integration_workspace_free(w);
		// zwrócenie wyniku całkowania		
		return result;
	}
// -- body of java native method gslQAWS
	JNIEXPORT jdouble JNICALL Java_Integration_NumericalIntegration_gslQAWS (JNIEnv * env, jobject object, jdouble alpha, jdouble beta, jint mu, jint nu){
		// pobranie danych całkowanej funkcji
		Method_Datas method = create_method_object(env, object);
		// pobranie wartości pól składowych, odpowiedzialnych za przedział (a, b) i granicę dopuszczalnego błędu
		double a, b, epsabs, epsrel;
		get_component_value_d(env, object, &a, "a");
		get_component_value_d(env, object, &b, "b");
		get_component_value_d(env, object, &epsabs, "epsabs");
		get_component_value_d(env, object, &epsrel, "epsrel");
		// zadeklarowanie wartości, które zostaną nadpisane przez gsl_integration
		double result, error;
		// Zaalokowanie pamięci dla przestrzeni do obliczeń	
		gsl_integration_workspace * w = gsl_integration_workspace_alloc(N);
		// zapisanie danych funkcji całkowanej do obiektu struktury gsl_function
		gsl_function F;
		F.function = &function;
		F.params = &method;

	    gsl_set_error_handler_off();
		
		gsl_integration_qaws_table * qaws_table = gsl_integration_qaws_table_alloc (alpha, beta, mu, nu);
		// wywołanie właściwej funkcji całkującej
		int status = gsl_integration_qaws (&F, a, b, qaws_table, epsabs, epsrel, LIMIT, w, &result, &error);
		// sprawdzenie, czy wystąpił błąd
		if (status != GSL_SUCCESS)
			(*env)->ThrowNew(env, (*env)->FindClass(env, "Integration/GSLErrors"), gsl_strerror (status));

		gsl_integration_qaws_table_free (qaws_table);		// zwolnienie wcześniej zaalokowanej pamięci
		gsl_integration_workspace_free(w);
		// zwrócenie wyniku całkowania		
		return result;

	}
// -- body of java native method gslQAWO
	JNIEXPORT jdouble JNICALL Java_Integration_NumericalIntegration_gslQAWO (JNIEnv * env, jobject object, jdouble omega, jint mode){
		// pobranie danych całkowanej funkcji
		Method_Datas method = create_method_object(env, object);
		// pobranie wartości pól składowych, odpowiedzialnych za przedział (a, b) i granicę dopuszczalnego błędu
		double a, b, epsabs, epsrel;
		get_component_value_d(env, object, &a, "a");
		get_component_value_d(env, object, &b, "b");
		get_component_value_d(env, object, &epsabs, "epsabs");
		get_component_value_d(env, object, &epsrel, "epsrel");
		// zadeklarowanie wartości, które zostaną nadpisane przez gsl_integration
		double result, error;
		// Zaalokowanie pamięci dla przestrzeni do obliczeń	
		gsl_integration_workspace * w = gsl_integration_workspace_alloc(N);
		// zapisanie danych funkcji całkowanej do obiektu struktury gsl_function
		gsl_function F;
		F.function = &function;
		F.params = &method;
		// Zdefiniowanie wartości L, na podstawie wartości zmiennych a i b
		double L = b - a;
		// wyłączenie domyślnego systemu zgłaszania błędów biblioteki GSL
	    gsl_set_error_handler_off();
		
		gsl_integration_qawo_table * qawo_table = gsl_integration_qawo_table_alloc (omega, L, mode, N);
		// wywołanie właściwej funkcji całkującej
		int status = gsl_integration_qawo (&F, a, epsabs, epsrel, LIMIT, w, qawo_table, &result, &error);
		// sprawdzenie, czy wystąpił błąd
		if (status != GSL_SUCCESS)
			(*env)->ThrowNew(env, (*env)->FindClass(env, "Integration/GSLErrors"), gsl_strerror (status));
		// zwolnienie wcześniej zaalokowanej pamięci
		gsl_integration_qawo_table_free (qawo_table);		
		gsl_integration_workspace_free(w);
		// zwrócenie wyniku całkowania		
		return result;

	}
// -- body of java native method gslQAWF
	JNIEXPORT jdouble JNICALL Java_Integration_NumericalIntegration_gslQAWF (JNIEnv * env, jobject object, jdouble omega, jint mode){
		// pobranie danych całkowanej funkcji
		Method_Datas method = create_method_object(env, object);
		// pobranie wartości pól składowych, odpowiedzialnych za przedział (a, b) i granicę dopuszczalnego błędu
		double a, b, epsabs;
		get_component_value_d(env, object, &a, "a");
		get_component_value_d(env, object, &b, "b");
		get_component_value_d(env, object, &epsabs, "epsabs");
		// zadeklarowanie wartości, które zostaną nadpisane przez gsl_integration
		double result, error;
		// zapisanie danych funkcji całkowanej do obiektu struktury gsl_function
		gsl_function F;
		F.function = &function;
		F.params = &method;
		// Zdefiniowanie wartości L, na podstawie wartości zmiennych a i b
		double L = b - a; 
		// wyłączenie domyślnego systemu zgłaszania błędów biblioteki GS
	    gsl_set_error_handler_off();
		// Zaalokowanie pamięci dla przestrzeni do obliczeń	
		gsl_integration_workspace * w = gsl_integration_workspace_alloc(N);
		gsl_integration_workspace * cycle_w = gsl_integration_workspace_alloc(N);
		gsl_integration_qawo_table * qawo_table = gsl_integration_qawo_table_alloc (omega, L, mode, N);
		// wywołanie właściwej funkcji całkującej
		int status = gsl_integration_qawf (&F, a, epsabs, LIMIT, w, cycle_w, qawo_table, &result, &error);
		// sprawdzenie, czy wystąpił błąd
		if (status != GSL_SUCCESS)
			(*env)->ThrowNew(env, (*env)->FindClass(env, "Integration/GSLErrors"), gsl_strerror (status));
		// zwolnienie wcześniej zaalokowanej pamięci
		gsl_integration_qawo_table_free (qawo_table);
		gsl_integration_workspace_free(w);
		// zwrócenie wyniku całkowania		
		return result;
	}
// -- body of java native method gslCQUAD
	JNIEXPORT jdouble JNICALL Java_Integration_NumericalIntegration_gslCQUAD (JNIEnv * env, jobject object){
		// pobranie danych całkowanej funkcji
		Method_Datas method = create_method_object(env, object);
		// pobranie wartości pól składowych, odpowiedzialnych za przedział (a, b) i granicę dopuszczalnego błędu
		double a, b, epsabs, epsrel;
		get_component_value_d(env, object, &a, "a");
		get_component_value_d(env, object, &b, "b");
		get_component_value_d(env, object, &epsabs, "epsabs");
		get_component_value_d(env, object, &epsrel, "epsrel");
		// zadeklarowanie wartości, które zostaną nadpisane przez gsl_integration
		double result, error;
		size_t nevals;
		// Zaalokowanie pamięci dla przestrzeni do obliczeń	
		gsl_integration_workspace * w = gsl_integration_workspace_alloc(N);
		// zapisanie danych funkcji całkowanej do obiektu struktury gsl_function
		gsl_function F;
		F.function = &function;
		F.params = &method;
		// wyłączenie domyślnego systemu zgłaszania błędów biblioteki GS
	    gsl_set_error_handler_off();
	    // Zaalokowanie pamięci dla przestrzeni do obliczeń	
		gsl_integration_cquad_workspace * cquad_workspace = gsl_integration_cquad_workspace_alloc (N);
		// wywołanie właściwej funkcji całkującej
		int status = gsl_integration_cquad (&F, a, b, epsabs, epsrel, cquad_workspace, &result, &error, &nevals);
		// sprawdzenie, czy wystąpił błąd
		if (status != GSL_SUCCESS)
			(*env)->ThrowNew(env, (*env)->FindClass(env, "Integration/GSLErrors"), gsl_strerror (status));
		// zwolnienie wcześniej zaalokowanej pamięci
		gsl_integration_cquad_workspace_free (cquad_workspace);		
		gsl_integration_workspace_free(w);
		// zwrócenie wyniku całkowania		
		return result;
	}
// -- body of java native method gslGLFIXED
	JNIEXPORT jdoubleArray JNICALL Java_Integration_NumericalIntegration_gslGLFIXED (JNIEnv * env, jobject object){
		// pobranie danych całkowanej funkcji
		Method_Datas method = create_method_object(env, object);

		double a, b;
		
		get_component_value_d(env, object, &a, "a");
		get_component_value_d(env, object, &b, "b");

		// double result[20];
		// zapisanie danych funkcji całkowanej do obiektu struktury gsl_function
		gsl_function F;
		F.function = &function;
		F.params = &method;

		double x[10];
		double w[10];
		size_t n = 10, i;


		gsl_integration_glfixed_table * glfixed_table = gsl_integration_glfixed_table_alloc (n);
		// wyłączenie domyślnego systemu zgłaszania błędów biblioteki GSL
		gsl_set_error_handler_off();
		// wywołanie właściwej funkcji całkującej
		int status = gsl_integration_glfixed (&F, a, b, glfixed_table);
		// sprawdzenie, czy wystąpił błąd
		if (status != GSL_SUCCESS)
			(*env)->ThrowNew(env, (*env)->FindClass(env, "Integration/GSLErrors"), gsl_strerror (status));

		jdoubleArray result = (*env)->NewDoubleArray(env, n * 2);
		double temp[20];
		
		for (i = 0; i < n; i++){
			temp[i] = glfixed_table->x[i];
			temp[i + n] = glfixed_table->w[i];
		}

		(*env)->SetDoubleArrayRegion(env, result, 0, n * 2, temp);

		for (i = 0; i < n; i++){
			temp[i] = glfixed_table->x[i];
			temp[i + n] = glfixed_table->w[i];
		}

		gsl_integration_glfixed_table_free (glfixed_table);
		// zwrócenie wyniku całkowania		
		return result;

	}
