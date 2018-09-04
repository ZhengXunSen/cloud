workdir=/home/spark/kafka_2.12-2.0.0/bin
topic=$1
$workdir/kafka-console-consumer.sh --bootstrap-server zxs-1:9092,zxs-2:9092,zxs-3:9092 -topic $topic --from-beginning
