#!/bin/sh
local tdir=$HOME/testrepos
local trepos=$tdir/repos
local tlog=$tdir/svngit.log
local workrepo=$trepos/working
local barerepo=$trepos/bare
local connectrepo=$trepos/connect
local ldir=`pwd`

sh $DEBUG svn_git_test_init.sh $tdir $trepos
sh $DEBUG svn_init_connection_repository.sh file://$trepos/svnrepo $connectrepo
sh $DEBUG svn_init_bare_repository.sh $connectrepo $barerepo
sh $DEBUG svn_create_local_repository.sh $barerepo $workrepo

cd $workrepo
touch work.txt
git add work.txt
git commit -m "git commit of touched work.txt"
git push

cd $ldir
sh $DEBUG svn_transfer_to_svn.sh $workrepo $barerepo $connectrepo 
