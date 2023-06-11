package br.lucasalves.api.integrationtests.testcontainers;

import java.util.Map;
import java.util.stream.Stream;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;

@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {

	public class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.33");
		
		private static void startContainers() {
			Startables.deepStart(Stream.of(mysql)).join();
		}
		
		private static Map<String, String> createConnectionConfiguration() {
			return Map.of(
					"spring:datasource.url", mysql.getJdbcUrl(),
					"spring:datasource.username", mysql.getUsername(),
					"spring:datasource.password", mysql.getPassword()
					);
		}
		
		@Override
		public void initialize(ConfigurableApplicationContext applicationContext) {
			startContainers();
			ConfigurableEnvironment environment = applicationContext.getEnvironment();
			@SuppressWarnings({"unchecked", "rawtypes"})
			MapPropertySource testcontainers = new MapPropertySource("testcontainers", (Map) createConnectionConfiguration());
			environment.getPropertySources().addFirst(testcontainers);
		}
		
	}

	
	
}
