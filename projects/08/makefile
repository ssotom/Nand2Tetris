JFLAGS = -g -d src/..
JC = javac

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	src/Parser.java \
	src/CodeWriter.java \
	src/Main.java


default: $(CLASSES:.java=.class)

clean:
	$(RM) *.class