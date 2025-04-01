package com.example.ks3;

import com.example.ks3.config.KafkaStreamConfig;
import com.example.ks3.model.PositionInstrument;
import com.example.ks3.model.BaseGoldSmcData;
import com.example.ks3.util.TestDataGenerator;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EmbeddedKafka
public class KafkaStreamTest {

    @Autowired
    private KafkaStreamConfig kafkaStreamConfig;

    @Test
    public void testStreamProcessing() {
        // 获取测试数据
        List<PositionInstrument> positions = TestDataGenerator.generatePositionInstruments();
        List<BaseGoldSmcData> goldSmcData = TestDataGenerator.generateGoldSmcData();

        // 验证期货合约富化
        PositionInstrument futPosition = positions.stream()
            .filter(p -> "fut".equals(p.getContractType()))
            .findFirst()
            .orElse(null);
        assertNotNull(futPosition);
        assertEquals("FUTURE", futPosition.getSecurityType());
        assertEquals("CMX", futPosition.getExchangeId());
        assertEquals("GC", futPosition.getExchangeClearingId());

        // 验证期权合约富化
        PositionInstrument optPosition = positions.stream()
            .filter(p -> "opt".equals(p.getContractType()))
            .findFirst()
            .orElse(null);
        assertNotNull(optPosition);
        assertEquals("OPTION", optPosition.getSecurityType());
        assertEquals("CMX", optPosition.getExchangeId());
        assertEquals("GC", optPosition.getExchangeClearingId());

        // 验证产品关联
        assertNotNull(optPosition.getProductId());
        assertEquals("GOLD", optPosition.getProductId());
    }
} 