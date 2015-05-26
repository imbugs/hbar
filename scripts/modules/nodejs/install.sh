#!/bin/bash

apt-get -y install nodejs
ln -s /usr/bin/nodejs /usr/bin/node

apt-get -y install npm

npm install -g bower