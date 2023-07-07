package br.lucasalves.api.integrationtests.testcontainers;

import java.util.Map;
import java.util.stream.Stream;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;

@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {

	public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

		public static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.33")
				.withExposedPorts(8888); // Define a porta exposta pelo contêiner

		private static void startContainers() {
			Startables.deepStart(Stream.of(mysql)).join();
		}

		private static Map<String, String> createConnectionConfiguration() {
			return Map.of(
					"spring:datasource.url", mysql.getJdbcUrl().replace("jdbc:", ""),
					"spring:datasource.username", mysql.getUsername(),
					"spring:datasource.password", mysql.getPassword()
			);
		}

		@Override
		public void initialize(@NotNull ConfigurableApplicationContext applicationContext) {
			startContainers();
			ConfigurableEnvironment environment = applicationContext.getEnvironment();
			@SuppressWarnings({"unchecked", "rawtypes"})
			MapPropertySource testcontainers = new MapPropertySource("testcontainers", (Map) createConnectionConfiguration());
			environment.getPropertySources().addFirst(testcontainers);
		}

	}

}
