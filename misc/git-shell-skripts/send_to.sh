export BUNDLEOUT=`git config --get sync.$1.bundleout`
export TEMP=`ls $BUNDLEOUT | sort -u | tail -1 | cut -d "." -f1`
export TEMP=`echo $TEMP | sed 's/0*//'`
declare -i MAX=$TEMP
declare -i NEXT=$MAX+1
declare T=`seq -f %09.0f $NEXT $NEXT`.bundle
git bundle create $BUNDLEOUT/$T HEAD `git config --get sync.$1.out`
git tag -f `git config --get sync.$1.out`
echo $T
