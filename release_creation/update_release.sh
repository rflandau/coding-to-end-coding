#!/bin/bash
#variables
version=v1

# create release repo
mkdir ctec_$version

# move to top of repo
cd ../
# compile
./compile.sh
# copy in new binaries and resources
cp -r classes/* release_creation/ctec_$version/
# move back down to release
cd release_creation/
# move constant files in
cp -r release_files/* ctec_$version
# zip up the directory
zip -r ctec_$version.zip ctec_$version
