package com.phlink.demo;


import org.apache.spark.api.java.function.FilterFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;

public class SimpleSpark {
    public static void main(String[] args) {
        String logFile = "/Users/wen/dev/phlink-common-framework/demo-spark/data/hello.txt";
        SparkSession spark = SparkSession.builder().appName("Simple Application").master("local").getOrCreate();
        Dataset<String> logData = spark.read().textFile(logFile).cache();

        long numAs = logData.filter((FilterFunction<String>) s -> s.contains("hello")).count();
        long numBs = logData.filter((FilterFunction<String>) s -> s.contains("world")).count();

        System.out.println("Lines with a: " + numAs + ", lines with b: " + numBs);

        spark.stop();
    }
}
