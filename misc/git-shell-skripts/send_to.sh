BUNDLEOUT=`git config --get sync.$1.bundleout`
TAGOUT=`git config --get sync.$1.out`
TEMP=`ls $BUNDLEOUT | sort -u | tail -1 | cut -d "." -f1`
TEMP=`echo $TEMP | sed 's/0*//'`
declare -i MAX=$TEMP
declare -i NEXT=$MAX+1
declare T=`seq -f %09.0f $NEXT $NEXT`.bundle
TEMP='TEMP'
TEMP=`git show $TAGOUT | grep commit | head -1 | cut -d " " -f 2 | wc -l`
echo $TEMP
echo $TAGOUT
if [ $TEMP != '1' ]; then
 git bundle create $BUNDLEOUT/$T HEAD
else
 git bundle create $BUNDLEOUT/$T HEAD `git config --get sync.$1.out`
fi
git tag -f `git config --get sync.$1.out`
echo $T
