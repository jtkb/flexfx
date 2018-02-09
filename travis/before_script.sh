#!/usr/bin/env bash

echo "TRAVIS_BRANCH = ${TRAVIS_BRANCH}"
echo "TRAVIS_PULL_REQUEST = ${TRAVIS_PULL_REQUEST}"

export PROJECT_VERSION=$(mvn help:evaluate -Dexpression=project.version | grep -v ^[^0-9] | grep '^[0-9]\+\.[0-9]\+\.[0-9]\+')
echo ${PROJECT_VERSION} | grep \\-SNAPSHOT$
export IS_RELEASE="$?"
echo "IS_RELEASE = ${IS_RELEASE}"
export TRUE=1
export FALSE=0

if [ "${TRAVIS_PULL_REQUEST}" == 'false' ]; then
    export IS_PR=${FALSE}
else
    export IS_PR=${TRUE}
fi

echo "IS PR: ${IS_PR}"

## Test if merge to master that it is not still in SNAPSHOT
if [ "$TRAVIS_BRANCH" == "master" ] && [ "$TRAVIS_PULL_REQUEST" != 'false' ]; then
    echo "Project Version: ${PROJECT_VERSION}"
    echo ${PROJECT_VERSION} | grep \\-SNAPSHOT$
    if [ $? -eq 0 ]; then
        echo "The version ends with -SNAPSHOT."
        exit 1
    fi
elif [ "$TRAVIS_BRANCH" == "feature/release_1.1.0" ]; then
    echo "Project Version: ${PROJECT_VERSION}"
    echo ${PROJECT_VERSION} | grep \\-SNAPSHOT$
    if [ $? -ne 0 ]; then
        echo "The version does not end with -SNAPSHOT. Build abort."
        exit 1
    fi
fi

if [ "$TRAVIS_BRANCH" == "dev" ] && [ ${IS_RELEASE} -eq ${FALSE} ]; then
    echo "This is a SNAPSHOT build"
    mkdir ~/.m2
    cp ${TRAVIS_BUILD_DIR}/travis/settings-security.xml ~/.m2/
    cp ${TRAVIS_BUILD_DIR}/travis/travissettings.xml ~/.m2/settings.xml

    openssl aes-256-cbc -K $encrypted_259161014934_key -iv $encrypted_259161014934_iv -in ${TRAVIS_BUILD_DIR}/travis/signing.asc.enc -out ${TRAVIS_BUILD_DIR}/travis/signing.asc -d

    openssl aes-256-cbc -K $encrypted_431ce540c668_key -iv $encrypted_431ce540c668_iv -in ${TRAVIS_BUILD_DIR}/travis/encrypt-settings.xml.enc -out ${TRAVIS_BUILD_DIR}/travis/encrypt-settings.xml -d

    gpg --fast-import --no-tty ${TRAVIS_BUILD_DIR}/travis/signing.asc &> /dev/null
fi

if [ "$TRAVIS_BRANCH" == "master" ] && [ ${IS_RELEASE} -eq ${TRUE} ]; then
    echo "This is a release build"
    mkdir ~/.m2
    cp ${TRAVIS_BUILD_DIR}/travis/settings-security.xml ~/.m2/
    cp ${TRAVIS_BUILD_DIR}/travis/travissettings.xml ~/.m2/settings.xml

    openssl aes-256-cbc -K $encrypted_259161014934_key -iv $encrypted_259161014934_iv -in ${TRAVIS_BUILD_DIR}/travis/signing.asc.enc -out ${TRAVIS_BUILD_DIR}/travis/signing.asc -d

    openssl aes-256-cbc -K $encrypted_431ce540c668_key -iv $encrypted_431ce540c668_iv -in ${TRAVIS_BUILD_DIR}/travis/encrypt-settings.xml.enc -out ${TRAVIS_BUILD_DIR}/travis/encrypt-settings.xml -d

    gpg --fast-import --no-tty ${TRAVIS_BUILD_DIR}/travis/signing.asc &> /dev/null
fi

if [ "$TRAVIS_BRANCH" == "feature/release_1.1.0" ] && [ ${IS_RELEASE} -eq ${FALSE} ]; then
    echo "This is a SNAPSHOT build"
    mkdir ~/.m2
    cp ${TRAVIS_BUILD_DIR}/travis/settings-security.xml ~/.m2/
    cp ${TRAVIS_BUILD_DIR}/travis/travissettings.xml ~/.m2/settings.xml

    openssl aes-256-cbc -K $encrypted_259161014934_key -iv $encrypted_259161014934_iv -in ${TRAVIS_BUILD_DIR}/travis/signing.asc.enc -out ${TRAVIS_BUILD_DIR}/travis/signing.asc -d

    openssl aes-256-cbc -K $encrypted_431ce540c668_key -iv $encrypted_431ce540c668_iv -in ${TRAVIS_BUILD_DIR}/travis/encrypt-settings.xml.enc -out ${TRAVIS_BUILD_DIR}/travis/encrypt-settings.xml -d

    gpg --fast-import --no-tty ${TRAVIS_BUILD_DIR}/travis/signing.asc &> /dev/null
fi

# Configure Maven command line params
if [ "$TRAVIS_BRANCH" == "master" ] && [ ${IS_RELEASE} -eq ${TRUE} ] && [ ${IS_PR} -eq ${FALSE} ]; then
    echo "Building MASTER for release"
    export MVN_PHASES="clean deploy"
    export MVN_PROFILES="-Possrh,publish,integration"
elif [ "$TRAVIS_BRANCH" == "dev" ] && [ ${IS_RELEASE} -eq ${FALSE} ] && [ ${IS_PR} -eq ${FALSE} ]; then
    echo "Building DEV for SNAPSHOT"
    export MVN_PHASES="clean deploy"
    export MVN_PROFILES="-Possrh,publish,integration"
elif [ "$TRAVIS_BRANCH" == "feature/release_1.1.0" ] && [ ${IS_RELEASE} -eq ${FALSE} ] && [ ${IS_PR} -eq ${FALSE} ]; then
    echo "Building feature/release_1.1.0 for SNAPSHOT"
    export MVN_PHASES="clean deploy"
    export MVN_PROFILES="-Possrh,publish,integration"
else
    echo "Doing standard build for ${TRAVIS_BRANCH} branch."
    export MVN_PHASES="clean install"
    export MVN_PROFILES="-Pintegration,examples"
fi