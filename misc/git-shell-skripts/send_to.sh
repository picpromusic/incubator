#! /bin/bash
#Script with implements :"sending the diff git bundle to
#an foreign party"

function global_usage() {
  echo "Usage $0 send-destination"
  exit
}

if [ $# -lt 1 ]; then
  global_usage
fi

if grep -q sync.$1.bundleout= - <<< $(git config -l); then
 BUNDLEOUT=$(git config --get sync.$1.bundleout)
else
 echo "Config Parameter sync.$1.bundleout not set"
 echo "Configure Sync with conf_sync.sh"
 exit
fi

if grep -q sync.$1.out= - <<< $(git config -l); then
 TAGOUT=$(git config --get sync.$1.out)
else
 echo "Config Parameter sync.$1.out not set"
 echo "Configure Sync with conf_sync.sh"
 exit
fi 

if grep -q sync.$1.signkey= - <<< $(git config -l); then
 SIGNKEY=$(git config --get sync.$1.signkey)
 SIGNKEY_EXIST='1'
else
 SIGNKEY_EXIST='0'
fi

if [ $SIGNKEY_EXIST = '1' ]; then
 if grep -q sync.self.name= - <<< $(git config -l); then
  SELF_NAME=$(git config --get sync.self.name)
 else
  echo "Config Parameter sync.self.name not set"
  echo "Configure Sync with conf_sync.sh"
  exit
 fi
fi


TEMP=$(ls $BUNDLEOUT | sort -u | tail -1 | cut -d "." -f1)
TEMP=$(echo $TEMP | sed 's/0*//')
declare -i MAX=$TEMP
declare -i NEXT=$MAX+1
declare T=$(seq -f %09.0f $NEXT $NEXT).bundle


ISSET=$(git show $TAGOUT | grep commit | head -1 | cut -d " " -f 2 | wc -l)
if [ $ISSET != '0' ]; then
 TAG_EXIST='1'
else
 TAG_EXIST='0'
fi

if [ $SIGNKEY_EXIST = '1' ]; then
 if [ $TAG_EXIST = '1' ]; then
  TAG_MESSAGE=$(git log --oneline $TAGOUT..HEAD)
  git tag -f -u $SIGNKEY -m '$TAG_MESSAGE' out.sign.tag.$SELF_NAME.to.$1
 else
  git tag -f -u $SIGNKEY -m 'Initial send' out.sign.tag.$SELF_NAME.to.$1
 fi
fi

if [ $TAG_EXIST != '1' ]; then
 if [ $SIGNKEY_EXIST = '1' ]; then
  git bundle create $BUNDLEOUT/$T HEAD --tags=out.sign.tag.$SELF_NAME.to.$1
 else
  git bundle create $BUNDLEOUT/$T HEAD
 fi
else 
 if [ $SIGNKEY_EXIST = '1' ]; then
  git bundle create $BUNDLEOUT/$T HEAD $TAGOUT --tags=out.sign.tag.$SELF_NAME.to.$1
 else
  git bundle create $BUNDLEOUT/$T HEAD $TAGOUT 
 fi
fi

if grep -q sync.$1.email= - <<< $(git config -l); then
  gpg -e -r $(git config --get sync.$1.email) $BUNDLEOUT/$T
fi

git tag -f $TAGOUT
ls -lha $BUNDLEOUT/$T*
