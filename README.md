# KS3 - Kafka Streams Position Enrichment Service

This project implements a Kafka Streams application that enriches position instrument data with product and contract information.

## Overview

The service processes position instrument data and enriches it with additional information from product and contract lookup tables. It supports both futures and options contracts.

## Kafka Topics

### Business Topics

1. **position-instrument**
   - Purpose: Input topic for position instrument data
   - Key: String (position ID)
   - Value: PositionInstrument
   - Description: Contains basic position information that needs to be enriched

2. **gold-smc**
   - Purpose: Lookup topic containing product and contract information
   - Key: String (ID)
   - Value: GoldSmcMessage (wrapper containing Product/Contract/Equity data)
   - Description: Contains reference data for products and contracts

3. **position-instrument-enriched**
   - Purpose: Output topic for enriched position data
   - Key: String (position ID)
   - Value: PositionInstrument
   - Description: Contains fully enriched position information

### Internal Topics (Created by Kafka Streams)

1. **KSTREAM-AGGREGATE-STATE-STORE-0000000000**
   - Purpose: State store for product lookup table
   - Description: Stores product information for quick lookups

2. **KSTREAM-AGGREGATE-STATE-STORE-0000000001**
   - Purpose: State store for contract lookup table
   - Description: Stores contract information for quick lookups

3. **KSTREAM-AGGREGATE-STATE-STORE-0000000002**
   - Purpose: State store for future contract lookup table
   - Description: Stores future contract information for quick lookups

4. **KSTREAM-AGGREGATE-STATE-STORE-0000000003**
   - Purpose: State store for option contract lookup table
   - Description: Stores option contract information for quick lookups

## Data Flow

1. **Position Instrument Processing**
   - Input: position-instrument topic
   - Processing: Split into futures and options streams
   - Output: Enriched data to position-instrument-enriched topic

2. **Product/Contract Lookup**
   - Input: gold-smc topic
   - Processing: 
     - Filter and transform into product and contract tables
     - Create lookup tables for futures and options
   - Usage: Used for enriching position data

## Data Models

1. **PositionInstrument**
   - Basic position information
   - Contains fields for both futures and options positions
   - Gets enriched with product and contract details

2. **Product**
   - Product reference data
   - Used for options contract lookup
   - Contains product-specific information

3. **Contract**
   - Contract reference data
   - Supports both futures and options contracts
   - Contains contract-specific information

4. **GoldSmcMessage**
   - Wrapper class for different message types
   - Handles type-safe conversion between different message types
   - Supports product, contract, and equity data

## Configuration

The application uses the following key configurations:

```properties
# Kafka Topics
kafka.topic.position-instrument=position-instrument
kafka.topic.gold-smc=gold-smc
kafka.topic.output=position-instrument-enriched

# Kafka Streams Configuration
spring.kafka.streams.application-id=ks3-application
spring.kafka.streams.properties.default.key.serde=org.apache.kafka.common.serialization.Serdes$StringSerde
spring.kafka.streams.properties.default.value.serde=org.springframework.kafka.support.serializer.JsonSerde
```

## Running the Application

1. Ensure Kafka is running and accessible
2. Create required topics (if not auto-created)
3. Start the application
4. Send test data to the input topics

## Error Handling

The application includes:
- Null checks for all data processing
- Type validation for message processing
- Error handling for deserialization issues
- Logging for processing errors

## Dependencies

- Spring Boot
- Spring Kafka
- Kafka Streams
- Lombok
- JSON Serialization/Deserialization 