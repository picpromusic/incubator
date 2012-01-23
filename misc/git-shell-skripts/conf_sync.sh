ISSET=$1'notset'
if [ $ISSET != 'notset' ]; then

 if [ $1 = '-self' ]; then
  ISSET=$2'notset'
  if [ $ISSET = 'notset' ]; then
   echo "Usage $0 -self own_name"
   exit
  fi
  git config sync.self.name=$2
  exit
 fi

 if [ $1 = '-other' ]; then
  ISSET=$2'notset'
  if [ $ISSET != 'notset' ]; then
   ISSET=$3'notset'
   if [ $ISSET != 'notset' ]; then
    git config sync.$2.in=$2_in
    git config sync.$2.out=$2_out
    git config sync.$2.bundlein=$3/in
    git config sync.$2.bundleout=$3/out
    exit
   fi
  fi
  echo "Usage $0 -other other_name abs_bundle_in_out_dir"
  exit
 fi

 if [ $1 = '-key' ]; then
  ISSET=$2'notset'
  if [ $ISSET != 'notset' ]; then
   ISSET=$3'notset'
   if [ $ISSET != 'notset' ]; then
    git config sync.$2.signkey=$3
    exit
   fi
  fi
  echo "Usage $0 -key other_name keyname" 
  exit
 fi

fi

echo "Usage $0 -self --> to set own name"
echo "Usage $0 -other --> to setup other syncrepo"
echo "Usage $0 -key --> to setup a gpg-key"
