package com.example;

import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.api.schema.SchemaDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Example that demonstrates a consumer consuming messages using {@link Schema#AVRO(Class)}.
 */
public class AvroSchemaConsumerExample {

    private static final Logger log = LoggerFactory.getLogger(AvroSchemaConsumerExample.class);

    private static final String TOPIC = "apache/pulsar/my-result-topic";

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(final String[] args) {

        final String pulsarServiceUrl = "pulsar://localhost:6650";

        try (PulsarClient client = PulsarClient.builder()
                .serviceUrl(pulsarServiceUrl)
                .authentication(AuthenticationFactory.token("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiJ9.etXkxEP3vJlKIuwO7YRdr7IVSvnCD4107jLacuYIdBo"))
                .build()) {

            Schema<Count> countSchema = Schema.AVRO(
                    SchemaDefinition.<Count>builder()
                            .withPojo(Count.class)
                            .withAlwaysAllowNull(true)
                            .build()
            );

            try (Consumer<Count> consumer = client.newConsumer(countSchema)
                    .topic(TOPIC)
                    .subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
                    .subscriptionName("test-payments")
                    .subscribe()) {

//                final int numMessages = 10;

                while (true) {
                    Message<Count> msg = consumer.receive();

                    final String key = msg.getKey();
                    final Count count = msg.getValue();

                    System.out.printf("key = %s, value = %s%n", key, count);
                }
            }
        } catch (PulsarClientException e) {
            log.error("Failed to consume avro messages from pulsar", e);
            Runtime.getRuntime().exit(-1);
        }
    }

}
