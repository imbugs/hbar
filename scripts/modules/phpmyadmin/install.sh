#!/bin/bash

echo "phpmyadmin phpmyadmin/reconfigure-webserver multiselect apache2" | debconf-set-selections
echo "phpmyadmin phpmyadmin/dbconfig-install boolean true" | debconf-set-selections
echo "phpmyadmin phpmyadmin/mysql/admin-user string root" | debconf-set-selections
echo "phpmyadmin phpmyadmin/mysql/admin-pass password $1" | debconf-set-selections
echo "phpmyadmin phpmyadmin/mysql/app-pass password $1" | debconf-set-selections
echo "phpmyadmin phpmyadmin/app-password-confirm password $1" | debconf-set-selections

apt-get -y install phpmyadmin apache2-utils
ln -s /etc/phpmyadmin/apache.conf /etc/apache2/sites-enabled/001-phpmyadmin.conf