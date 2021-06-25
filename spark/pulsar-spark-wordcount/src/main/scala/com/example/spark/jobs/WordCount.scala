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


  val lines = spark.readStream
    .format("pulsar")
    .option("service.url", "pulsar://192.168.56.2:6650")
    .option("admin.url", "http://192.168.56.2:8080")
    .option("topic", "apache/pulsar/my-topic")
    .option("startingOffsets", "latest")
    .load()

//  lines.printSchema()

//  val words = lines.select(col("value").cast("string"))
//    .as[String].flatMap(_.split("\\s+"))

//  val wc = words.groupBy("value").count()

//  val query = wc.selectExpr("to_json(struct(*)) AS value")
  val out = lines.select(col("value").cast("string"))

  val query = out.writeStream
    .format("pulsar")
//    .outputMode("complete")
    .option("checkpointLocation", "/tmp/checkpoint")
    .option("service.url", "pulsar://192.168.56.2:6650")
    .option("admin.url", "http://192.168.56.2:8080")
    .option("topic", "apache/pulsar/my-result-topic")
    .start()

    query.awaitTermination()
}
