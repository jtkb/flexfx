#!/usr/bin/env bash

if [ "${TRAVIS_BRANCH}" = 'dev' ] || [ "${TRAVIS_BRANCH}" = 'master' ]; then
    shred --remove ${TRAVIS_BUILD_DIR}/travis/encrypt-settings.xml
    shred --remove ${TRAVIS_BUILD_DIR}/travis/signing.asc
fi
