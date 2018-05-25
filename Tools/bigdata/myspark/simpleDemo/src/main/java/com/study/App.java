package com.study;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.SparkSession;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        String logFile="YOUR_SPARK_HOME/README.md"; // Should be some file on your system
        SparkSession spark= SparkSession.builder().appName("simpleDeamo").getOrCreate();
        Dataset<String> logData=spark.read().textFile(logFile).cache();
        long numAs = logData.filter(s -> s.contains("a")).count();
    }
}
