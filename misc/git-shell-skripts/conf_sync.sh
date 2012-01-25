#! /bin/bash
#Script with implements :"configure sync with 
#an foreign party"

declare -i error_found=0

function global_usage_info() {
  echo "Usage $0 -self --> to set own name"
  echo "Usage $0 -other --> to setup other syncrepo"
  echo "Usage $0 -key --> to setup a gpg-key"
  echo "Usage $0 -check -> to check for minimal configuration"
  echo "Usage $0 --help --> to display this message"
  exit
}

function self_usage_info() {
  echo "Usage $0 -self own_name"
  exit
}

function other_usage_info() {
  echo "Usage $0 -other other_name abs_bundle_in_out_dir"
  exit
}

function key_usage_info() {
  echo "Usage $0 -key other_name keyname" 
  exit
}

function crypt_usage_info() {
  echo "Usage $0 -crypt other_name email" 
  exit
}

function single_check() {
  if !(grep -q "$2=" - <<< $1); then
    echo [$3] configuration does not contain $2
    if [ "$3" = 'error' ]; then
      error_found=$error_found+1
    fi
  fi
}

function check() {
  echo checking configuration for $1
  conf=$(git config -l | grep sync)
  single_check "$conf" sync.self.name error
  single_check "$conf" sync.$1.in error
  single_check "$conf" sync.$1.out error
  single_check "$conf" sync.$1.bundlein error
  single_check "$conf" sync.$1.bundleout error
  single_check "$conf" sync.$1.signkey "warning: no signature-key given"
  single_check "$conf" sync.$1.email "warning: no encryption email given"
}


function checkall() {
  conf=$(git config -l | grep sync | grep -v sync.self.name | cut -d "." -f 2 | sort -u)
  confs=''
  for conf_name in $conf; do confs=$confs,$conf_name; done
  echo "checking the following configurations (${confs:1})"
  for conf_name in $conf; do check $conf_name; done
}


if [ $# -lt 1 ]; then
  git config -l | grep sync
  exit
fi

case "$1" in
-self )
  if [ $# -ne 2 ]; then
    self_usage_info
  fi
  git config sync.self.name $2
  ;;
-other )
  if [ $# -ne 3 ]; then
    other_usage_info
  fi
  git config sync.$2.in $2_in
  git config sync.$2.out $2_out
  git config sync.$2.bundlein $3/in
  git config sync.$2.bundleout $3/out
  ;;
-key )
  if [ $# -ne 3 ]; then
    key_usage_info
  fi
  git config sync.$2.signkey $3
  ;;
-crypt )
  if [ $# -ne 3 ]; then
    crypt_usage_info
  fi
  git config sync.$2.email $3
  ;;
-check )
  if [ $# -eq 2 ]; then
    check $2
  else
    checkall
  fi
  if [ $error_found -gt 0 ]; then
    echo $error_found errors found in configuration. Please Check
    exit 1
  fi
  ;;
--help )
  global_usage_info
  ;;
* )
  global_usage_info
  ;;
esac
