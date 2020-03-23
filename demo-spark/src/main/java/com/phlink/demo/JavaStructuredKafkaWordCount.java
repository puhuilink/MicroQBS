package com.phlink.demo;

import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.streaming.StreamingQuery;

import java.util.Arrays;

public final class JavaStructuredKafkaWordCount {

  public static void main(String[] args) throws Exception {
    if (args.length < 3) {
      System.err.println("Usage: JavaStructuredKafkaWordCount <bootstrap-servers> " +
        "<subscribe-type> <topics>");
      System.exit(1);
    }

    String bootstrapServers = args[0];
    String subscribeType = args[1];
    String topics = args[2];

    SparkSession spark = SparkSession
      .builder()
      .appName("JavaStructuredKafkaWordCount")
      .master("local[*]")
      .getOrCreate();

    Dataset<String> lines = spark
      .readStream()
      .format("kafka")
      .option("kafka.bootstrap.servers", bootstrapServers)
      .option(subscribeType, topics)
      .load()
      .selectExpr("CAST(value AS STRING)")
      .as(Encoders.STRING());

    Dataset<Row> wordCounts = lines.flatMap(
        (FlatMapFunction<String, String>) x -> Arrays.asList(x.split(" ")).iterator(),
        Encoders.STRING()).groupBy("value").count();

    StreamingQuery query = wordCounts.writeStream()
      .outputMode("complete")
      .format("console")
      .start();

    query.awaitTermination();
  }
}