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
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.StringUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.util.*;

public class WordCount {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Configuration conf=new Configuration();
        GenericOptionsParser optionsParser=new GenericOptionsParser(conf,args);
        String[] remainingArgs=optionsParser.getRemainingArgs();
        if((remainingArgs.length!=2)&&(remainingArgs.length!=4)){
            System.err.println("Usage:wordcount <int><out>[-skip skipParrernFile]");
            System.exit(2);
        }

        Job job=Job.getInstance(conf,"word count");
        job.setJarByClass(WordCount.class);
        job.setMapperClass(TokenizerMapper.class);
        //在每台机器上先执行合并操作
        job.setCombinerClass(IntSumReducer.class);
        job.setReducerClass(IntSumReducer.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        List<String> otherArgs=new ArrayList<String>();
        for (int i = 0; i <remainingArgs.length ; i++) {
            if ("-skip".equals(remainingArgs[i])) {
               job.addCacheFile(new Path(remainingArgs[++i]).toUri());
               job.getConfiguration().setBoolean("wordcount.skip.parrerns",true);
            }else{
                otherArgs.add(remainingArgs[i]);
            }
        }

        FileInputFormat.addInputPath(job,new Path(otherArgs.get(0)));
        FileOutputFormat.setOutputPath(job,new Path(otherArgs.get(1)));

        System.exit(job.waitForCompletion(true)?0:1);
    }


    //mapper class
    public static class TokenizerMapper extends Mapper<Object,Text,Text,IntWritable> {

        private Text word=new Text();

        private  final static IntWritable one=new IntWritable(1);
        private boolean caseSensitive;
        private Set<String> patternsToSkip =new HashSet<String>();

        private  Configuration conf;
        private BufferedReader fis;
        @Override
        public void  setup(Context context) throws IOException {
           conf= context.getConfiguration();
           caseSensitive=conf.getBoolean("wordcount.case.sensitive",true);
            if(conf.getBoolean("wordcount.skip.patterns",false)){
                URI[] patternsURIs=Job.getInstance(conf).getCacheFiles();
                for(URI parrernsURI: patternsURIs){
                    Path patternsPath=new Path(parrernsURI.getPath());
                    String patternsFileName=patternsPath.getName().toString();
                    parseSkipFile(patternsFileName);
                }
            }
        }

        private void parseSkipFile(String fileName) {
            try {
                fis=new BufferedReader(new FileReader(fileName));
                String pattern=null;
                while ((pattern=fis.readLine())!=null){
                    patternsToSkip.add(pattern);
                }
            }catch (IOException ioe){
                System.err.print("Caught exception while parsing the cached file"
                        +StringUtils.stringifyException(ioe)
                );
            }
        }

        @Override
        public void map(Object key,Text value,Context context) throws IOException, InterruptedException {
            String line=caseSensitive?value.toString():value.toString().toLowerCase();
            for(String pattern:patternsToSkip){
                line=line.replaceAll(pattern,"");
            }
            //StringTokenizer 默认使用 "空格"、"制表符（'\t'）"、"换行符（'\n'）"、"回车符（‘\r’）"
            StringTokenizer itr=new StringTokenizer(line);
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
