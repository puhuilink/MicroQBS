package com.phlink.demo.spark

import org.apache.spark.{SparkConf, SparkContext}

object WordCountLocal extends App {

  var conf = new SparkConf()
    .setMaster("local")
    .setAppName("testRdd")

  val sc = new SparkContext(conf)
  val data = sc.textFile("/Users/wen/dev/phlink-common-framework/demo-spark/data/hello.txt")
  data.flatMap(_.split(" "))
    .map((_, 1))
    .reduceByKey(_ + _)
    .collect()
    .foreach(println)

  Thread.sleep(100000)
}
