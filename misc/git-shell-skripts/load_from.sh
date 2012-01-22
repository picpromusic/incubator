git checkout `git config --get sync.$1.in`
for f in `git config --get sync.$1.bundleIn`/*.bundle ; do git pull "$f"; done
git checkout master
