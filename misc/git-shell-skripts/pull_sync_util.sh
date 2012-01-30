#TREE_HASH=$(git write-tree)
#COMMIT_HASH=$(echo 'init master-branch' | git commit-tree $TREE_HASH)
#git update-ref refs/heads/master $COMMIT_HASH

scripts=$(ls /home/sebastian/repos/github-incubator/misc/git-shell-skripts/)
for e in $scripts; do
 cp ls /home/sebastian/repos/github-incubator/misc/git-shell-skripts/$e .
 HASH=$(git hash-object -w $e)
 git update-index --add --cacheinfo 100755 $HASH $e
done

TREE_HASH=$(git write-tree)
for e in $scripts; do
 rm $e
done
git read-tree --prefix=bin $TREE_HASH
TREE_HASH=$(git write-tree)
git read-tree --prefix=tools $TREE_HASH
TREE_HASH=$(git write-tree)

COMMIT_HASH=$(echo 'import sync_util' | git commit-tree $TREE_HASH)
git update-ref refs/heads/sync_tool $COMMIT_HASH
git symbolic-ref HEAD refs/heads/sync_tool
