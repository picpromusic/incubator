#!/bin/bash
local tdir=$1
local trepos=$2

mkdir $tdir
echo ""
echo ""
echo ""
echo `date`

echo "*** Try to use git svn with multiple git stages inbetween"
mkdir $trepos
cd $trepos
echo "*** Cleanup $trepos"
rm -rf *

echo "*** Create SVN Repo and Setup trunk/tags/branches"
svnadmin create svnrepo
svn co file://$trepos/svnrepo/ svnco
mkdir svnco/trunk
mkdir svnco/tags
mkdir svnco/branches
svn add svnco/*
svn ci -m "init svn structure" svnco
#Optionaly extract existing repo, instead of creating a new
#tar -xzf ../svnrepo.tgz
#svn co file://$tdir/svnrepo svnco

echo "*** Test Checkout of trunk/tags/svnbranches"
svn co file://$trepos/svnrepo/trunk svntrunk
svn co file://$trepos/svnrepo/tags svntags
svn co file://$trepos/svnrepo/branches svnbranches
