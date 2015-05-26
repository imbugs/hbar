echo "mysql-server-5.5 mysql-server/root_password_again password $1" | debconf-set-selections
echo "mysql-server-5.5 mysql-server/root_password password $1" | debconf-set-selections
# apt-get -y install mysql-server php5-mysql

apt-get -y install mysql-server-5.6 php5-mysqlnd