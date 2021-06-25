package com.example.spark.jobs

import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{IntegerType, StringType, StructField, StructType}

object WordCount extends App {
  val spark = SparkSession.builder()
    .appName("WordCount")
    .master("local[2]")
    .getOrCreate()

  spark.sparkContext.setLogLevel("ERROR")

  val someData = Seq(
    Row("python", 1024),
    Row("java", 5000),
    Row("golang", 2000)
  )

  val someSchema = List(
    StructField("language", StringType, nullable = false),
    StructField("amount", IntegerType, nullable = true)
  )

  val someDF = spark.createDataFrame(
    spark.sparkContext.parallelize(someData),
    StructType(someSchema)
  )

  someDF.write
    .format("pulsar")
    .option("checkpointLocation", "/tmp/checkpoint")
    .option("service.url", "pulsar://192.168.56.2:6650")
    .option("admin.url", "http://192.168.56.2:8080")
    .option("topic", "apache/pulsar/my-result-topic")
    .save()
}
