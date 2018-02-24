#!/bin/bash

_SCRIPT_DIR=$(dirname $(readlink -f $0))
_BASE_DIR=$_SCRIPT_DIR/../
_REPO_URL="https://github.com/Willena/sqlite-jdbc-crypt.git"

source $_SCRIPT_DIR/git-config.sh
set -ev

#VERSION=$(mvn -q -Dexec.executable="echo" -Dexec.args='${project.version}' --non-recursive org.codehaus.mojo:exec-maven-plugin:1.2.1:exec)

# Deploy a snapshot version only for master branch and jdk8
#if [[ "$TRAVIS_JDK_VERSION" == "oraclejdk8" ]]; then
  #if [[ "$TRAVIS_PULL_REQUEST" == "false" ]] && [[ "$VERSION" == *SNAPSHOT ]]; then
  #   make && mvn -s settings.xml deploy -DskipTests;
  #else
#    make;
  #fi;
#else
#  make linux64 && mvn test;
#fi;
cd $_BASE_DIR

git checkout travis_config

echo "The Given type is $TYPE"
echo "Starting build !"

make CODEC_TYPE=$TYPE;

ls /home/travis/build/Willena/sqlite-jdbc-crypt/target/
. $_BASE_DIR/VERSION


git tag -f "$version"
git push --force --tags --quiet "https://${GH_TOKEN}@github.com/Willena/sqlite-jdbc-crypt.git"
