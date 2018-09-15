workdir=/home/hadoop/apache-hive-3.0.0-bin/bin
serverlogdir=/home/hadoop/apache-hive-3.0.0-bin/logs/hiveserver2.log
metalogdir=/home/hadoop/apache-hive-3.0.0-bin/logs/meta.log
nohup $workdir/hive --service hiveserver2 > $serverlogdir  2>&1  &
nohup $workdir/hive --service metastore > $metalogdir  2>&1  &
