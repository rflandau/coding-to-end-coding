#!/bin/bash
# See comments in compile.sh
jfx_path=lib/javafx-sdk-14.0.1/lib
java \
  --module-path $jfx_path \
  --add-modules javafx.controls,javafx.fxml \
  core.Main
