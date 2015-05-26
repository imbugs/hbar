Vagrant.configure("2") do |config|

    config.vm.box = "ubuntu/trusty64"

    config.vm.provision :shell, path: "scripts/bootstrap.sh", args:ENV['MYSQL_ROOT_PASS']||'vagrant'

	config.vm.provider "virtualbox" do |v|
		v.memory = 2048
		v.cpus = 2
	end

    config.vm.network :forwarded_port, guest: 80, host: 8080

    config.vm.synced_folder "webroot/", "/vagrant/webroot/", owner: "www-data", group: "www-data", :nfs => false

end