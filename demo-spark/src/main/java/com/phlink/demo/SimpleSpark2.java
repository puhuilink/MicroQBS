package com.phlink.demo;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;

import java.util.Arrays;
import java.util.List;

public class SimpleSpark2 {
    public static final String appName = "simple App";
    public static final String master = "local";
    public static final SparkConf conf;
    public static final JavaSparkContext sc;

    static {
        conf = new SparkConf().setAppName(appName).setMaster(master);
        sc = new JavaSparkContext(conf);
    }


    public static void simpleMapReduce1(){
        List<Integer> data = Arrays.asList(1, 2, 3, 4, 5);
        JavaRDD<Integer> distData = sc.parallelize(data);
        JavaRDD<Integer> lineLengths = distData.map(new Function<Integer, Integer>() {
            @Override
            public Integer call(Integer a) throws Exception {
                return a;
            }
        });
        int result = lineLengths.reduce(new Function2<Integer, Integer, Integer>() {
            public Integer call(Integer a, Integer b) { return (a + b)/2; }
        });

        System.out.println("result: " + result);
    }


    public static void simpleMapReduce2() {
        String localFile = "/Users/wen/dev/phlink-common-framework/demo-spark/data/hello.txt";
        JavaRDD<String> lines = sc.textFile(localFile, 10);
        JavaRDD<Integer> lineLengths = lines.map(new Function<String, Integer>() {
            public Integer call(String s) { return s.length(); }
        });
        int totalLength = lineLengths.reduce(new Function2<Integer, Integer, Integer>() {
            public Integer call(Integer a, Integer b) { return a + b; }
        });
        System.out.println("totalLength: " + totalLength);
    }

    public static void stop() {
        sc.stop();
    }

    public static void main(String[] args) {
        SimpleSpark2.simpleMapReduce1();
        SimpleSpark2.simpleMapReduce2();
        SimpleSpark2.stop();
    }

}
