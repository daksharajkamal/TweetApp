package com.tweetapp.configs.metrics;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.micrometer.core.instrument.MeterRegistry;

/**
 * Configuration for Micrometer metrics.
 *
 * @author cogjava729
 */
@Configuration
public class MicrometerConfig {

	private final BuildProperties buildProperties;
	private final String environment;
	private final String artifactId;

	public static final String ENVIRONMENT = "environment";
	public static final String SERVICE_NAME = "service";
	public static final String HOST_NAME = "host_name";

	public static final String HOST_IP = "host_ip";

	public MicrometerConfig(final BuildProperties pBuildProperties, @Value("${env}") final String pEnvironment) {
		buildProperties = pBuildProperties;
		environment = pEnvironment;
		artifactId = buildProperties.getArtifact().replace('-', '_');
	}

	@Bean
	MeterRegistryCustomizer<MeterRegistry> registerCommonTags(
			@Value("${management.application.name}") String applicationName) {

		return registry -> registry.config().commonTags("" + ENVIRONMENT, environment, "" + SERVICE_NAME, artifactId,
				"application", applicationName, HOST_IP, getHostIp(), HOST_NAME, getHostName(), "build.group",
				buildProperties.getGroup(), "build.artifact", buildProperties.getArtifact(), "build.version",
				buildProperties.getVersion());
	}

	private String getHostName() {
		try {
			return InetAddress.getLocalHost().getCanonicalHostName();
		} catch (UnknownHostException e) {
			return "unknown";
		}
	}

	private String getHostIp() {
		try {
			return InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			return "unknown";
		}
	}
}
