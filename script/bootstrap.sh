#!/bin/bash 

sudo apt-get update
sudo apt-get install apache2
sudo apt-get install mysql-server-5.6 php5-mysqlnd
sudo apt-get install php5 libapache2-mod-php5 php5-mcrypt
sudo apt-get install phpmyadmin apache2-utils
sudo ln -s /etc/phpmyadmin/apache.conf /etc/apache2/sites-enabled/001-phpmyadmin.conf
sudo service apache2 restart
sudo apt-get install git
sudo apt-get install openjdk-7-jdk
sudo apt-get install maven