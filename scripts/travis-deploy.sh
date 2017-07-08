#!/bin/bash
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

make;
#ls /home/travis/build/Willena/sqlite-jdbc-crypt/target/
git config credential.helper "store --file=.git/credentials"
echo "https://${GH_TOKEN}:@github.com" > .git/credentials
. ./VERSION
git tag $version
git push origin --tags

