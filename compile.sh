#!/bin/bash
# Script does not rely on PATH_TO_FX being defined.
# However, if you compile w/o this script, you probably ought to define that:
# export PATH_TO_FX=path/to/javafx-sdk-14/lib
#
# Also! If you need more modules, add them to this script, unless they are
#   already included transitively (just fuck around with it and figure out if it
#   is needed or not).
jfx_path=lib/javafx-sdk-14.0.1/lib
javac -d classes/ \
  --module-path  $jfx_path \
  --add-modules javafx.controls \
  src/*.java
javac -d classes/ \
  --module-path  $jfx_path \
  --add-modules javafx.controls \
  src/core/*.java
