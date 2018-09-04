workdir=/home/spark/kafka_2.12-2.0.0/bin
confdir=/home/spark/kafka_2.12-2.0.0/config
outdir=/home/spark/kafka_2.12-2.0.0/logs/kafka.out
echo "stop kafka"
ps -ef|grep kafka|grep -v grep| awk '{print $2}' | xargs -I {} kill -9 {}
nohup $workdir/kafka-server-start.sh $confdir/server.properties > $outdir  2>&1  &
