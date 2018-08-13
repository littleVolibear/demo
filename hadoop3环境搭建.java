1. 环境
主机名　　ip　　角色

hdp01　　192.168.184.61　　ResourceManager/NameNode/SecondaryNameNode

hdp02　　192.168.184.62　　NodeManager/DataNode

hdp03　　192.168.184.63　　NodeManager/DataNode

(1个namenode 2个datanode)

2.准备工作
windows10(物理机系统)
VMware12 workstation（虚拟机软件）
centos7.0(虚拟机系统)
hadoop3.0.0
jdk1.8
SecureCRT
3.配置IP/hostname及SSH免密码登录/hosts
3.1 vi /etc/sysconfig/network-scripts/ifcfg-eth0(删掉UUID  HWADDR)

rm -rf 　/etc/udev/rules.d/70-persistent-net.rules(删掉eth0,将eth1改为eth0)

---修改虚拟网卡，删掉旧的

service network restart(重启网关)

3.2 vi /etc/sysconfig/network

HOSTNAME = hdp-server01(192.168.184.61)

HOSTNAME = hdp-server02(192.168.184.62)

HOSTNAME = hdp-server03(192.168.184.63)

3.3 ssh-keygen 

ssh-copy-id hdp01

ssh-copy-id hdp02

ssh-copy-id hdp03

3.4 vi /etc/hosts

192.168.184.61 master

192.168.184.62 node1

192.168.184.63 node2

4.关闭防火墙
service iptables stop(关闭防火墙)
chkconfig iptables --list(检查防火墙启动状态)	
chkconfig iptables off(关闭防火墙启动)
chkconfig iptables status(查看防火墙状态)

5.安装JDK
vi /etc/profile

export JAVA_HOME=/usr/local/jdk1.8.0_11

6.配置hadoop
6.1 hadoop-env.sh

export JAVA_HOME=/usr/local/jdk1.8.0_11
export HDFS_NAMENODE_USER=root
export HDFS_DATANODE_USER=root
export HDFS_JOURNALNODE_USER=root
export YARN_RESOURCEMANAGER_USER=root
export YARN_NODEMANAGER_USER=root
export HDFS_SECONDARYNAMENODE_USER=root

6.2 core-site.xml

<configuration>
<property>
<name>fs.defaultFS</name>
<value>hdfs://hdp01:9000</value>
</property>

<property>
<name>hadoop.tmp.dir</name>
<value>/usr/local/hadoop/tmp</value>
</property>
</configuration>

6.3 hdfs-site.xml

<configuration>
<property>
<name>dfs.replication</name>
<value>2</value>
</property>
<property>
<name>dfs.namenode.name.dir</name>
<value>/usr/local/hadoop/hdfs/name</value>
</property>
<property>
<name>dfs.datanode.data.dir</name>
<value>/usr/local/hadoop/hdfs/data</value>
</property>
<property>
<name>dfs.namenode.secondary.http-address</name>
<value>hdp01:9001</value>
</property>
<property>
<name>dfs.http.address</name>
<value>0.0.0.0:50070</value>
</property>
</configuration>

6.4 mapred-site.xml

<configuration>
<property>
<name>mapred.job.tracker.http.address</name>
<value>0.0.0.0:50030</value>
</property>
<property>
<name>mapred.task.tracker.http.address</name>
<value>0.0.0.0:50060</value>
</property>

<property>
<name>mapreduce.framework.name</name>
<value>yarn</value>
</property>

<property>
<name>mapreduce.application.classpath</name>
<value>
/usr/local/hadoop/etc/hadoop,
/usr/local/hadoop/share/hadoop/common/*,
/local/hadoop/share/hadoop/common/lib/*,
/usr/local/hadoop/share/hadoop/hdfs/*,
/usr/local/hadoop/share/hadoop/hdfs/lib/*,
/usr/local/hadoop/share/hadoop/mapreduce/*,
/usr/local/hadoop/share/hadoop/mapreduce/lib/*,
/usr/local/hadoop/share/hadoop/yarn/*,
/usr/local/hadoop/share/hadoop/yarn/lib/*
</value>
</property>
</configuration>

6.5 workers

hdp02
hdp03

6.6 yarn-site.xml

<configuration>

<!-- Site specific YARN configuration properties -->

<property>
<name>yarn.nodemanager.aux-services</name>
<value>mapreduce_shuffle</value>
</property>
<property>
<name>yarn.resourcemanager.address</name>
<value>hdp01:8032</value>
</property>
<property>
<name>yarn.resourcemanager.scheduler.address</name>
<value>hdp01:8030</value>
</property>
<property>
<name>yarn.resourcemanager.resource-tracker.address</name>
<value>hdp01:8031</value>
</property>
<property>
<name>yarn.resourcemanager.admin.address</name>
<value>hdp01:8033</value>
</property>
<property>
<name>yarn.resourcemanager.webapp.address</name>
<value>hdp01:8088</value>
</property>

</configuration>

6.7 配置环境变量

vi /etc/profile

export JAVA_HOME=/usr/local/jdk1.8.0_11
export HADOOP_HOME=/usr/local/hadoop

export PATH=$PATH:$JAVA_HOME/bin

export PATH=$PATH:$HADOOP_HOME/bin

export PATH=$PATH:$HADOOP_HOME/sbin

 

7.启动hadoop守护进程及检查进程启动情况
格式化：

hadoop namenode -format

start-dfs.sh

start-yarn.sh

ui:http://master:50070

yarn:http://master:8088

正常启动节点情况：

8598 ResourceManager
8343 SecondaryNameNode
8077 NameNode

5654 Jps
4759 DataNode
4877 NodeManager

4503 Jps
3578 DataNode
3695 NodeManager

小case 验证：

hadoop jar /usr/local/hadoop/share/hadoop/mapreduce/hadoop-mapreduce-examples-3.0.3.jar wordcount /data/wordcount /output/

时间不同步:date -s "2018-08-13 17:05:08"