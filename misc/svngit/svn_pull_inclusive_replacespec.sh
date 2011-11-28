local workrepo=$1
local ldir=`pwd`

cd $workrepo
git pull origin
git pull replacespec
cd $ldir

