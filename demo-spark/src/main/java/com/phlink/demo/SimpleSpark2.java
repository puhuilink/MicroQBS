package com.phlink.demo;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import scala.Tuple2;

import java.util.Arrays;
import java.util.List;

public class SimpleSpark2 {
    public static final String APP_NAME = "simple App";
    public static final String MASTER = "local";
    public static final SparkConf CONF;
    public static final JavaSparkContext SC;
    public static final String LOCAL_FILE = "/Users/wen/dev/phlink-common-framework/demo-spark/data/hello.txt";

    static {
        CONF = new SparkConf().setAppName(APP_NAME).setMaster(MASTER);
        SC = new JavaSparkContext(CONF);
    }


    public static void simpleMapReduce1() {
        Long start = System.currentTimeMillis();
        List<Integer> data = Arrays.asList(1, 2, 3, 4, 5);
        JavaRDD<Integer> distData = SC.parallelize(data);
        JavaRDD<Integer> lineLengths = distData.map(new Function<Integer, Integer>() {
            @Override
            public Integer call(Integer a) throws Exception {
                return a;
            }
        });
        int result = lineLengths.reduce(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer a, Integer b) {
                return (a + b) / 2;
            }
        });

        Long end = System.currentTimeMillis();
        System.out.println("simpleMapReduce1 result: " + result + " time: " + (end - start));
    }


    public static void simpleMapReduce2() {
        Long start = System.currentTimeMillis();
        JavaRDD<String> lines = SC.textFile(LOCAL_FILE);
        JavaRDD<Integer> lineLengths = lines.map(new Function<String, Integer>() {
            @Override
            public Integer call(String s) {
                return s.length();
            }
        });
        int totalLength = lineLengths.reduce(new Function2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer a, Integer b) {
                return a + b;
            }
        });
        Long end = System.currentTimeMillis();
        System.out.println("simpleMapReduce2 totalLength: " + totalLength + " time: " + (end - start));
    }


    public static void simpleMapReduce3() {
        Long start = System.currentTimeMillis();
        JavaRDD<String> lines = SC.textFile(LOCAL_FILE);
        JavaRDD<Integer> lineLengths = lines.map(new GetLength());
        int totalLength = lineLengths.reduce(new Sum());
        Long end = System.currentTimeMillis();
        System.out.println("simpleMapReduce3 totalLength: " + totalLength + " time: " + (end - start));
    }

    public static void simpleMapReduce4() {
        Long start = System.currentTimeMillis();
        JavaRDD<String> lines = SC.textFile(LOCAL_FILE);
        JavaPairRDD<String, Integer> pairs = lines.mapToPair(s -> new Tuple2(s, 1));
        JavaPairRDD<String, Integer> counts = pairs.reduceByKey((a, b) -> a + b);
        Long end = System.currentTimeMillis();
        counts.sortByKey().foreach(f -> {
            System.out.println("句子: + " + f._1() + " 次数: + " + f._2());
        });
        System.out.println("simpleMapReduce4 time: " + (end - start));
    }

    public static void stop() {
        SC.stop();
    }

    public static void main(String[] args) {
        SimpleSpark2.simpleMapReduce1();
        SimpleSpark2.simpleMapReduce2();
        SimpleSpark2.simpleMapReduce3();
        SimpleSpark2.simpleMapReduce4();
        SimpleSpark2.stop();
    }

    static class GetLength implements Function<String, Integer> {
        @Override
        public Integer call(String s) {
            return s.length();
        }
    }

    static class Sum implements Function2<Integer, Integer, Integer> {
        @Override
        public Integer call(Integer a, Integer b) {
            return a + b;
        }
    }

}
