To successfully run an application:

1. Run sbt compile
2. If target/scala2-./src_managed/main marked as Sources Root then unmark this directory
3. Download docker image: https://hub.docker.com/r/johnnypark/kafka-zookeeper/
4. Run docker image as: docker run -p 2181:2181 -p 9092:9092 -e ADVERTISED_HOST=127.0.0.1  -e NUM_PARTITIONS=10 johnnypark/kafka-zookeeper
5. Run application as usual