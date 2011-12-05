#!/bin/bash
local svnRepoUrl=$1
local connectrepo=$2

#so könnte man die erste rev ermitteln
#svn log file:///home/incubator/testrepos/repos/svnrepo/ | grep "^r.*$" | cut -d " " -f 1 | cut -d "r" -f 2 | tail -n 1

echo "*** git svn clone of svn repo > $USER_svnclone"
echo "*** This is my gitsvn-connection-repository"
git svn clone --stdlayout $svnRepoUrl $connectrepo

echo "*** prepare gitsvn-connection-repository"
mkdir $connectrepo/.git/refs/replace_backup
