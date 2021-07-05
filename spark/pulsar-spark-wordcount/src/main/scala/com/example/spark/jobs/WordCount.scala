package com.example.spark.jobs

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._

object WordCount extends App {
  val spark = SparkSession.builder()
    .appName("WordCount")
    .master("local[2]")
    .getOrCreate()

  import spark.implicits._
  spark.sparkContext.setLogLevel("ERROR")

//  val certPath = getClass.getResource("/ca.cert.pem").getPath
  val lines = spark.readStream
    .format("pulsar")
    .option("service.url", "pulsar://192.168.56.2:6650")
    .option("admin.url", "http://192.168.56.2:8080")
    .option("pulsar.client.authPluginClassName", "org.apache.pulsar.client.impl.auth.AuthenticationToken")
    .option("pulsar.client.authParams", "token:eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiJ9.etXkxEP3vJlKIuwO7YRdr7IVSvnCD4107jLacuYIdBo")
//    .option("pulsar.client.tlsTrustCertsFilePath", certPath)
//    .option("pulsar.client.tlsAllowInsecureConnection", "false")
//    .option("pulsar.client.tlsHostnameVerificationEnable", "false")
    .option("topic", "apache/pulsar/my-topic")
    .option("startingOffsets", "latest")
    .load()

  val out = lines.select(col("value").cast("string"))

  val query = out.writeStream
    .format("pulsar")
    .option("checkpointLocation", "/tmp/checkpoint")
    .option("service.url", "pulsar://192.168.56.2:6650")
    .option("admin.url", "http://192.168.56.2:8080")
    .option("pulsar.client.authPluginClassName", "org.apache.pulsar.client.impl.auth.AuthenticationToken")
    .option("pulsar.client.authParams", "token:eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiJ9.etXkxEP3vJlKIuwO7YRdr7IVSvnCD4107jLacuYIdBo")
//    .option("pulsar.client.tlsTrustCertsFilePath", certPath)
//    .option("pulsar.client.tlsAllowInsecureConnection", "false")
//    .option("pulsar.client.tlsHostnameVerificationEnable", "false")
    .option("topic", "apache/pulsar/my-result-topic")
    .start()

    query.awaitTermination()
}
