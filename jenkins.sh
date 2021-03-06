#!/usr/bin/env bash
set -e
CURRENT_DIR=$PWD
function cleanup {
  cd "$CURRENT_DIR"
  rm -r "$CURRENT_DIR/work"
}
trap cleanup EXIT
cd "$(dirname "$0")"


cfLogin() {
  if [ -z "${CF_USER:-}" ]; then
    echo "Using cached credentials in ${CF_HOME:-home directory}" >&2
  else
    CF_API="${CF_API:?CF_USER is set - CF_API environment variable also needs to be set}"
    CF_ORG="${CF_ORG:?CF_USER is set - CF_ORG environment variable also needs to be set}"
    CF_SPACE="${CF_SPACE:?CF_USER is set - CF_SPACE environment variable also needs to be set}"
    CF_PASS="${CF_PASS:?CF_USER is set - CF_PASS environment variable also needs to be set}"

    # CloudFoundry will cache credentials in ~/.cf/config.json by default.
    # Create a dedicated work area to avoid contaminating the user's credential cache
    export CF_HOME="$CURRENT_DIR/work"
    rm -rf "$CF_HOME"
    mkdir -p "$CF_HOME"
    echo "Authenticating to CloudFoundry at '$CF_API' ($CF_ORG/$CF_SPACE) as '$CF_USER'" >&2
    cf api "$CF_API"
    # Like 'cf login' but for noninteractive use
    cf auth "$CF_USER" "$CF_PASS"
    cf target -o "$CF_ORG" -s "$CF_SPACE"
  fi
}

cd prototypes/prototype-0

# run JS tests and builds via docker
./stub-rp/docker-pre-commit.sh

for APP_NAME in verify-service-provider stub-verify-hub
do
  ./$APP_NAME/pre-commit.sh
done

cfLogin

cf push -f test-manifest.yml

(
cd acceptance-tests
STUB_RP_START_URL=https://stub-rp-proto-test.cloudapps.digital ./gradlew check
)

cf push -f demo-manifest.yml

(
cd acceptance-tests
STUB_RP_START_URL=https://stub-rp-proto-demo.cloudapps.digital ./gradlew check
)

