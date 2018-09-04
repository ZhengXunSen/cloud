echo "stop zookeeper"
ps -ef|grep zookeeper|grep -v grep|awk '{print $2}'| xargs -I {} kill -9 {}
