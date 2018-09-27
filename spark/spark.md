# spark安装 #

---
## **scala 安装** ##
 ***1. 下载Scala***
scala官网下载最新的Scala版本,以下以scala-2.12.6为例
> 下载地址：https://www.scala-lang.org/download/

 ***2. 上传 scala-2.12.6.tgz***
 ***3. 解压***
> `tar -xzvf scala-2.12.6.tgz`
 
 ***4. 配置环境变量***
 >  `vim /etc/profile`
 ```
    export SCALA_HOME=/app/scala-2.12.6
    export PATH=.:$PATH:$SCALA_HOME/bin:$PATH
```

***5.执行如下命令使环境变量生效***
> source /etc/profile

----------
##**jdk安装**##
***1.yum安装***

- 检查是否装有java
> `rpm -qa |grep java`,`rpm -qa |grep jdk`,`rpm -qa |grep gcj`

- 如果安装了执行如下命令删除
> `rpm -qa | grep java | xargs rpm -e --nodeps`

- 检索java列表 
> `yum list java*`

- 安装1.8.0的所有文件 
> `yum install java-1.8.0-openjdk* -y`

- 检查是否安装成功 
> `java -version`

- 配置环境变量
> 在/etc/profile加入如下
```
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.171-8.b10.el7_5.x86_64
export JRE_HOME=$JAVA_HOME/jre
export CLASSPATH=$JAVA_HOME/lib:$JRE_HOME/lib:$CLASSPATH
export PATH=.:$PATH:$JAVA_HOME/bin:$JRE_HOME/bin:$PATH
```

***2.压缩包安装***

- 下载地址
> http://www.oracle.com/technetwork/java/javase/downloads/index.html

- 上传tar包至服务器，以jdk1.8.0_144为例，执行解压
> `tar -zxvf jdk-8u144-linux-x64.tar`

- 配置环境变量
> 
1. 打开/etc/profile加入
```
export JAVA_HOME=/opt/soft/jdk1.8.0_144
export PATH=$JAVA_HOME/bin:$PATH
export CLASSPATH=.:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib/tools.jar
```
2. 执行 `source /etc/profile`

## **hadoop安装** ##
***1.ssh免密登录***

- 下载hadoop压缩包
> http://mirror.bit.edu.cn/apache/hadoop/common
找到需要下载的版本，选择镜像的链接下载，以下以hadoop-3.1.0进行说明,命令下载`wget http://mirror.bit.edu.cn/apache/hadoop/common/hadoop-3.1.1/hadoop-3.1.1.tar.gz`

- 设置机器名称并配置host,此处以三台机器作为集群搭建
> 
1. 使用hostname分别命名三台机器为zxs-1,zxs-2,zxs-3,例：`hostname zxs-1`
2. 打开/etc/hosts配置刚刚命名的主机名和ip映射
2. 使用hostname检查主机名，ping 主机名查看ip映射

- 给机器生成秘钥,以zxs-1服务器为例
> 
1. 执行命令生成空字符串的秘钥(后面要使用公钥) 
`ssh-keygen  -t   rsa   -P  ''`
2. 因为我使用的是root用户，所以秘钥保存在root用户中，使用命令 `ls    /root/.ssh/`可以查看到有两个文件 id_rsa id_rsa.pub
3. 使用同样的方法在另外两台机器上生成秘钥

- 在zxs-1上创建authorized_keys文件
> 接下来要做的事情是在3台机器的/root/.ssh/目录下都存入一个内容相同的文件，文件名称叫authorized_keys，文件内容是我们刚才为3台机器生成的公钥。为了方便，先在zxs-1上生成authorized_keys文件，然后把3台机器刚才生成的公钥加入到这个zxs-1的authorized_keys文件里，然后在将这个authorized_keys文件复制到hserver2和hserver3上面。
1. 在zxs-1的/root/.ssh/目录中生成一个名为authorized_keys的文件 
`touch  /root/.ssh/authorized_keys`
使用`ls   /root/.ssh/` 查看是否生成**authorized_keys**文件
2. 其次将zxs-1上的/root/.ssh/id_rsa.pub文件内容，zxs-2上的/root/.ssh/id_rsa.pub文件内容，zxs-3上的/root/.ssh/id_rsa.pub文件内容复制到这个**authorized_keys**文件中
3. 将**authorized_keys**复制到其他机器上
4. 在zxs-1使用`ssh zxs-2`和`ssh zxs-3`分别测试连接，同理在另外两台也执行相同操作测试

- 添加hadoop用户，生成ssh连接
> 
1. 新建用户: `adduser hadoop`
2. 设置密码: `passwd hadoop`
3. 重复上述的生成ssh密钥的步骤，ssh目录为/home/hadoop/.ssh
4. 修改ssh配置`vim /etc/ssh/sshd_config` 添加
`PubkeyAuthentication yes   
RSAAuthentication yes`
5. 重启ssh `service sshd restart`
6. 将本机id存入本机中`ssh-copy-id hadoop@zxs-1`
7. 修改权限`chmod -R 700 /home/hadoop/.ssh/
chmod 644 /home/hadoop/.ssh/authorized_keys`

- 设置环境变量
> 
1. 打开/etc/profile 
```
export HADOOP_HOME=/app/hadoop-3.1.0
export HADOOP_CONF_DIR=${HADOOP_HOME}/etc/hadoopexport HADOOP_COMMON_LIB_NATIVE_DIR=${HADOOP_HOME}/lib/native
export HADOOP_OPTS="-Djava.library.path=${HADOOP_HOME}/lib"
export PATH=.:$PATH:$SCALA_HOME/bin:${SPARK_HOME}/bin:$JAVA_HOME/bin:$JRE_HOME/bin:${HADOOP_HOME}/sbin:$PATH
```
2. 执行`source /etc/profile`市环境变量生效

- 配置hadoop
在hadoop目录下创建dataDir,/dataDir/tmp,/dataDir/hdfs,/dataDir/hdfs/data,/dataDir/hdfs/name，该目录会在以下配置文件中使用，配置位置在hadoop目录下的/etc/hadoop目录下

>`vim hdfs-site.xml`文件，输入配置

```

    <!-- 设置namenode的http通讯地址 -->
    <property>
        <name>dfs.namenode.http-address</name>
        <value>zxs-1:50070</value>
    </property>
     <!-- 设置namenode存放的路径 -->
    <property>
        <name>dfs.namenode.name.dir</name>
        <value>file:/app/hadoop-3.1.0/dataDir/hdfs/name</value>
     </property>
     <!-- 设置datanode存放的路径 -->
    <property>
        <name>dfs.datanode.data.dir</name>
        <value>file:/app/hadoop-3.1.0/dataDir/hdfs/data</value>
    </property> 
    <!-- 设置hdfs副本数量 -->
    <property>
        <name>dfs.replication</name>
        <value>2</value>
    </property>
    <!-- 设置secondarynamenode的http通讯地址 -->
    <property>
        <name>dfs.namenode.secondary.http-address</name>
        <value>zxs-2:50080</value>
    </property>
    <property>
        <name>dfs.webhdfs.enabled</name>
        <value>true</value>
    </property>
```
  
  >'vim mapred-site.xml',输入如下配置

```
    <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>
    <property>
        <name>yarn.nodemanager.auxservices.mapreduce.shuffle.class</name>
        <value>org.apache.hadoop.mapred.ShuffleHandler</value>
    </property>
    <property>
        <name>yarn.resourcemanager.address</name>
        <value>zxs-1:8032</value>
    </property>
    <property>
        <name>yarn.resourcemanager.scheduler.address</name>
        <value>zxs-1:8030</value>
    </property>
    <property>
        <name>yarn.resourcemanager.resource-tracker.address</name>
        <value>zxs-1:8031</value>
    </property>
    <property>
        <name>yarn.resourcemanager.admin.address</name>
        <value>zxs-1:8033</value>
    </property>
    <property>
        <name>yarn.resourcemanager.webapp.address</name>
        <value>zxs-1:8088</value>
    </property>
    <property>
        <name>yarn.nodemanager.resource.memory-mb</name>
        <value>768</value>
    </property>
    <!-- 通知框架MR使用YARN -->
    <property>
        <name>mapreduce.framework.name</name>
        <value>yarn</value>
    </property>
    <property>
        <name>mapreduce.jobhistory.address</name>
        <value>zxs-1:10020</value>
    </property>
    <property>
        <name>mapreduce.jobhistory.webapp.address</name>
        <value>zxs-1:19888</value>
    </property>
```

> `vim core-site.xml`,输入如下配置

```
    <!-- 指定HDFS老大（namenode）的通信地址 -->
    <property>
     <name>fs.defaultFS</name>
     <value>hdfs://zxs-1:9000</value>
    </property>
     <!-- 指定hadoop运行时产生文件的存储路径 -->
    <property>
     <name>hadoop.tmp.dir</name>
     <value>file:/app/hadoop-3.1.0/dataDir/tmp</value>
    </property>
    <property>
      <name>io.file.buffer.size</name>
      <value>131702</value>
    </property>
```


> `./bin/hdfs namenode -format`格式化:格式化一次就好,多次格式化可能导致datanode无法识别,如果想要多次格式化,需要先删除数据再格式化

----------

> 在同一目录下打开hadoop-env.sh，输入如下配置

```
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.171-8.b10.el7_5.x86_64
export HDFS_DATANODE_SECURE_USER=hadoop
export HDFS_SECONDARYNAMENODE_USER=hadoop
```
yarn-env.sh配置jdk环境变量
`export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.171-8.b10.el7_5.x86_64`

> 打开`vim workers`输入子节点主机名
删除localhost,输入
zxs-2
zxs-3

>打开sbin目录下的start-dfs.sh和stop-dfs.sh文件输入(用户均输入该目录的用户所有者，通过命令 `chown -R user /dir`改变拥有用户)
```
HDFS_DATANODE_USER=hadoop
HDFS_DATANODE_SECURE_USER=hadoop
HDFS_NAMENODE_USER=hadoop
HDFS_SECONDARYNAMENODE_USER=hadoop
```
>打开sbin目录下的start-yarn.sh和stop-yarn.sh文件输入;(用户均输入该目录的用户所有者)
```
YARN_RESOURCEMANAGER_USER=hadoop
HDFS_DATANODE_SECURE_USER=hadoop
YARN_MODEMANAGER_USER=hadoop
YARN_NODEMANAGER_USER=hadoop
```

> scp hadoop到另外两台机器

> 切换到sbin目录下执行start-all.sh启动hadoop集群，访问首页http://10.3.20.126:8088

>关闭datanode的防火墙（因为datanode连接的端口较多，直接关闭比开放来的快。防止在`$HADOOP_HOME/bin/hadoop fs -put /dir /dir2`
时出现主机连接错误。`systemctl stop firewalld.service`;禁止开机启动`systemctl disable firewalld.service` 参考：http://hadoop.apache.org/docs/current/hadoop-project-dist/hadoop-common/SecureMode.html

### windows下hadoop安装配置 ###
1. 在windows下调试程序时会出现找不到hadoop的环境变量情况，所以需要在windows下安装hadoop，设置环境变量
2. Hadoop官方下载地址：http://hadoop.apache.org/releases.html
3. 解压；本处解压至F盘，下面以此为例
4. 设置环境变量，添加HADOOP_HOME配置`F:\soft\hadoop-3.1.0`;Path配置`%HADOOP_HOME\bin%`
5. 修改F:\soft\hadoop-3.1.0/etc/hadoop/core-site.xml配置:
```
<configuration>  
    <property>  
       <name>fs.default.name</name>  
       <value>hdfs://localhost:9000</value>  
   </property>  
</configuration> 
```
6. 与上面同级目录下的mapred-site.xml（下同）
```
<configuration>  
    <property>  
       <name>mapreduce.framework.name</name>  
       <value>yarn</value>  
   </property>  
</configuration>
```
7. hdfs-site.xml
```
<configuration>  
    <!-- 这个参数设置为1，因为是单机版hadoop -->  
    <property>  
        <name>dfs.replication</name>  
        <value>1</value>  
    </property>  
    <property>   
     <name>dfs.permissions</name>   
     <value>false</value>   
  </property>  
   <property>  
       <name>dfs.namenode.name.dir</name>  
       <value>/D:/hadoop-3.0.0/data/namenode</value>  
   </property>  
   <property>  
        <name>fs.checkpoint.dir</name>  
        <value>/D:/hadoop-3.0.0/data/snn</value>  
    </property>  
    <property>  
        <name>fs.checkpoint.edits.dir</name>  
        <value>/D:/hadoop-3.0.0/data/snn</value>  
    </property>  
       <property>  
       <name>dfs.datanode.data.dir</name>  
       <value>/D:/hadoop-3.0.0/data/datanode</value>  
   </property>  
</configuration>  
```
8. yarn-site.xml
```
<configuration>  
<!-- Site specific YARN configuration properties -->  
    <property>  
        <name>yarn.nodemanager.aux-services</name>  
        <value>mapreduce_shuffle</value>  
   </property>  
   <property>  
        <name>yarn.nodemanager.auxservices.mapreduce.shuffle.class</name>    
        <value>org.apache.hadoop.mapred.ShuffleHandler</value>  
   </property>  
</configuration>  
```
9. F:\soft\hadoop-3.1.0/目录下创建data目录，作为数据存储路径：并在该目录下创建文件夹datanode和namenode
10. 修改==D:/hadoop-3.0.0/etc/hadoop/hadoop-env.cmd==配置，找到"set JAVA_HOME=%JAVA_HOME%"替换为"set JAVA_HOME===你windows下jdk的JAVA_HOME环境变量=="
11. bin目录替换，至https://github.com/steveloughran/winutils下载解压，找到对应的版本后完整替换bin目录即可
12. 找到hadoop安装的bin目录，执行格式化
> `hdfs namenode -format`

## **安装spark**

- 下载spark压缩包并解压
> 
1. 访问 http://mirrors.hust.edu.cn/apache/spark/
找到最新版本的spark下载，以下以spark-2.3.1-bin-hadoop2.7为例`wget http://mirrors.hust.edu.cn/apache/spark/spark-2.1.3/spark-2.1.3-bin-hadoop2.7.tgz`
2. 上传至服务器，解压`tar -zxvf spark-2.3.1-bin-hadoop2.7.tgz`

- 添加环境变量
> 
1. 打开/etc/profile 输入以下环境变量
```
export PATH=.:$PATH:$SCALA_HOME/bin:${SPARK_HOME}/bin:$JAVA_HOME/bin:$JRE_HOME/bin:${HADOOP_HOME}/sbin:$PATH
export  SPARK_HOME=/app/spark-2.3.1-bin-hadoop2.7
```
2. 执行 `source /etc/profile` 使环境变量生效

- 配置conf目录下的文件
> 
1. `cd /spark安装目录/conf`
2. 以spark为我们创建好的模板创建一个spark-env.h文件
`cp    spark-env.sh.template   spark-env.sh`
3. 编辑spark-env.h文件，在里面加入配置(环境变量由实际机器安装目录决定)
```
export SCALA_HOME=/app/scala-2.12.6
export  SPARK_HOME=/app/spark-2.3.1-bin-hadoop2.7
export JAVA_HOME=/usr/lib/jvm/java-1.8.0-openjdk-1.8.0.171-8.b10.el7_5.x86_64
export JRE_HOME=$JAVA_HOME/jre
export HADOOP_HOME=/app/hadoop-3.1.0
export HADOOP_CONF_DIR=${HADOOP_HOME}/etc/hadoop
export SPARK_MASTER_IP=zxs-1
export SPARK_EXECUTOR_MEMORY=512M
```
- 启动spark集群
>
1. `cd /spark安装目录/sbin`

2. 添加端口映射
```
#查看开放的端口
firewall-cmd --list-ports
#添加8080端口映射
firewall-cmd --zone=public --add-port=8080/tcp --permanent 
#添加7077端口映射(master默认端口7077，需要暴露给从机连接)
firewall-cmd --zone=public --add-port=7077/tcp --permanent 
#重启防火墙生效
firewall-cmd --reload 
```
3. 执行启动脚本 `./start-all.sh`

4. 访问 http://ip:8080/

5. 从节点挂掉后重新启动，sbin目录下执行`start-slave.sh 10.3.20.126:7077`（此url为master节点）

### windows安装spark
1. 将下载的spark包解压

2.配置环境变量生效

3.cmd打开输入spark-shell能进入spark说明安装成功

4.spark安装需依赖java和scala的环境

## zookeeper安装
>
1. 下载
`wget https://archive.apache.org/dist/zookeeper/zookeeper-3.4.9/zookeeper-3.4.9.tar.gz`
2. 解压缩`tar -zxvf zookeeper-3.4.9.tar.gz`
3. 进入到 /zookeeper-3.4.9/conf 目录中，复制 zoo_sample.cfg 文件的并命名为为 zoo.cfg：`cp zoo_sample.cfg zoo.cfg`
 4. 打开 zoo.cfg 文件并修改其内容为如下：
>  
```
# The number of milliseconds of each tick

# zookeeper 定义的基准时间间隔，单位：毫秒
tickTime=2000

# The number of ticks that the initial 
# synchronization phase can take
initLimit=10
# The number of ticks that can pass between 
# sending a request and getting an acknowledgement
syncLimit=5
# the directory where the snapshot is stored.
# do not use /tmp for storage, /tmp here is just 
# example sakes.
# dataDir=/tmp/zookeeper

# 数据文件夹
dataDir=/usr/local/services/zookeeper/zookeeper-3.4.9/data

# 日志文件夹
dataLogDir=/usr/local/services/zookeeper/zookeeper-3.4.9/logs

# the port at which the clients will connect
# 客户端访问 zookeeper 的端口号
clientPort=2181

# the maximum number of client connections.
# increase this if you need to handle more clients
#maxClientCnxns=60
#
# Be sure to read the maintenance section of the 
# administrator guide before turning on autopurge.
#
# http://zookeeper.apache.org/doc/current/zookeeperAdmin.html#sc_maintenance
#
# The number of snapshots to retain in dataDir
#autopurge.snapRetainCount=3
# Purge task interval in hours
# Set to "0" to disable auto purge feature
#autopurge.purgeInterval=1
#3888是选举端口
server.1=zxs-1:2181:3888
server.2=zxs-2:2181:3888
server.3=zxs-3:2181:3888
```
5.配置环境变量`vim /etc/profile`
```
export ZOOKEEPER_HOME=/usr/local/services/zookeeper/zookeeper-3.4.9/
export PATH=$ZOOKEEPER_HOME/bin:$PATH
```
6.在$zookeerper/data/目录下新建myid文件，根据具体机器ip对应的server.*填上相应的编号。（注：zookeeper集群启动后会选举出leader，leader对的clientPort配置不能与server相同，否则会出现端口占用异常）

7.使用开放端口命令开放相应端口以便集群相互连通
```
firewall-cmd --zone=public --add-port=3888/tcp --permanent
```
8. 修改zkServer.sh中的_ZOO_DAEMON_OUT为指定文件，防止后台启动重定向文件到处生成。也可以配置相应的log目录
## 安装kafka
1.下载`wget https://mirrors.cnnic.cn/apache/kafka/2.0.0/kafka_2.12-2.0.0.tgz`

2.解压`tar -zxvf kafka_2.12-2.0.0.tgz`

3.配置环境变量：
```
export KAFKA_HOME=/home/spark/kafka_2.12-2.0.0
export PATH=$PATH:$KAFKA_HOME/bin
```

4.修改配置$KAFKA_HOME/config目录下的server.properties
```
#id唯一
broker.id=181
delete.topic.enable=true
listeners = PLAINTEXT://zxs-1:9092
log.dirs=/home/spark/kafka_2.12-2.0.0/log/
#zookeeper集群连接
zookeeper.connect=zxs-1:2181,zxs-2:2181,zxs-3:2181
```
5.后台启动：`setsid $KAFKA_HOME/bin/kafka-server-start.sh $KAFKA_HOME/config/server.properties`
## 安装hive
1.下载hive:`wget http://mirror.bit.edu.cn/apache/hive/hive-3.0.0/apache-hive-3.0.0-bin.tar.gz`

2.解压：tar -zxvf apache-hive-3.0.0-bin.tar.gz

3.配置环境变量：
```
export HIVE_HOME=/home/hadoop/apache-hive-3.1.0-bin
export PATH=$PATH:$HIVE_HOME/bin
```
4.修改配置文件：
> 在conf目录下`vim hive-site.xml`,输入
```
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<configuration>
<!--<!--hive元数据服务的地址,以下hive远程连接配置适用于本地spark连接远程hive-->
  <property>
    <name>hive.metastore.uris</name>
    <value>thrift://zxs-1:9083</value>
  </property>
  <property>
    <name>hive.server2.thrift.port</name>
    <value>10000</value>
  </property>-->
    <property>
       <name>javax.jdo.option.ConnectionURL</name>
       <value>jdbc:mysql://zxs-1:3306/hive?&amp;createDatabaseIfNotExist=true&amp;characterEncoding=UTF-8&amp;useSSL=false</value>
    </property>
    <property>
        <name>javax.jdo.option.ConnectionUserName</name>
        <value>zxs</value>
    </property>
    <property>
        <name>javax.jdo.option.ConnectionPassword</name>
        <value>jfz123456</value>
    </property>
    <property>
        <name>javax.jdo.option.ConnectionDriverName</name>
        <value>com.mysql.jdbc.Driver</value>
    </property>
    <property>
        <name>datanucleus.schema.autoCreateAll</name>
        <value>true</value> </property>
    <property>
        <name>hive.metastore.schema.verification</name>
        <value>false</value>
     </property>
    <property>
      <name>hive.execution.engine</name>
      <value>spark</value>
    </property>
    
    <property>
      <name>hive.enable.spark.execution.engine</name>
      <value>true</value>
    </property>
    
    <property>
      <name>spark.home</name>
      <value>/app/spark-2.3.1-bin-hadoop2.7</value>
    </property>
    <property>
      <name>spark.master</name>
      <value>spark://zxs-1:7077</value>
    </property>
    <property>
      <name>spark.enentLog.enabled</name>
      <value>true</value>
    </property>
    <property>
      <name>spark.enentLog.dir</name>
      <value>/home/hadoop/apache-hive-3.1.0-bin/logs/spark-log</value>
    </property>
    <property>
      <name>spark.serializer</name>
      <value>org.apache.spark.serializer.KryoSerializer</value>
    </property>
    <property>
      <name>spark.executor.memeory</name>
      <value>1g</value>
    </property>
    <property>
      <name>spark.driver.memeory</name>
      <value>1g</value>
    </property>
    <property>
      <name>spark.executor.extraJavaOptions</name>
      <value>-XX:+PrintGCDetails -Dkey=value -Dnumbers="one two three"</value>
    </property>
</configuration>
```
> 配置hive-env.sh：`cp hive-env.sh.template hive-env.sh`;`vim hive-env.sh`
```$xslt
HADOOP_HOME=/opt/hadoop-2.7.5
export HIVE_CONF_DIR=/home/hadoop/apache-hive-3.0.0-bin
```

5.将mysql连接的jar包拷到$HIVE_HOME/lib目录下

6.将上述hive-site.xml拷贝到$SPARK_HOME/conf目录，将mysql连接的jar包拷贝到lib目录下

7.修改hadoop中hdfs-site.xml文件配置，使之不限用户操作
```
   <property>
     <name>dfs.permissions.enabled</name>
    <value>false</value>
   </property>
```

8.关闭hadoop的安全模式：`hadoop dfsadmin -safemode leave`

## 安装kafka-manager
1.`git clone https://github.com/yahoo/kafka-manager.git`下载kafka-manager源码

2.通过 cd ~进入当前用户目录，然后通过命令mkdir .sbt创建.sbt目录，进入创建的该目录，使用vi创建repositories文件，编辑内容如下：
```
[repositories]
local
aliyun: http://maven.aliyun.com/nexus/content/groups/public
typesafe: http://repo.typesafe.com/typesafe/ivy-releases/, [organization]/[module]/(scala_[scalaVersion]/)(sbt_[sbtVersion]/)[revision]/[type]s/[artifact](-[classifier]).[ext], bootOnly
```
3.然后进入解压后的 Kafka-manager 目录，执行下面的命令：`./sbt clean dist`

4.命令执行完成后，在 target/universal 目录中会生产一个zip压缩包kafka-manager-1.3.3.7.zip。将压缩包拷贝到要部署的目录下解压。

5.在解压后的conf目录中打开 application.conf文件，修改其中的配置信息，最主要的内容为配置kafka的zookeeper连接信息：
```
kafka-manager.zkhosts="localhost:2181"
```

6.启动：`bin/kafka-manager -Dconfig.file=/path/to/application.conf -Dhttp.port=8080`