JFLAGS = -g -d src/..
JC = javac

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	src/SymbolTable.java \
	src/Code.java \
	src/Parser.java \
	src/Main.java


default: $(CLASSES:.java=.class)

clean:
	$(RM) *.class