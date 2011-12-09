#!/bin/bash
local tdir=$HOME/testrepos
local trepos=$tdir/repos
local tlog=$tdir/svngit.log
local workrepo=$trepos/working
local barerepo=$trepos/bare
local connectrepo=$trepos/connect
pushd
 
sh $DEBUG $SVNGIT_HOME/svn_git_test_init.sh $tdir $trepos
sh $DEBUG $SVNGIT_HOME/svn_init_connection_repository.sh file://$trepos/svnrepo $connectrepo
sh $DEBUG $SVNGIT_HOME/svn_init_bare_repository.sh $connectrepo $barerepo
sh $DEBUG $SVNGIT_HOME/svn_create_local_repository.sh $barerepo $workrepo

cd $workrepo
touch work.txt
git add work.txt
git commit -m "git commit of touched work.txt"
git push

sh $DEBUG $SVNGIT_HOME/svn_transfer_to_svn.sh $workrepo $barerepo $connectrepo

cd $workrepo
echo "commit2" >> work.txt
git add work.txt
git commit -m "git commit commit2"
echo "commit3" >> work.txt
git add work.txt
git commit -m "git commit commit3"
git push

sh $DEBUG $SVNGIT_HOME/svn_transfer_to_svn.sh $workrepo $barerepo $connectrepo

cd $trepos/svntrunk
echo "commit svn" >> work.txt
svn ci -m "commit from svn"

cd $workrepo
echo "commit git" >> work.txt
git add work.txt
git commit -m "commit from git"
git push

popd
