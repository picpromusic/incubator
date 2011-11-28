#!/bin/sh
local tdir=$HOME/testrepos
local trepos=$tdir/repos
local tlog=$tdir/svngit.log
local workrepo=$trepos/working
local barerepo=$trepos/bare
local connectrepo=$trepos/connect

sh svn_git_test_init.sh :
