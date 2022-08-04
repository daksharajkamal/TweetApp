package com.tweetapp.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaAdmin;

public class KafkaTopicConfig {

	@Value(value = "{kafka.bootstrapAddress}")
	private String bootstrapAddress;

	@Value(value = "{kafka.topicName}")
	private String topicName;

	@Bean
	public KafkaAdmin kafkaAdmin() {
		Map<String, Object> configs = new HashMap<>();
		return new KafkaAdmin(configs);
	}

	@Bean
	public NewTopic tweets() {
		return new NewTopic(topicName, 1, (short) 1);
	}
}
