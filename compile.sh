#!/bin/bash
# Script does not rely on PATH_TO_FX being defined.
# However, if you compile w/o this script, you probably ought to define that:
# export PATH_TO_FX=path/to/javafx-sdk-14/lib
#
# Also! If you need more JFX modules, add them to this script, unless they are
#   already included transitively (just fuck around with it and figure out if it
#   is needed or not).
# Lastly, the sources.txt needs to be updated with every new .java, so it is a
#	short-term solution.
jfx_path=lib/javafx-sdk-14.0.1/lib
javac -d classes \
	--module-path $jfx_path \
	--add-modules javafx.controls,javafx.fxml \
	@sources.txt
# Copy the fxml into the output dir
cp resources/fxml/main.fxml classes/core/main.fxml
