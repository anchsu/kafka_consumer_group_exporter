package org.ianitrix.jmx.exporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

/**
 * Main class
 * 
 * @author Guillaume Waignier
 *
 */
@Slf4j
public class Main {

	public static void main(String[] args) throws InterruptedException {
		log.info("Starting ...");

		checkArgument(args);

		final ConsumerGroupOffsetExporter consumerGroupOffsetExporter = new ConsumerGroupOffsetExporter(
				loadConfigurationFile(args));

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			consumerGroupOffsetExporter.stop();
		}));

		consumerGroupOffsetExporter.start();
	}

	private static void checkArgument(String[] args) {
		if (args.length != 1) {
			log.error("Missing argument for configuration file");
			System.out.println("Missing argument for configuration file");
			System.out.println("Command line are : java - jar Main.jar config.properties");
			System.exit(1);
		}
	}

	private static Properties loadConfigurationFile(String[] args) {
		final File propertiesFile = new File(args[0]);
		final Properties properties = new Properties();
		

		try (final FileInputStream inputStream = new FileInputStream(propertiesFile)) {
			properties.load(inputStream);
			return properties;
		} catch (final FileNotFoundException e) {
			log.error("Missing configuration file {}", args[0], e);
			System.out.println("Missing configuration file " + args[0]);
			System.exit(1);
		} catch (final IOException e) {
			log.error("Error when loading configuration file", e);
			System.out.println("Error when loading configuration file");
			System.exit(1);
		}
		
		return null;
	}
}
