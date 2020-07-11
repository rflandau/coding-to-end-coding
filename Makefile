JFX       = lib/javafx-sdk-14.0.1/lib
MOD       = --add-modules javafx.controls,javafx.fxml
SRC       = @sources.txt
CLASS     = classes
JC        = javac
JCFLAGS   = -d ${CLASS} --module-path ${JFX} ${MOD}
JFLAGS    = --module-path ${JFX} ${MOD}
J         = java
MAIN      = core.Main

all:
	${JC} ${JCFLAGS} ${SRC}

clean:
	rm -r classes
	mkdir classes

test:
	./run.sh

ci:
	git commit -am "soft tabs and makefile changes"
	git push
