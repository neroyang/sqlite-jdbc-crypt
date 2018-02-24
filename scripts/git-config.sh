#!/bin/bash


_SCRIPT_DIR=$(dirname $(readlink -f $0))
_BASE_DIR=$_SCRIPT_DIR/../
_REPO_URL="https://github.com/Willena/sqlite-jdbc-crypt.git"

cd $_BASE_DIR
git config --local user.name "Villena Guillaume"
git config --local user.email "guiguivil@gmail.com"

git config remote.origin.fetch "+refs/heads/*:refs/remotes/origin/*"

set -x
