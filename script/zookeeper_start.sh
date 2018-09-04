dir=/home/spark/zookeeper-3.4.9/bin
echo "stop zookeeper"
ps -ef|grep zookeeper|grep -v grep|awk '{print $2}'| xargs -I {} kill -9 {}
echo "starting"
$dir/zkServer.sh start
