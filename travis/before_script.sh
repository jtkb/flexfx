#!/usr/bin/env bash

if [ "${TRAVIS_BRANCH}" = 'dev' ] || [ "${TRAVIS_BRANCH}" = 'master' ]; then
    mkdir ~/.m2
    cp ${TRAVIS_BUILD_DIR}/travis/settings-security.xml ~/.m2/

    openssl aes-256-cbc -K $encrypted_259161014934_key -iv $encrypted_259161014934_iv -in ${TRAVIS_BUILD_DIR}/travis/signing.asc.enc -out ${TRAVIS_BUILD_DIR}/travis/signing.asc -d

    openssl aes-256-cbc -K $encrypted_431ce540c668_key -iv $encrypted_431ce540c668_iv -in ${TRAVIS_BUILD_DIR}/travis/encrypt-settings.xml.enc -out ${TRAVIS_BUILD_DIR}/travis/encrypt-settings.xml -d


    gpg --fast-import --no-tty ${TRAVIS_BUILD_DIR}/travis/codesigning.asc &> /dev/null

fi
