#!/bin/bash

set -x

PKG_NAME=tdfw
PROJECT_DESC="Turn Down For What! (URL Shortening Service)"
BUILD_NUMBER=1
VERSION=1.0.0
DEBS=/tmp/build
BUILD_LOCAL_ID="$(date +%s)-1"
BUILD_FILE=$BUILD_LOCAL_ID-$GIT_COMMIT
BUILD_DIR=build
BUILD_HOME=/data/jenkins/workspace/
DEB_NAME="$PKG_NAME"_"$VERSION"-"$BUILD_NUMBER"_amd64.deb
MD5_FILE="$DEB_NAME".md5

WORKING=$(pwd)
FAKEROOT=$WORKING/fakeroot
BUILDROOT=$WORKING/build
DEPLOY_PATH=$BUILDROOT/opt/$PKG_NAME
UPSTART_SCRIPT=$WORKING/debian-packaging/$PKG_NAME.upstart
BEFORE_REMOVE_SCRIPT=$WORKING/debian-packaging/before-remove.sh
CONFIG_TEMPLATE_DIR=$DEPLOY_PATH/config_templates
CONFIG_DIR=$DEPLOY_PATH/config
LOG_DIR=$DEPLOY_PATH/logs
TMP_DIR=$DEPLOY_PATH/tmp

rm -rf $BUILDROOT

mkdir -p $DEPLOY_PATH
mkdir -p $TMP_DIR
mkdir -p $DEPLOY_PATH
mkdir -p $CONFIG_DIR
mkdir -p $LOG_DIR

if [ ! -d $DEBS ]; then
  mkdir -p $DEBS
fi

#Configure $FAKEROOT directories and build jar file
rm -rf $FAKEROOT
mkdir -p $FAKEROOT/project

cp -r src $FAKEROOT/src
cp project/plugins.sbt $FAKEROOT/project/plugins.sbt
cp build.sbt $FAKEROOT/build.sbt
cd $FAKEROOT

# Create jar
sbt assembly

#Copy/create build assets
mv $FAKEROOT/target/scala-2.11/tdfw-assembly-1.0.jar $DEPLOY_PATH/tdfw-assembly-1.0.jar

#Give the build root directory the proper permissions
chmod -R 755 $BUILDROOT

#Navgate to the directory where the final deb will be stored
cd $DEBS

if [ "$TESTING" = "true" ]; then
   $RM -f $DEB_NAME
fi

#Build .deb
fpm \
    -s dir \
    -t deb \
    -n $PKG_NAME \
    --vendor "Zundra" \
    -m "<zundra.daniel@gmail.com>" \
    --description "$PROJECT_DESC" \
    --directories /opt/$PKG_NAME \
    --url "http://tdfw.io" \
    --deb-upstart $UPSTART_SCRIPT \
    -v $VERSION \
    --iteration $BUILD_NUMBER \
    --deb-user zundra \
    --deb-group zundra \
    -C $BUILDROOT .


# Cleanup
if [ "$TESTING" = "false" ]; then
    rm -rf $BUILDROOT
fi
