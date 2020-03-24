package com.phlink.demo;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.StorageLevels;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import scala.Tuple2;

import java.util.Arrays;
import java.util.regex.Pattern;

public class StreamSpark {
    public static final String appName = "JavaNetworkWordCount";
    public static final String master = "local[*]";
    public static final SparkConf conf;
    public static final JavaStreamingContext jssc;
    public static final String localFile = "/Users/wen/dev/phlink-common-framework/demo-spark/data/hello.txt";
    private static final Pattern SPACE = Pattern.compile(" ");

    static {
        conf = new SparkConf().setAppName(appName).setMaster(master);
        jssc = new JavaStreamingContext(conf, Durations.seconds(1));
    }


    public static void main(String[] args) throws InterruptedException {
        // nc -lk 9999
        JavaReceiverInputDStream<String> lines = jssc.socketTextStream("localhost", 9999, StorageLevels.MEMORY_AND_DISK_SER);

        JavaDStream<String> words = lines.flatMap(x -> Arrays.asList(SPACE.split(x)).iterator());

        JavaPairDStream<String, Integer> wordCounts = words.mapToPair(s -> new Tuple2<>(s, 1))
                .reduceByKey((i1, i2) -> i1 + i2);

        wordCounts.print();
        jssc.start();              // Start the computation
        jssc.awaitTermination();   // Wait for the computation to terminate
//        StreamSpark.stop();
    }
}
