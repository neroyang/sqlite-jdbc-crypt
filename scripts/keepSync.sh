#!/bin/bash

_SCRIPT_DIR=$(dirname $(readlink -f $0))
_BASE_DIR=$_SCRIPT_DIR/../
_REPO_URL="https://github.com/Willena/sqlite-jdbc-crypt.git"

source $_SCRIPT_DIR/git-config.sh

git remote add xenial https://github.com/xerial/sqlite-jdbc.git
git checkout master

echo "Trying to merge ! "
git pull xenial master

if [[ "$?" != "0" ]]; then
  echo "May have conflict ! Abort !"
  exit 1
fi

git add .
git commit -m "Updated to latest version of xenial/sqlite3-jdbc"


SQLITE_VERSION=$(curl -s "https://raw.githubusercontent.com/Willena/libsqlite3-wx-see/master/src/sqlite3.h" | grep "#define SQLITE_VERSION " | xargs | cut -d ' ' -f 3)
SQLITE_VERSION=$(echo "$SQLITE_VERSION" | sed -re 's|[^a-zA-Z0-9.\/-]||g')


echo "Changing version in VERSION file"
echo "version=$SQLITE_VERSION" > $_BASE_DIR/VERSION
cat $_BASE_DIR/VERSION

echo "Changing version in pom.xml"
sed -ire "s|\(<version>\)\(.*\)\(\-SNAPSHOT<\/version>\)|\1$SQLITE_VERSION\3|g" $_BASE_DIR/pom.xml
git add $_BASE_DIR/pom.xml
git add $_BASE_DIR/VERSION
git commit -m "Prepare for $SQLITE_VERSION"

git push --force --quiet "https://${GH_TOKEN}@github.com/Willena/sqlite-jdbc-crypt.git"
