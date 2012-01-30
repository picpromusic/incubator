#! /bin/bash
#Script with implements :"loading git from a foreign party"

function global_usage() {
  echo "Usage $0 foreign-party-name"
  exit
}

if [ $# -lt 1 ]; then
  global_usage
fi

if grep -q sync.$1.bundlein= - <<< $(git config -l); then
 BUNDLEIN=$(git config --get sync.$1.bundlein)
else
 echo "Config Parameter sync.$1.bundlein not set"
 echo "Configure Sync with conf_sync.sh"
 exit
fi

if grep -q sync.$1.in= - <<< $(git config -l); then
 BRANCHIN=$(git config --get sync.$1.in)
else
 echo "Config Parameter sync.$1.in not set"
 echo "Configure Sync with conf_sync.sh"
 exit
fi

NO_MASTER_YET='0'
if ! grep -q master - <<< $(git branch -av); then
 NO_MASTER_YET='1'
fi

if [ $NO_MASTER_YET != '1' ]; then
 if ! grep -q $BRANCHIN - <<< $(git branch -av); then
  echo "Branch $BRANCHIN does not exist"
  echo "Configure Sync with conf_sync.sh or create branch"
  exit
 fi
fi


toDecrypt=$(ls $BUNDLEIN | grep .gpg$ | sort -u )
decrypted=$(ls $BUNDLEIN | grep .decrypted$ )
for e in $toDecrypt; do
 if grep -q $e - <<< $decrypted; then
   newDecrypt=''
   for z in $toDecrypt; do
     if grep -v -q $z - <<< $e ; then
       newDecrypt=$(echo $newDecrypt $z)
     fi
   done
   toDecrypt=$newDecrypt
 fi
done


echo $toDecrypt
for t in $toDecrypt; do
  gpg -d $BUNDLEIN/$t >> $BUNDLEIN/$t.decrypted	
done

if [ $NO_MASTER_YET != '1' ]; then
 git checkout $BRANCHIN
fi
decrypted=$(ls $BUNDLEIN | grep .decrypted$ | sort -u)
for t in $decrypted; do
  git pull $BUNDLEIN/$t
  git fetch --tags $BUNDLEIN/$t 
done
if [ $NO_MASTER_YET = '1' ]; then
 git branch $BRANCHIN
fi
git checkout master
if ! git merge --ff-only $BRANCHIN; then
  echo "no fast-forward. please merge manually"
fi

