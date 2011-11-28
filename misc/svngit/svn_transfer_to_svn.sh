local workrepo=$1
local barerepo=$2
local connectrepo=$3
local ldir=`pwd`

cd $connectrepo
mv .git/refs/replace/* .git/refs/replace_backup
git pull $barerepo
git svn rebase
git svn dcommit
git pull $barerepo
mv .git/refs/replace_backup/* .git/refs/replace
git replace `git log -2 --oneline | cut -d " " -f1`

cd $barerepo
git fetch

cd $ldir
sh $DEBUG svn_pull_inclusive_replacespec.sh $workrepo
