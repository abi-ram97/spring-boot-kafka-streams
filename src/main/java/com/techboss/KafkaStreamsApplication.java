package com.techboss;

import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.techboss.kafka.model.User;

@SpringBootApplication
public class KafkaStreamsApplication {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	public static void main(String[] args) {
		SpringApplication.run(KafkaStreamsApplication.class, args);
	}
	
	//@Bean
	public Consumer<KStream<String, User>> process() {
		return stream -> stream.map((key, value)-> new KeyValue<>(value.getAge(), value.getName()))
				.groupByKey(Grouped.with(Serdes.Long(), Serdes.String()))
				.count()
				.toStream().foreach((key, value)->{
					 logger.info("Key [{}] with Value [{}]", key, value);
				});
	}
	
	@Bean
	public Function<KStream<String, User>, KStream<Long, Long>> userProcessor() {
		return inStream -> inStream.map((key, value)-> new KeyValue<>(value.getAge(), value.getName()))
				.groupByKey(Grouped.with(Serdes.Long(), Serdes.String()))
				.count()
				.toStream();
	}
	
}
