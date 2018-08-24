package com.flink;


import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

public class SocketWindowWordCount {

    public static void main(String[] args) throws Exception {

        //从命令行中解析端口
        final int port;
        try{
            final ParameterTool paras= ParameterTool.fromArgs(args);
            port=paras.getInt("port");
        }catch(Exception e){
            System.err.print("No port specified. Please run 'sockWindowWordCount--port<port>'");
            return;
        }

        //获取执行环境
        final StreamExecutionEnvironment env=StreamExecutionEnvironment.getExecutionEnvironment();

        //从socket中获取输入数据
        DataStream<String> text =env.socketTextStream("localhost",port,"\n");

        DataStream<WordWithCount> windowCounts=text.flatMap(new FlatMapFunction<String, WordWithCount>() {
            @Override
            public void flatMap(String value, Collector<WordWithCount> out) throws Exception {
                for(String word:value.split("\\s")){
                    out.collect(new WordWithCount(word,1L));
                }
            }
        }).keyBy("word")
                //每5秒从socket中读取一次数据
                .timeWindow(Time.seconds(5L),Time.seconds(1L))
                .reduce(new ReduceFunction<WordWithCount>() {
                    @Override
                    public WordWithCount reduce(WordWithCount a, WordWithCount b) throws Exception {
                        return new WordWithCount(a.word,a.count+b.count);
                    }
                });
        //设置单线程执行
        windowCounts.print().setParallelism(1);
        env.execute("Socket window wordCount");
    }

    public static class WordWithCount{
        public String word;
        public long count;

        public WordWithCount(){}

        public WordWithCount(String word,long count){
            this.word=word;
            this.count=count;
        }

        @Override
        public String toString() {
            return word+":"+count;
        }
    }
}
