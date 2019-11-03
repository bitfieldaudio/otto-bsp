################################################################################
#
# Vagrantfile
#
################################################################################

### Change here for more memory/cores ###
VM_MEMORY=8192
VM_CORES=8

Vagrant.configure('2') do |config|

	required_plugins = %w( vagrant-vbguest vagrant-disksize )
	required_plugins.each do |plugin|
	  system "vagrant plugin install #{plugin}" unless Vagrant.has_plugin? plugin
	end		

	config.vm.box = 'ubuntu/bionic64'
	config.disksize.size = '64GB'

	config.vm.provider :virtualbox do |v, override|
		v.memory = VM_MEMORY
		v.cpus = VM_CORES
	end

	config.vm.provision 'shell' do |s|
		s.inline = 'echo Setting up machine name'

		config.vm.provider :virtualbox do |v, override|
			v.name = "otto-bsp"
		end
	end

	# Check our system locale -- make sure it is set to UTF-8
	# This also means we need to run 'dpkg-reconfigure' to avoid "unable to re-open stdin" errors (see http://serverfault.com/a/500778)
	# For now, we have a hardcoded locale of "en_US.UTF-8"
	locale = "en_US.UTF-8"

	config.vm.provision 'shell', privileged: true, inline:
		"sed -i 's|deb http://us.archive.ubuntu.com/ubuntu/|deb mirror://mirrors.ubuntu.com/mirrors.txt|g' /etc/apt/sources.list
		fallocate -l 4G /swapfile && chmod 0600 /swapfile && mkswap /swapfile && swapon /swapfile && echo '/swapfile none swap sw 0 0' >> /etc/fstab
		echo vm.swappiness = 10 >> /etc/sysctl.conf && echo vm.vfs_cache_pressure 
		dpkg --add-architecture i386
		apt-get -q update
		apt-get purge -q -y snapd lxcfs lxd ubuntu-core-launcher snap-confine
		apt-get -q -y install gawk wget git-core diffstat unzip texinfo gcc-multilib \
			build-essential chrpath socat libsdl1.2-dev xterm device-tree-compiler python repo
		apt-get -q -y autoremove
		apt-get -q -y clean
		echo 'Setting locale to UTF-8 (#{locale})...' && locale | grep 'LANG=#{locale}' > /dev/null || update-locale --reset LANG=#{locale} && dpkg-reconfigure -f noninteractive locales"

	config.vm.provision 'shell', privileged: false, inline:
		"echo 'Downloading OTTO BSP'
		git clone --recurse-submodules -b warrior https://github.com/OTTO-project/otto-bsp.git
		"

end