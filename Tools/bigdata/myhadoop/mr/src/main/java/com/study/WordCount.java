package com.study;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;
import java.util.StringTokenizer;

public class WordCount {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf=new Configuration();

        Job job=Job.getInstance(conf,"word count");
        job.setJarByClass(WordCount.class);
        job.setMapperClass(TokenizerMapper.class);
        //在每台机器上先执行合并操作
        job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        FileInputFormat.addInputPath(job,new Path(args[0]));
        FileOutputFormat.setOutputPath(job,new Path(args[1]));
        System.exit(job.waitForCompletion(true)?0:1);
    }


    //mapper class
    public static class TokenizerMapper extends Mapper<Object,Text,Text,IntWritable> {

        private Text word=new Text();

        private  final static IntWritable one=new IntWritable(1);

        @Override
        public void map(Object key,Text value,Context context) throws IOException, InterruptedException {
            //StringTokenizer 默认使用 "空格"、"制表符（'\t'）"、"换行符（'\n'）"、"回车符（‘\r’）"
            StringTokenizer itr=new StringTokenizer(value.toString());
            while (itr.hasMoreTokens()){
                word.set(itr.nextToken());
                context.write(word,one);
            }
        }
    }

    //Reducer class
    public static class IntSumReducer extends Reducer<Text,IntWritable,Text,IntWritable>
    {
        private IntWritable result=new IntWritable();
        public void reduce(Text key,Iterable<IntWritable> values,Context context) throws IOException, InterruptedException {
            int sum=0;
            for(IntWritable val:values){
                sum+=val.get();
            }
            result.set(sum);
            context.write(key,result);
        }
    }
}
