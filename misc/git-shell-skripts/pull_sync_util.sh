#! /bin/bash
#Script that is able to pull the sync_tool into an existing git repository
#It pulls the sync_tool into the branch sync_tool where it can be merge into
#other branches

##Defaut Values
#Pull Sync Tool from SYNC_TOOL_STABLE tag of picpromusic/incubator@github
DOWNLOAD_FROM="https://raw.github.com/picpromusic/incubator/SYNC_TOOL_STABLE/misc/git-shell-skripts/"
#Pull Sync Tool from the web. See DOWNLOAD_FROM
DOWNLOAD_FROM_WEB='1'
ALTERNATE_DOWNLOAD_URL='0'

#Show usage info when --help is given as parameter
#Instead in the first parmeter another Pull Source can be given. (See usage message for examples)
if [ $# -eq 1 ]; then
  DOWNLOAD_FROM=$1
  ALTERNATE_DOWNLOAD_URL='1'
  if [ $DOWNLOAD_FROM = "--help" ]; then
    echo "Usage: $0 [alternating download_url or download_path]"
    echo "Use for latest UNSTABLE version: https://raw.github.com/picpromusic/incubator/SYNC_TOOL_DEV/misc/git-shell-skripts/"
    echo "Use for latest HEAD version : https://raw.github.com/picpromusic/incubator/master/misc/git-shell-skripts/"
    exit
  fi
fi

#Put Download URL in git config if an alternative for the download url is given
if [ $ALTERNATE_DOWNLOAD_URL = "1" ]; then
  git config sync.self.toolurl $DOWNLOAD_FROM
fi

#Put Download URL in git config if there is no config yet
if !(grep -q sync.self.toolurl - <<< $(git config -l)); then
  git config sync.self.toolurl $DOWNLOAD_FROM
fi

#Read Download URL from git config
DOWNLOAD_FROM=$(git config -l | grep sync.self.toolurl | cut -d "=" -f 2)

#Estimate if url is a wget compatible url
if ! grep -q ^http:// - <<< $DOWNLOAD_FROM; then
  if ! grep -q ^https:// - <<< $DOWNLOAD_FROM; then
    DOWNLOAD_FROM_WEB='0'
  fi 
fi

#Put Default Name of SyncTool Branch in git config when there is no config yet
if !(grep -q sync.self.toolbranch - <<< $(git config -l)); then
  git config sync.self.toolbranch sync_tool
fi

#Read SyncTool Branch from git config
TOOL_BRANCH=$(git config -l | grep sync.self.toolbranch | cut -d "=" -f 2)

#Estimate the actual selected branch if there is any
PREV_BRANCH=$(git branch | grep ^\* | cut -d \* -f 2 | wc -l)
if [ $PREV_BRANCH != '0' ]; then
  PREV_BRANCH=$(git branch | grep ^\* | cut -d \* -f 2)
  echo Previous Branch: $PREV_BRANCH
else
  PREV_BRANCH=master
fi

#Try to checkout the actual branch. This should trivialy work if the branch exist.
#If it does not work. Try to create that branch with git plumbing tools
if ! git checkout -q $PREV_BRANCH
then
	echo "Creating empty branch: $PREV_BRANCH"
	TREE_HASH=$(git write-tree)
	COMMIT_HASH=$(echo "init $PREV_BRANCH-branch" | git commit-tree $TREE_HASH)
	git update-ref refs/heads/$PREV_BRANCH $COMMIT_HASH
fi

FIRST_IMPORT='0'
#Check if there is a SyncTool Branch yet. If not create empty SyncTool Branch with
#git plumbing tools. If the Branch exists. The branch is checkouted
if ! git checkout -q $TOOL_BRANCH
then
  FIRST_IMPORT='1'
  # plumbing empty directory
  EMPTY_TREE=$(echo -n '' | git hash-object -t tree -w --stdin)
  # Prepare Commit Object in file empty_tree.txt
  echo "tree $EMPTY_TREE" > empty_tree.txt
  echo "author Import Script Of SyncTool <spam@spam.org> 0 +0000" >> empty_tree.txt
  echo "commiter Import Script Of SyncTool <spam@spam.org> 0 +0000" >> empty_tree.txt
  echo "" >> empty_tree.txt
  echo "Empty Branch $TOOL_BRANCH for the SyncTool" >> empty_tree.txt
  # plumbing the commit
  EMPTY_COMMIT=$(echo -n '' | git hash-object -t commit -w empty_tree.txt)
  # delete temporary file
  rm empty_tree.txt
  # Set SyncTool Branch ref to the created commit
  git update-ref refs/heads/$TOOL_BRANCH $EMPTY_COMMIT
  # Checkout the SyncTool Branch.
  git checkout -q sync_tool
fi

# Read Scriptlist from web or from file store
if [ $DOWNLOAD_FROM_WEB = '1' ]; then
	scripts=$(wget -qO- $DOWNLOAD_FROM/script.list)
else 
	scripts=$(cat $DOWNLOAD_FROM/script.list)
fi

# If this is the first import do some directory plumbing. Place the SyncTool
# in path/tree (./tools/bin)
if [ $FIRST_IMPORT = '1' ]; then
# for each script in the scriptlist
  for e in $scripts; do
# Download from web or copy from file storage  
    if [ $DOWNLOAD_FROM_WEB = '1' ]; then
      wget -q $DOWNLOAD_FROM/$e
    else
      cp $DOWNLOAD_FROM/$e ./$e
    fi
# Plumbing of a script    
	HASH=$(git hash-object -w $e)
	git update-index --add --cacheinfo 100755 $HASH $e
  done
# Plumbing of the content of the directory  
  TREE_HASH=$(git write-tree)
# Remove the scripts from the index
  for e in $scripts; do
   git rm -q -f $e
  done
# Plumbing of the bin directory
  git read-tree --prefix=bin $TREE_HASH
  TREE_HASH=$(git write-tree)
# Remove bin dir from the index
  git rm -q -r bin
# Plumbing of the tools directory
  git read-tree --prefix=tools $TREE_HASH
  TREE_HASH=$(git write-tree)
# Remove bin tools from the index
  git rm -q -r tools

# Do the commit of the directory structure and the initial pulled scripts
  COMMIT_HASH=$(echo 'import syncTool' | git commit-tree $TREE_HASH -p $EMPTY_COMMIT)
# Update the SyncTool Branch to the new commit
  git update-ref refs/heads/$TOOL_BRANCH $COMMIT_HASH
else
# for each script in the scriptlist
  for e in $scripts; do
# Download or copy the scripts to the existing tools/bin dir
    if [ $DOWNLOAD_FROM_WEB = '1' ]; then
      wget -qO tools/bin/$e $DOWNLOAD_FROM/$e
    else
      cp $DOWNLOAD_FROM/$e ./tools/bin/$e
    fi
# Add the script to the index    
    git add tools/bin/$e
  done
# Commit the update
  git commit -m "updated syncTool"
fi

echo "Branch $TOOL_BRANCH updated. You can now merge with your branch"
#Return to the original branch
git checkout -q $PREV_BRANCH
