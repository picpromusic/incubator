#!/bin/bash
local connectrepo=$1
local barerepo=$2
pushd

echo "*** now create a bare clone of the gitsvn-connection-repository"
git clone --mirror $connectrepo/ $barerepo 
cd $barerepo
git fetch $barerepo

popd
