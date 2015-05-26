# hbar
hbar financial charting and strategy library.

## Installation

### Requirements
* [VirtualBox](https://www.virtualbox.org/)
* [Vagrant](https://www.vagrantup.com/)
```sh
git clone https://github.com/hbar-digital/hbar.git
```

### Setting Up the VM
```sh
cd crypto-strats
env MYSQL_ROOT_PASS='new_password' vagrant up
```
`vagrant up` will startup an Ubuntu 14.04 virtual machine. If this is the first time using vagrant, it will take a while to download Ubuntu. Once Ubuntu is setup, vagrant will run the `bootstrap.sh` provisioning script found in the scripts directory. This provisioning script will download all of the project dependencies. The default mysql root password and phpmyadmin password are 'vagrant'. You must set the MYSQL_ROOT_PASS environment variable to override this default. 

## Running the Server
### Running on the VM
```sh
vagrant ssh
cd /vagrant/crypto-strats-server/script
sudo ./start.sh
```

Now that all dependencies are installed and the data server is running, you can visit localhost:8080 to view the charts. You can use the same process to quickly launch an instance in the cloud.

1. `git clone`
2. `bootstrap.sh`
3. `start.sh`
4. ???
5. $$$

### Running in Eclipse

Make sure you have the [m2e](http://eclipse.org/m2e/) plugin installed. Import the crypto-strats-server project into eclipse and create a new `Maven Build` under run configurations.

#### Main tab
- Base directory: `${workspace_loc:/crypto-strats}`
- Goals: `clean install vertx:runMod`

#### JRE tab
- VM arguments:
```
-Djavax.net.ssl.trustStore=./certs/jssecacerts
-Dlog4j.configuration=file:./src/main/resources/log4j2.xml
```
