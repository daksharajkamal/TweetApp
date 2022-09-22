package com.tweetapp.configs.mongodb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.WriteConcernResolver;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientURI;
import com.mongodb.WriteConcern;

import lombok.extern.log4j.Log4j2;

/**
 * @author cogjava729
 */

@Configuration
@EnableMongoAuditing
@Log4j2
@EnableMongoRepositories(basePackages = "com.tweetapp.repositories")
public class MongoDbConfig extends AbstractMongoConfiguration {

	private final String containerMongoUri;
	private final String dbName;
	private final int maxIdleConnectionTimeInMillis;

	public MongoDbConfig(@Value("${mongodb.uri:URI_CONFIGURATION_MISSING}") final String pContainerMongoUri,
			@Value("${mongodb.dbname}") final String pDbName,
			@Value("${mongodb.maxIdleConnectionTimeInMillis:30000}") final int pMaxIdleConnectionTimeInMillis) {
		this.containerMongoUri = pContainerMongoUri;
		this.dbName = pDbName;
		this.maxIdleConnectionTimeInMillis = pMaxIdleConnectionTimeInMillis;
	}

	@Override
	protected String getDatabaseName() {
		log.info("Database Name: '{}'", dbName);
		return dbName;
	}

	@Override
	public MongoClient mongoClient() {
		log.debug("Initializing DocumentDB uri: {}", containerMongoUri);
		return new MongoClient(
				new MongoClientURI(containerMongoUri, MongoClientOptions.builder().sslInvalidHostNameAllowed(true)
						.maxConnectionIdleTime(maxIdleConnectionTimeInMillis).writeConcern(WriteConcern.ACKNOWLEDGED)));
	}

	@Bean
	public WriteConcernResolver writeConcernResolver() {
		return action -> {
			log.info("Using Write Concern of Acknowledged");
			return WriteConcern.ACKNOWLEDGED;
		};
	}
}
