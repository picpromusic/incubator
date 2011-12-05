#!/bin/bash
local workrepo=$1
pushd

cd $workrepo
git pull origin
git pull replacespec

popd
