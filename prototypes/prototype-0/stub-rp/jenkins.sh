#!/usr/bin/env bash
set -e
CURRENT_DIR=$PWD
function cleanup {
  cd "$CURRENT_DIR"
}
trap cleanup EXIT
cd "$(dirname "$0")"
cd passport-verify
yarn
cd ..
yarn
./pre-commit.sh
