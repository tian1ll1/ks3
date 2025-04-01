#!/bin/bash

# Kafka broker 地址
KAFKA_BROKER="localhost:9092"

# 创建业务 topic
echo "Creating business topics..."
kafka-topics.sh --bootstrap-server $KAFKA_BROKER --create --topic position-instrument --partitions 3 --replication-factor 1
kafka-topics.sh --bootstrap-server $KAFKA_BROKER --create --topic gold-smc --partitions 3 --replication-factor 1
kafka-topics.sh --bootstrap-server $KAFKA_BROKER --create --topic output --partitions 3 --replication-factor 1

# 创建内部 topic
echo "Creating internal topics..."
kafka-topics.sh --bootstrap-server $KAFKA_BROKER --create --topic ks3-KSTREAM-SOURCE-0000000000 --partitions 3 --replication-factor 1
kafka-topics.sh --bootstrap-server $KAFKA_BROKER --create --topic ks3-KSTREAM-SOURCE-0000000001 --partitions 3 --replication-factor 1
kafka-topics.sh --bootstrap-server $KAFKA_BROKER --create --topic ks3-KSTREAM-MERGE-0000000004 --partitions 3 --replication-factor 1

# 创建查找表相关的 topic
echo "Creating lookup table topics..."
kafka-topics.sh --bootstrap-server $KAFKA_BROKER --create --topic ks3-KSTREAM-FILTER-0000000000 --partitions 3 --replication-factor 1
kafka-topics.sh --bootstrap-server $KAFKA_BROKER --create --topic ks3-KSTREAM-FILTER-0000000001 --partitions 3 --replication-factor 1
kafka-topics.sh --bootstrap-server $KAFKA_BROKER --create --topic ks3-KSTREAM-MAP-0000000001 --partitions 3 --replication-factor 1
kafka-topics.sh --bootstrap-server $KAFKA_BROKER --create --topic ks3-KSTREAM-MAP-0000000002 --partitions 3 --replication-factor 1
kafka-topics.sh --bootstrap-server $KAFKA_BROKER --create --topic ks3-KSTREAM-MAP-0000000003 --partitions 3 --replication-factor 1

# 创建状态存储 topic
echo "Creating state store topics..."
kafka-topics.sh --bootstrap-server $KAFKA_BROKER --create --topic ks3-KSTREAM-STATE-0000000000 --partitions 3 --replication-factor 1
kafka-topics.sh --bootstrap-server $KAFKA_BROKER --create --topic ks3-KSTREAM-STATE-0000000001 --partitions 3 --replication-factor 1
kafka-topics.sh --bootstrap-server $KAFKA_BROKER --create --topic ks3-KSTREAM-STATE-0000000002 --partitions 3 --replication-factor 1

echo "All topics created successfully!" 