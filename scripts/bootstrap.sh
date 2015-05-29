#!/bin/bash -x


cd /vagrant/scripts

apt-get update


### LAMP ###

./modules/apache2/install.sh

./modules/mysql/install.sh $1

./modules/php5/install.sh

./modules/phpmyadmin/install.sh $1



### VERSION CONTROL ###

./modules/git/install.sh



### JAVA ###

./modules/openjdk7/install.sh

./modules/maven/install.sh



### LARAVEL ###

# ./modules/composer/install.sh

# ./modules/laravel/install.sh



### NODE.JS / NPM / BOWER ###

./modules/nodejs/install.sh



service apache2 restart