echo "stop kafka"
ps -ef|grep kafka|grep -v grep| awk '{print $2}' | xargs -I {} kill -9 {}
