@echo off
set KAFKA_BROKER=localhost:9092

echo Creating business topics...
kafka-topics.bat --bootstrap-server %KAFKA_BROKER% --create --topic position-instrument --partitions 3 --replication-factor 1
kafka-topics.bat --bootstrap-server %KAFKA_BROKER% --create --topic gold-smc --partitions 3 --replication-factor 1
kafka-topics.bat --bootstrap-server %KAFKA_BROKER% --create --topic output --partitions 3 --replication-factor 1

echo Creating internal topics...
kafka-topics.bat --bootstrap-server %KAFKA_BROKER% --create --topic ks3-KSTREAM-SOURCE-0000000000 --partitions 3 --replication-factor 1
kafka-topics.bat --bootstrap-server %KAFKA_BROKER% --create --topic ks3-KSTREAM-SOURCE-0000000001 --partitions 3 --replication-factor 1
kafka-topics.bat --bootstrap-server %KAFKA_BROKER% --create --topic ks3-KSTREAM-MERGE-0000000004 --partitions 3 --replication-factor 1

echo Creating lookup table topics...
kafka-topics.bat --bootstrap-server %KAFKA_BROKER% --create --topic ks3-KSTREAM-FILTER-0000000000 --partitions 3 --replication-factor 1
kafka-topics.bat --bootstrap-server %KAFKA_BROKER% --create --topic ks3-KSTREAM-FILTER-0000000001 --partitions 3 --replication-factor 1
kafka-topics.bat --bootstrap-server %KAFKA_BROKER% --create --topic ks3-KSTREAM-MAP-0000000001 --partitions 3 --replication-factor 1
kafka-topics.bat --bootstrap-server %KAFKA_BROKER% --create --topic ks3-KSTREAM-MAP-0000000002 --partitions 3 --replication-factor 1
kafka-topics.bat --bootstrap-server %KAFKA_BROKER% --create --topic ks3-KSTREAM-MAP-0000000003 --partitions 3 --replication-factor 1

echo Creating state store topics...
kafka-topics.bat --bootstrap-server %KAFKA_BROKER% --create --topic ks3-KSTREAM-STATE-0000000000 --partitions 3 --replication-factor 1
kafka-topics.bat --bootstrap-server %KAFKA_BROKER% --create --topic ks3-KSTREAM-STATE-0000000001 --partitions 3 --replication-factor 1
kafka-topics.bat --bootstrap-server %KAFKA_BROKER% --create --topic ks3-KSTREAM-STATE-0000000002 --partitions 3 --replication-factor 1

echo All topics created successfully! 