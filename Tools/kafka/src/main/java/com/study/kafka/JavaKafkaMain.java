package com.study.kafka;

import java.util.Properties;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;

public class JavaKafkaMain {
	
	

	public static void main(String[] args) {
		
			String kafkaBootstrapServers="master:9092";
		
		 	Properties props = new Properties();
	        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
	        props.put(ProducerConfig.CLIENT_ID_CONFIG, "TcpServer");
	        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");
	        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.ByteArraySerializer");
	        //kafka中存byte[]
	        Producer<byte[], byte[]> producer = new KafkaProducer<>(props);
	        
	}

}
