package com.tweetapp;

import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.log4j.Log4j2;

/**
 * @author cogjava729
 */
@Log4j2
@SpringBootApplication
public class TweetAppApiApplication {

	public static void main(String[] args) {
		log.info("Setting TimeZone to default..");
		TimeZone.setDefault(TimeZone.getDefault());
		SpringApplication.run(TweetAppApiApplication.class, args);
	}

}
