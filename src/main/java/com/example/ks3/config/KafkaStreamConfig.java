package com.example.ks3.config;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.support.serializer.JsonSerde;

import com.example.ks3.model.PositionInstrument;
import com.example.ks3.model.Contract;
import com.example.ks3.model.Product;
import com.example.ks3.model.GoldSmcMessage;

@Configuration
@EnableKafkaStreams
public class KafkaStreamConfig {

    @Value("${kafka.topic.position-instrument}")
    private String positionInstrumentTopic;

    @Value("${kafka.topic.gold-smc}")
    private String goldSmcTopic;

    @Value("${kafka.topic.output}")
    private String outputTopic;

    @Bean
    public KStream<String, PositionInstrument> kStream(StreamsBuilder streamsBuilder) {
        // 创建序列化器
        JsonSerde<PositionInstrument> positionSerde = new JsonSerde<>(PositionInstrument.class);
        JsonSerde<GoldSmcMessage> goldSmcSerde = new JsonSerde<>(GoldSmcMessage.class);
        JsonSerde<Product> productSerde = new JsonSerde<>(Product.class);
        JsonSerde<Contract> contractSerde = new JsonSerde<>(Contract.class);

        // 创建 PositionInstrument 流
        KStream<String, PositionInstrument> positionStream = streamsBuilder
            .stream(positionInstrumentTopic, 
                org.apache.kafka.streams.kstream.Consumed.with(Serdes.String(), positionSerde));

        // 创建 GoldSmcData 表
        KTable<String, GoldSmcMessage> goldSmcTable = streamsBuilder
            .table(goldSmcTopic,
                org.apache.kafka.streams.kstream.Consumed.with(Serdes.String(), goldSmcSerde))
            .filter((key, value) -> value != null && value.isValid());

        // 创建产品表（只包含 product 类型的数据）
        KTable<String, Product> productTable = goldSmcTable
            .filter((key, value) -> value != null && value.isProduct())
            .mapValues(value -> value.asProduct())
            .filter((key, value) -> value != null);

        // 创建合约表（只包含 contract 类型的数据）
        KTable<String, Contract> contractTable = goldSmcTable
            .filter((key, value) -> value != null && value.isContract())
            .mapValues(value -> value.asContract())
            .filter((key, value) -> value != null);

        // 创建期货合约查找表
        KTable<String, Contract> futureContractTable = contractTable
            .filter((key, value) -> value != null && "fut".equals(value.getContractType()))
            .toStream()
            .map((key, contract) -> {
                if (contract.getSecurityType() == null || 
                    contract.getExchangeId() == null || 
                    contract.getExchangeClearingId() == null ||
                    contract.getContractSize() == null || 
                    (contract.getLastTradeDate() == null && contract.getMaturityDate() == null)) {
                    return KeyValue.pair(key, contract);
                }
                String lookupKey = String.format("%s:%s:%s:%d:%s",
                    contract.getSecurityType(),
                    contract.getExchangeId(),
                    contract.getExchangeClearingId(),
                    contract.getContractSize(),
                    contract.getLastTradeDate() != null ? 
                        contract.getLastTradeDate() : 
                        contract.getMaturityDate());
                return KeyValue.pair(lookupKey, contract);
            })
            .toTable();

        // 创建产品查找表
        KTable<String, Product> productLookupTable = productTable
            .toStream()
            .map((key, product) -> {
                if (product.getSecurityType() == null || 
                    product.getExchangeId() == null || 
                    product.getExchangeClearingId() == null) {
                    return KeyValue.pair(key, product);
                }
                String lookupKey = String.format("%s:%s:%s",
                    product.getSecurityType(),
                    product.getExchangeId(),
                    product.getExchangeClearingId());
                return KeyValue.pair(lookupKey, product);
            })
            .toTable();

        // 创建期权合约查找表
        KTable<String, Contract> optionContractTable = contractTable
            .filter((key, value) -> value != null && "opt".equals(value.getContractType()))
            .toStream()
            .map((key, contract) -> {
                if (contract.getProductId() == null) {
                    return KeyValue.pair(key, contract);
                }
                String lookupKey = contract.getProductId();
                return KeyValue.pair(lookupKey, contract);
            })
            .toTable();

        // 进行数据富化
        // 第一步：根据合约类型分流
        KStream<String, PositionInstrument> futureStream = positionStream
            .filter((key, position) -> "fut".equals(position.getContractType()))
            .map((key, position) -> {
                String lookupKey = String.format("%s:%s:%s:%d:%s",
                    position.getSecurityType(),
                    position.getExchangeId(),
                    position.getExchangeClearingId(),
                    position.getContractSize(),
                    position.getLastTradeDate());
                return KeyValue.pair(lookupKey, position);
            });

        KStream<String, PositionInstrument> optionStream = positionStream
            .filter((key, position) -> "opt".equals(position.getContractType()))
            .map((key, position) -> {
                String lookupKey = String.format("%s:%s:%s",
                    position.getSecurityType(),
                    position.getExchangeId(),
                    position.getExchangeClearingId());
                return KeyValue.pair(lookupKey, position);
            });

        // 第二步：期货合约富化
        KStream<String, PositionInstrument> futureEnrichedStream = futureStream
            .leftJoin(futureContractTable,
                (position, contract) -> {
                    if (contract != null) {
                        position.setName(contract.getName());
                        position.setContractType(contract.getContractType());
                        position.setSecurityType(contract.getSecurityType());
                        position.setExchangeId(contract.getExchangeId());
                        position.setExchangeClearingId(contract.getExchangeClearingId());
                        position.setContractSize(contract.getContractSize());
                        position.setLastTradeDate(contract.getLastTradeDate());
                        position.setMaturityDate(contract.getMaturityDate());
                    }
                    return position;
                },
                org.apache.kafka.streams.kstream.Joined.with(Serdes.String(),
                    positionSerde,
                    contractSerde));

        // 第三步：期权合约富化
        KStream<String, PositionInstrument> optionEnrichedStream = optionStream
            .leftJoin(productLookupTable,
                (position, product) -> {
                    if (product != null) {
                        position.setProductId(product.getId());
                    }
                    return position;
                },
                org.apache.kafka.streams.kstream.Joined.with(Serdes.String(),
                    positionSerde,
                    productSerde))
            .selectKey((key, value) -> value.getProductId() != null ? value.getProductId() : key)
            .leftJoin(optionContractTable,
                (position, contract) -> {
                    if (contract != null) {
                        position.setName(contract.getName());
                        position.setContractType(contract.getContractType());
                        position.setSecurityType(contract.getSecurityType());
                        position.setExchangeId(contract.getExchangeId());
                        position.setExchangeClearingId(contract.getExchangeClearingId());
                        position.setContractSize(contract.getContractSize());
                        position.setLastTradeDate(contract.getLastTradeDate());
                        position.setMaturityDate(contract.getMaturityDate());
                    }
                    return position;
                },
                org.apache.kafka.streams.kstream.Joined.with(Serdes.String(),
                    positionSerde,
                    contractSerde));

        // 第四步：合并流
        KStream<String, PositionInstrument> enrichedStream = futureEnrichedStream
            .merge(optionEnrichedStream);

        // 输出到目标主题
        enrichedStream.to(outputTopic,
            org.apache.kafka.streams.kstream.Produced.with(Serdes.String(),
                positionSerde));

        return enrichedStream;
    }
} 