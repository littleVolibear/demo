1.上传、下载
    Linux上安装lrzsz
2.tree
    yum -y install tree
3.intall soft
    安装jdk的过程：
	**解压安装包
	 tar -zxvf jdk-7u45-linux-x64.tar.gz -C apps/
	**然后修改环境变量
	vi /etc/profile
	在文件最后添加
	export JAVA_HOME=/root/apps/jdk1.7.0_45
	export PATH=$PATH:$JAVA_HOME/bin


	export JAVA_HOME=/usr/local/jdk1.7.0_45
	export HADOOP_HOME=/home/hadoop/apps/hadoop-2.6.4
	export PATH=$PATH:$JAVA_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin
	保存退出

	**然后重新加载环境变量
	source /etc/profile
4.清屏  clear
5.mysql istall

* 上传MySQL-server-5.5.48-1.linux2.6.x86_64.rpm、MySQL-client-5.5.48-1.linux2.6.x86_64.rpm到Linux上
* 使用rpm命令安装MySQL-server-5.5.48-1.linux2.6.x86_64.rpm，缺少perl依赖
	rpm -ivh MySQL-server-5.5.48-1.linux2.6.x86_64.rpm 

* 安装perl依赖，上传6个perl相关的rpm包
	rpm -ivh perl-*
* 再安装MySQL-server，rpm包冲突
  	rpm -ivh MySQL-server-5.5.48-1.linux2.6.x86_64.rpm

* 卸载冲突的rpm包
	rpm -e mysql-libs-5.1.73-5.el6_6.x86_64 --nodeps
* 再安装MySQL-client和MySQL-server
	rpm -ivh MySQL-client-5.5.48-1.linux2.6.x86_64.rpm
	rpm -ivh MySQL-server-5.5.48-1.linux2.6.x86_64.rpm
*	启动MySQL服务，然后初始化MySQL
	service mysql start
	/usr/bin/mysql_secure_installation
*   测试MySQL
	mysql -u root -p
