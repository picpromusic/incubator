ISSET=`git config --get sync.$1.bundleout | wc -l`
if [ $ISSET != '0' ]; then
 BUNDLEOUT=`git config --get sync.$1.bundleout`
else
 echo "Config Parameter sync.$1.bundleout not set"
 exit
fi

ISSET=`git config --get sync.$1.out | wc -l`
if [ $ISSET != '0' ]; then
 TAGOUT=`git config --get sync.$1.out`
else
 echo "Config Parameter sync.$1.out not set"
 exit
fi 

ISSET=`git config --get sync.$1.signkey | wc -l`
if [ $ISSET != '0' ]; then
 SIGNKEY=`git config --get sync.$1.signkey`
 SIGNKEY_EXIST='1'
else
 SIGNKEY_EXIST='0'
fi

TEMP=`ls $BUNDLEOUT | sort -u | tail -1 | cut -d "." -f1`
TEMP=`echo $TEMP | sed 's/0*//'`
declare -i MAX=$TEMP
declare -i NEXT=$MAX+1
declare T=`seq -f %09.0f $NEXT $NEXT`.bundle


ISSET=`git show $TAGOUT | grep commit | head -1 | cut -d " " -f 2 | wc -l`
if [ $ISSET != '0' ]; then
 TAG_EXIST='1'
else
 TAG_EXIST='0'
fi

if [ $SIGNKEY_EXIST = '1' ]; then
 if [ $TAG_EXIST = '1' ]; then
  TAG_MESSAGE=`git log --oneline $TAGOUT..HEAD`
  git tag -f -u $SIGNKEY -m '$TAG_MESSAGE' out.sign.tag.$1
 else
  git tag -f -u $SIGNKEY -m 'Initial send' out.sign.tag.$1
 fi
fi

if [ $TAG_EXIST != '1' ]; then
 if [ $SIGNKEY_EXIST = '1' ]; then
  git bundle create $BUNDLEOUT/$T HEAD --tags=out.sign.tag.$1
 else
  git bundle create $BUNDLEOUT/$T HEAD
 fi
else 
 if [ $SIGNKEY_EXIST = '1' ]; then
  git bundle create $BUNDLEOUT/$T HEAD $TAGOUT --tags=out.sign.tag.$1
 else
  git bundle create $BUNDLEOUT/$T HEAD $TAGOUT 
 fi
fi

git tag -f `git config --get sync.$1.out`
echo $T
