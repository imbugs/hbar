#!/bin/bash 

sudo apt-get update
sudo apt-get -y install apache2
sudo ln -s /vagrant/webroot/public /var/www/html
sudo apt-get -y install mysql-server-5.6 php5-mysqlnd
sudo apt-get -y install php5 libapache2-mod-php5 php5-mcrypt
sudo apt-get -y install phpmyadmin apache2-utils
sudo ln -s /etc/phpmyadmin/apache.conf /etc/apache2/sites-enabled/001-phpmyadmin.conf
sudo service apache2 restart
sudo apt-get -y install git
sudo apt-get -y install openjdk-7-jdk
sudo apt-get -y install maven