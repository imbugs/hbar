#!/bin/bash

WORDPRESS_DB_NAME="wordpress"
WORDPRESS_DB_USER="wordpress"
WORDPRESS_DB_PASS=$MYSQL_ROOT_PASS
WORDPRESS_VERSION="wordpress-4.0.1-en_CA.tar.gz"
DIR=$(dirname $(readlink -f "$0"))

sudo apt-get -y install php5-gd libssh2-php

sed s/WORDPRESS_DB_PASS/$WORDPRESS_DB_PASS/ < $DIR/db-wordpress.sql | mysql -u root -p$WORDPRESS_DB_PASS

wget https://en-ca.wordpress.org/$WORDPRESS_VERSION
tar xzvf wordpress-4.0.1-en_CA.tar.gz

cd wordpress

cp wp-config-sample.php wp-config.php

sed -i s/database_name_here/$WORDPRESS_DB_NAME/ wp-config.php
sed -i s/username_here/$WORDPRESS_DB_USER/ wp-config.php
sed -i s/password_here/$WORDPRESS_DB_PASS/ wp-config.php

cd ..

rsync -avP wordpress/ /vagrant/webroot/

rm $WORDPRESS_VERSION
rm -rf wordpress