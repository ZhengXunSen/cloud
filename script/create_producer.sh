workdir=/home/spark/kafka_2.12-2.0.0/bin
topic=$1
$workdir/kafka-console-producer.sh --broker-list zxs-1:9092 -topic $topic
