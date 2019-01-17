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
C_NAME = c_source
PACKAGE = Integration
J_NAME = NumericalIntegration
SO_NAME = lib$(J_NAME).so
SIG_NAME = $(J_NAME)_signatures.txt


# CC - name of c compilator
# IDIR - path to javac
# CFLAGS - options used during compilation of c programs
# DEPS - names of header files
CC = "gcczz
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

#CXX - name of c++ compilator
#CXXFLAGS - options used during compilation of c++ programs
#GDIR - path to generator source
#G_NAME - name of generator
CXX = g++
CXXFLAGS = -std=c++11
GDIR = ./generator/
G_NAME = c_generator

#.SUFFIXES - defines default list of suffixes, before make reads any makefile
#.PHONY - defines targets, which are not a files
.SUFFIXES: .java .class .h .txt .out .c .so .cpp
.PHONY: create_h, sig, sig_s, generate, build, run, clean, clean_g, clean_c, clean_all, doc

#compiles java files and create .class files
$(MAIN).class: 
	$(JC) $(MAIN).java

#creates header files, based on java classes, which contains native methods
create_h: $(MAIN).class
	$(JH) -o $(DEPS) $(PACKAGE).$(J_NAME) 

#prints the names of method signatures for java classes
sig:
	$(JP) $(JPFLAGS) $(PACKAGE).$(J_NAME)

#prints the names of method signatures for java classes to file
sig_s:
	$(JP) $(JPFLAGS) $(PACKAGE).$(J_NAME) >> $(SIG_NAME)

#creates generator file
$(G_NAME).cpp: 
	$(CXX) $(CXXFLAGS) $(GDIR)$(G_NAME).cpp -o $(G_NAME).out

#runs generator, that create default c files, based on header file, which will implement native methods
generate: create_h $(G_NAME).cpp sig_s
	./$(G_NAME).out -h $(J_NAME).h -c $(J_NAME).h -s $(SIG_NAME) 

#complites c programs, which implement native methods
$(SO_NAME):	./$(PACKAGE)/$(C_NAME).c $(DEPS)
	$(CC) ./$(PACKAGE)/$(C_NAME).c -o $@ $(CFLAGS)

#builds and runs ready program
build:	$(MAIN).class create_h $(SO_NAME) 
	$(JVM) -Djava.library.path=. $(MAIN)

#only runs ready program
run:
	java -Djava.library.path=. $(MAIN)

#cleans all files, created during building program
clean:	
	$(RM) ./$(PACKAGE)/*.class
	$(RM) ./Tests/*.class
	$(RM) *.class
	$(RM) *.log
	$(RM) $(DEPS)
	$(RM) $(SO_NAME)

#deletes generator file and file, which defines signatures
clean_g:
	$(RM) $(G_NAME).out
	$(RM) $(SIG_NAME)
	
#deletes default c file
clean_c:
	$(RM) $(J_NAME).c

#cleans all files, created by make commands
clean_all: clean clean_c clean_g

#cleans the documentation
clean_doc:
	$(RM) ./doc -r

#generates the documentation
doc:
	javadoc $(PACKAGE) -d ./doc

#opens the documentation
open_doc:
	firefox ./doc/$(PACKAGE)/$(J_NAME).html 

draw: QNG_result.txt
	gnuplot -e "filename='QNG_result.txt'; outputname='QNG result.png'" "draw.plg"