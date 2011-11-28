local svnRepoUrl=$1
local connectrepo=$2


echo "*** git svn clone of svn repo > $USER_svnclone"
echo "*** This is my gitsvn-connection-repository"
git svn clone --stdlayout $svnRepoUrl $connectrepo

echo "*** prepare gitsvn-connection-repository"
mkdir $connectrepo/.git/refs/replace_backup
