
#!/bin/bash

apt-get -y install apache2 apache2-utils

rm -rf /var/www/html
ln -s /vagrant/webroot/public/ /var/www/html

a2enmod rewrite