#!/usr/bin/env bash

if [ "${TRAVIS_BRANCH}" = 'dev' ] || [ "${TRAVIS_BRANCH}" = 'master' ]; then
    mvn clean deploy -Possrh,publish,integration --settings travis/travissettings.xml
else
    mvn clean install -Pintegration,examples
fi