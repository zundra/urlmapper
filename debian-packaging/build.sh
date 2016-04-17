#!/bin/bash

set -x

PKG_NAME=tdfw
PROJECT_DESC="Turn Down For What! (URL Shortening Service)"
BUILD_NUMBER=1
VERSION=1.0.0
DEBS=/tmp/build
WORKING=$(pwd)
BUILDROOT=$WORKING/build
DEPLOY_PATH=$BUILDROOT/opt/$PKG_NAME
BEFORE_REMOVE_SCRIPT=$WORKING/debian-packaging/before-remove.sh
CONFIG_DIR=$DEPLOY_PATH/conf
PUBLIC_DIR=$DEPLOY_PATH/public
LOG_DIR=$DEPLOY_PATH/logs
TMP_DIR=$DEPLOY_PATH/tmp
NGINX_DIR=$BUILDROOT/etc/nginx
NGINX_SITES_AVAILABLE=$NGINX_DIR/sites-available
NGINX_SITES_ENABLED=$NGINX_DIR/sites-enabled
UPSTART_SCRIPT=$WORKING/conf/$PKG_NAME

# Start clean
rm -rf $DEBS
rm -rf $FAKEROOT
rm -rf $BUILDROOT

# Create directories used to build
mkdir -p $DEPLOY_PATH
mkdir -p $TMP_DIR
mkdir -p $DEPLOY_PATH
mkdir -p $LOG_DIR
mkdir -p $NGINX_SITES_AVAILABLE
mkdir -p $NGINX_SITES_ENABLED
mkdir -p $DEBS

cp -r $WORKING/conf $CONFIG_DIR
cp $WORKING/conf/$PKG_NAME.nginx $NGINX_SITES_AVAILABLE/$PKG_NAME

ln -s $NGINX_SITES_AVAILABLE/$PKG_NAME $NGINX_SITES_ENABLED/$PKG_NAME

#Create jar
sbt assembly

#Copy/create build assets
mv $WORKING/target/scala-2.11/$PKG_NAME-assembly-1.0.jar $DEPLOY_PATH/$PKG_NAME-assembly-1.0.jar

#Give the build root directory the proper permissions
chmod -R 755 $BUILDROOT

#Navgate to the directory where the final deb will be stored
cd $DEBS

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
    --before-remove $BEFORE_REMOVE_SCRIPT \
    -v $VERSION \
    --iteration $BUILD_NUMBER \
    --deb-user zundra \
    --deb-group zundra \
    -C $BUILDROOT .


# Copy deb package out of tmp directory and into local directory
mv $DEBS/tdfw_1.0.0-1_amd64.deb $WORKING/tdfw.deb

# End clean
rm -rf $DEBS
rm -rf $FAKEROOT
rm -rf $BUILDROOT


