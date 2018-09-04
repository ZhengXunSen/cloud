workdir=/home/spark/kafka_2.12-2.0.0/bin
topic=$1
$workdir/kafka-topics.sh --create --zookeeper zxs-1:2181,zxs-2:2181,zxs-3:2181 --replication-factor 3 --partitions 3 --topic $topic
