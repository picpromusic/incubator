DOWNLOAD_FROM="https://raw.github.com/picpromusic/incubator/master/misc/git-shell-skripts/"
DOWNLOAD_FROM_WEB='1'
if [ $# -eq 1 ]; then
  DOWNLOAD_FROM=$1
  if ! grep -q ^http:// - <<< $DOWNLOAD_FROM; then
    if ! grep -q ^https:// - <<< $DOWNLOAD_FROM; then
      DOWNLOAD_FROM_WEB='0'
    fi 
  fi
fi


PREV_BRANCH=$(git branch | grep ^\* | cut -d \* -f 2 | wc -l)
if [ $PREV_BRANCH != '0' ]; then
  PREV_BRANCH=$(git branch | grep ^\* | cut -d \* -f 2)
else
  PREV_BRANCH=master
fi
echo $PREV_BRANCH

if ! git checkout -q $PREV_BRANCH
then
	echo "Creating empty branch: $PREV_BRANCH"
	TREE_HASH=$(git write-tree)
	COMMIT_HASH=$(echo "init $PREV_BRANCH-branch" | git commit-tree $TREE_HASH)
	git update-ref refs/heads/$PREV_BRANCH $COMMIT_HASH
fi

FIRST_IMPORT='0'
if ! git checkout -q sync_tool
then
  FIRST_IMPORT='1'
  EMPTY_TREE=$(echo -n '' | git hash-object -t tree -w --stdin)
  echo "tree $EMPTY_TREE" > empty_tree.txt
  echo "author Import Script Of SyncTool <spam@spam.org> 0 +0000" >> empty_tree.txt
  echo "commiter Import Script Of SyncTool <spam@spam.org> 0 +0000" >> empty_tree.txt
  echo "" >> empty_tree.txt
  echo "Empty Branch sync_tool" >> empty_tree.txt
  EMPTY_COMMIT=$(echo -n '' | git hash-object -t commit -w empty_tree.txt)
  rm empty_tree.txt
  git update-ref refs/heads/sync_tool $EMPTY_COMMIT
  git checkout -q sync_tool
fi

if [ $DOWNLOAD_FROM_WEB = '1' ]; then
	scripts=$(wget -qO- $DOWNLOAD_FROM/script.list)
else 
	scripts=$(cat $DOWNLOAD_FROM/script.list)
fi
if [ $FIRST_IMPORT = '1' ]; then
  for e in $scripts; do
    if [ $DOWNLOAD_FROM_WEB = '1' ]; then
      wget -q $DOWNLOAD_FROM/$e
    else
      cp $DOWNLOAD_FROM/$e ./$e
    fi
	HASH=$(git hash-object -w $e)
	git update-index --add --cacheinfo 100755 $HASH $e
  done
  TREE_HASH=$(git write-tree)
  for e in $scripts; do
   git rm -q -f $e
  done
  git read-tree --prefix=bin $TREE_HASH
  TREE_HASH=$(git write-tree)
  git rm -q -r bin
  git read-tree --prefix=tools $TREE_HASH
  TREE_HASH=$(git write-tree)
  git rm -q -r tools

  COMMIT_HASH=$(echo 'import sync_util' | git commit-tree $TREE_HASH -p $EMPTY_COMMIT)
  git update-ref refs/heads/sync_tool $COMMIT_HASH
else
  for e in $scripts; do
    if [ $DOWNLOAD_FROM_WEB = '1' ]; then
      wget -qO tools/bin/$e $DOWNLOAD_FROM/$e
    else
      cp $DOWNLOAD_FROM/$e ./tools/bin/$e
    fi
    git add tools/bin/$e
  done
  git commit -m "updated sync_tool"
fi

echo "Branch sync_tool updated. You can now merge with your branch"
git checkout -q $PREV_BRANCH
