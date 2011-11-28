local barerepo=$1
local workrepo=$2
local ldir=`pwd`


git clone $barerepo $workrepo

echo "*** preparing work clone to fetch replacespecs and push all heads to base repo"
cd $workrepo
git config --add remote.origin.push +refs/heads/*:refs/heads/*
git config --add remote.replacespec.fetch +refs/replace/*:refs/replace/*
# maybe this must be connectrepo
git config --add remote.replacespec.url $barerepo 
cd $ldir
sh $DEBUG svn_pull_inclusive_replacespec.sh $workrepo
