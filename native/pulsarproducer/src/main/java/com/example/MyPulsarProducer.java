package com.example;

import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.PulsarClientException;

import java.nio.charset.StandardCharsets;

public class MyPulsarProducer {
    public static void main(String[] args) throws PulsarClientException {
        PulsarClient client = PulsarClient.builder()
                .serviceUrl("pulsar+ssl://pulsar.example.com:6651")
                .tlsTrustCertsFilePath(MyPulsarProducer.class.getResource("/cacerts.pem").getPath())
                .allowTlsInsecureConnection(false)
                .enableTlsHostnameVerification(true)
                .build();
        Producer<byte[]> producer = client.newProducer()
                .topic("apache/pulsar/mytopic")
                .create();
        for (int i = 0; i < 30; i++) {
            producer.send(String.format("My message hello world TMA %d", i).getBytes(StandardCharsets.UTF_8));
        }
        producer.close();
        client.close();
    }
}
