#!/bin/bash
local tdir=$HOME/testrepos
local trepos=$tdir/repos
local tlog=$tdir/svngit.log
local workrepo=$trepos/working
local barerepo=$trepos/bare
local connectrepo=$trepos/connect

mkdir $tdir
echo "" >> $tlog
echo "" >> $tlog
echo "" >> $tlog
echo `date` >> $tlog

echo "*** Try to use git svn with multiple git stages inbetween" >> $tlog
mkdir $trepos
cd $trepos
echo "*** Cleanup $tdir" >> $tlog
rm -rf *

echo "*** Create SVN Repo and Setup trunk/tags/branches" >> $tlog
svnadmin create svnrepo >> $tlog
svn co file://$trepos/svnrepo/ svnco >> $tlog
mkdir svnco/trunk 
mkdir svnco/tags
mkdir svnco/branches
svn add svnco/* >> $tlog
svn ci -m "init svn structure" svnco >> $tlog
#Optionaly extract existing repo, instead of creating a new
#tar -xzf ../svnrepo.tgz
#svn co file://$tdir/svnrepo svnco

echo "*** Test Checkout of trunk/tags/svnbranches" >> $tlog
svn co file://$trepos/svnrepo/trunk svntrunk >> $tlog
svn co file://$trepos/svnrepo/tags svntags >> $tlog
svn co file://$trepos/svnrepo/branches svnbranches >> $tlog

echo "*** git svn clone of svn repo > $USER_svnclone" >> $tlog
echo "*** This is my gitsvn-connection-repository" >> $tlog
git svn clone --stdlayout file://$trepos/svnrepo/ $connectrepo >> $tlog

echo "*** prepare gitsvn-connection-repository" >> $tlog
cd $connectrepo
mkdir .git/refs/replace_backup

echo "*** adding first new commit to gitsvn-connection-repository" >> $tlog
echo "*** This change will be dcommited directly to svn" >> $tlog
touch git.txt
git add git.txt >> $tlog
git commit -m "git commit of touched git.txt" >>$tlog
git svn rebase >> $tlog
git svn dcommit >> $tlog

echo "*** updating the three svn checkouts" >> $tlog
cd ..
svn up svnco/ svntags/ svnbranches/ >> $tlog

echo "*** now create a bare clone of the gitsvn-connection-repository" >> $tlog
git clone --mirror $connectrepo/ $barerepo  >> $tlog
cd $barerepo
git fetch $barerepo
cd $trepos

echo "*** now create a work clone of the bare repository" >> $tlog 
git clone $barerepo $workrepo >> $tlog

echo "*** preparing work clone to fetch replacespecs and push all heads to base repo" >> $tlog
cd $workrepo
git config --add remote.origin.push +refs/heads/*:refs/heads/* >> $tlog
git config --add remote.replacespec.fetch +refs/replace/*:refs/replace/* >> $tlog
# maybe this must be connectrepo
git config --add remote.replacespec.url $barerepo >> $tlog

echo "*** updating work clone from bare repository" >> $tlog
git pull origin >> $tlog
git pull replacespec >> $tlog

echo "*** adding second new commit in work repository" >> $tlog
touch work.txt
git add work.txt >> $tlog
git commit -m "git commit of touched work.txt" >> $tlog

echo "*** pushing commit to bare repository " >> $tlog
git push >> $tlog

echo "*** now use my new procedure to update svn-connection-repository" >> $tlog
cd $connectrepo
echo "** backup all replacespecs " >> $tlog
mv .git/refs/replace/* .git/refs/replace_backup
echo "** pull in changes " >> $tlog
git pull $barerepo >> $tlog
echo "** rebasing and dcommiting to svn" >> $tlog
git svn rebase >> $tlog
git svn dcommit >> $tlog
echo "** repull change to get new svn changeset and old git changeset and merge of both" >> $tlog
git pull $barerepo >> $tlog
git log -3 --oneline >> $tlog
echo "** restore backuped replacespecs" >> $tlog
mv .git/refs/replace_backup/* .git/refs/replace
echo "** add new replacespec last commit 'merge' will be replaced with TODO:uebersetze:vorletzten 'svn commit' changeset" >> $tlog
git replace `git log -2 --oneline | cut -d " " -f1`  >> $tlog
git log -3 --oneline >> $tlog

echo "** pull changes'svn-dcommit' result to bare-repository" >> $tlog
cd $barerepo
git fetch >> $tlog

echo "** pull changes'svn-dcommit' result and changed replacesspec to work-repository" >> $tlog
cd $workrepo
git pull origin >> $tlog
git pull replacespec >> $tlog

echo "** make another change and commit it" >> $tlog
echo "insert 1 line" >> work.txt
git add work.txt >> $tlog
git commit -m "new content in work.txt" >> $tlog

echo "*** pushing commit to bare repository " >> $tlog
git push >> $tlog

echo "*** now use my new procedure to update svn-connection-repository" >> $tlog
cd $connectrepo
mv .git/refs/replace/* .git/refs/replace_backup
git pull $barerepo >> $tlog
git svn rebase >> $tlog
git svn dcommit >> $tlog
git pull $barerepo >> $tlog
mv .git/refs/replace_backup/* .git/refs/replace
git replace `git log -2 --oneline | cut -d " " -f1` >> $tlog
git log -3 --oneline >> $tlog

echo "** pull changes'svn-dcommit' result to bare-repository" >> $tlog
cd $barerepo
git fetch >> $tlog

echo "** pull changes'svn-dcommit' result and changed replacesspec to work-repository" >> $tlog
cd $workrepo
git pull origin >> $tlog
git pull replacespec >> $tlog


cd $tdir/..
