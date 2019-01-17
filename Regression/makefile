help: 
	echo sig - prints the names of method signatures for java classes
	echo sig_s - prints the names of method signatures for java classes to file
	echo generate - runs generator
	echo build - builds and runs ready program
	echo run - only runs ready program
	echo doc - generates the documentation
	echo clean - cleans all files, created during building program
	echo clean_g - deletes generator file and file, which defines signatures
	echo clean_c - deletes default c file
	echo clean_all - cleans all files, created by make commands
	echo clean_doc - cleans the documentation

# MAIN - the name of java file, which contains main function
# C_NAME - files, which implement native methods of java classes
# J_NAME - Names of java classes, which contains native methods
# SO_NAME - Names of libres, created by C programs to implement java native methods
# SIG_NAME - name of file, contains signatures of native methods. 
# 			the file is created by sig_s command
MAIN = Main
PACKAGE = least_squares_fitting

C_NAME1 = roubust_linear_regression_c_source
J_NAME1 = RobustLinearRegression

C_NAME2 = regularized_linear_regression_c_source
J_NAME2 = RegularizedLinearRegression

C_NAME4 = multi_parameter_regression_c_source
J_NAME4 = MultiParameterRegression

C_NAME5 = linear_regression_c_source
J_NAME5 = LinearRegression

SO_NAME1 = lib$(J_NAME1).so
SO_NAME2 = lib$(J_NAME2).so
SO_NAME4 = lib$(J_NAME4).so
SO_NAME5 = lib$(J_NAME5).so

# CC - name of c compilator
# IDIR - path to javac
# CFLAGS - options used during compilation of c programs
CC = "gcc-4.9"
IDIR = /usr/lib/jvm/java-11-oracle/include
JAVAPART = -fpic -shared -I $(IDIR) -I $(IDIR)/linux
GSLDIR = -I/usr/local/include -L/usr/local/lib
GSLPART = -lgsl -lgslcblas -lm $(DIR)
CFLAGS = $(JAVAPART) $(GSLPART)
DEPS = ./$(PACKAGE)/$(J_NAME).h

#JC - name of java compilator
#JH - program, which creates header file, based on java classes, that contains native methods 
#JP - program, which prints java class, with signatures of its methods and components
#JPFLAGS - options used during running JP program
#JVM - name of Java Virtual Machine
JC = javac
JH = javah
JP = javap
JPFLAGS = -s -private
JVM = java

#.SUFFIXES - defines default list of suffixes, before make reads any makefile
#.PHONY - defines targets, which are not a files
.SUFFIXES: .java .class .h .txt .out .c .so
.PHONY: create_h, sig, build, run, clean, clean_c, doc

#compiles java files and create .class files
$(MAIN).class: 
	@$(JC) *.java
	@$(JC) ./$(PACKAGE)/*.java

#creates header files, based on java classes, which contains native methods
create_h1:
	@$(JH) -o ./$(PACKAGE)/$(J_NAME1).h $(PACKAGE).$(J_NAME1) 

create_h2:
	@$(JH) -o ./$(PACKAGE)/$(J_NAME2).h $(PACKAGE).$(J_NAME2)  

create_h4:
	@$(JH) -o ./$(PACKAGE)/$(J_NAME4).h $(PACKAGE).$(J_NAME4) 

create_h5:
	@$(JH) -o ./$(PACKAGE)/$(J_NAME5).h $(PACKAGE).$(J_NAME5) 

create_h: create_h1 create_h2 create_h4 create_h5

#complites c programs, which implement native methods
$(SO_NAME1):	
	@$(CC) ./$(PACKAGE)/$(C_NAME1).c -o $@ $(CFLAGS)

$(SO_NAME2):	
	@$(CC) ./$(PACKAGE)/$(C_NAME2).c -o $@ $(CFLAGS)

$(SO_NAME4):	
	@$(CC) ./$(PACKAGE)/$(C_NAME4).c -o $@ $(CFLAGS)

$(SO_NAME5):	
	@$(CC) ./$(PACKAGE)/$(C_NAME5).c -o $@ $(CFLAGS)

#builds and runs ready program
build:	$(MAIN).class create_h $(SO_NAME1)  $(SO_NAME2)  $(SO_NAME4)  $(SO_NAME5) 
	@$(JVM) -Djava.library.path=. $(MAIN)

#only runs ready program
run:
	@java -Djava.library.path=. $(MAIN)

#cleans all files, created during building program
clean:	
	$(RM) ./$(PACKAGE)/*.class
	$(RM) ./TEST/*.class
	$(RM) *.class
	$(RM) *.log
	$(RM) ./$(PACKAGE)/*.h
	$(RM) *.so
	
#deletes default c file
clean_c:
	$(RM) $(J_NAME).c

#cleans the documentation
clean_doc:
	$(RM) ./doc -r

#generates the documentation
doc:
	javadoc $(PACKAGE) -d ./doc

#opens the documentation
open_doc:
	firefox ./doc/$(PACKAGE)/$(J_NAME).html