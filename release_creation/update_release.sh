#!/bin/bash
#variables
version=v1

# move to top of repo
cd ../
# compile
./compile.sh
# copy in new binaries and resources
cp -r classes/* release_creation/ctec_$version/
# move back down to release
cd release_creation/
zip -r ctec_$version.zip ctec_$version
