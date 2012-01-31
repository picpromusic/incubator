if ! git checkout -q master
then
	echo "Creating empty master branch"
	TREE_HASH=$(git write-tree)
	COMMIT_HASH=$(echo 'init master-branch' | git commit-tree $TREE_HASH)
	git update-ref refs/heads/master $COMMIT_HASH
fi

if ! git checkout -q sync_tool
then
	EMPTY_TREE=$(echo -n '' | git hash-object -t tree -w --stdin)
	echo "tree $EMPTY_TREE" > empty_tree.txt
	echo "author Import Script Of SyncTool <spam@spam.org> 0 +0000" >> empty_tree.txt
	echo "commiter Import Script Of SyncTool <spam@spam.org> 0 +0000" >> empty_tree.txt
	echo "" >> empty_tree.txt
	echo "Empty Branch sync_tool" >> empty_tree.txt
	EMPTY_COMMIT=$(echo -n '' | git hash-object -t commit -w empty_tree.txt)
	rm empty_tree.txt
	git update-ref refs/heads/sync_tool $EMPTY_COMMIT
	git checkout sync_tool
fi

scripts=$(ls /home/sebastian/repos/github-incubator/misc/git-shell-skripts/)
for e in $scripts; do
 cp /home/sebastian/repos/github-incubator/misc/git-shell-skripts/$e .
 HASH=$(git hash-object -w $e)
 git update-index --add --cacheinfo 100755 $HASH $e
done

TREE_HASH=$(git write-tree)
for e in $scripts; do
 git rm -f $e
done
git read-tree --prefix=bin $TREE_HASH
TREE_HASH=$(git write-tree)
git rm -r bin
git read-tree --prefix=tools $TREE_HASH
TREE_HASH=$(git write-tree)
git rm -r tools

COMMIT_HASH=$(echo 'import sync_util' | git commit-tree $TREE_HASH -p $EMPTY_COMMIT)
git update-ref refs/heads/sync_tool $COMMIT_HASH
git checkout master
