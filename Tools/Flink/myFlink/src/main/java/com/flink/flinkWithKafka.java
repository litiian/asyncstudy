package com.flink;

import com.flink.deserializationSchema.ByteArrayDeserializationSchema;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.serialization.TypeInformationSerializationSchema;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;

import java.util.Properties;

public class flinkWithKafka {

    public static void main(String[] args) {

        //设置运行环境
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //event-time
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.getConfig().setAutoWatermarkInterval(1000);

        //配置kafka消费端
        Properties props = new Properties();
        props.setProperty("zookeeper.connect","172.16.4.66,172.16.4.65,172.16.4.64");
        props.setProperty("bootstrap.servers","172.16.4.61:9092,172.16.4.62:9092,172.16.4.63:9092");
        props.setProperty("group.id","myFlinkGroup");
        props.setProperty("auto.offset.reset","earliest");

        //创建kafka消费者
        FlinkKafkaConsumer011<byte[]> consumer=new FlinkKafkaConsumer011(
                "zcsy",new ByteArrayDeserializationSchema(),
                props
        );

        DataStream rides = env.addSource(consumer);

        System.out.print(rides.print());

    }
}
