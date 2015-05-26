CREATE DATABASE wordpress;
CREATE USER wordpress@localhost IDENTIFIED BY 'WORDPRESS_DB_PASS';
GRANT ALL PRIVILEGES ON wordpress.* TO wordpress@localhost;
FLUSH PRIVILEGES;