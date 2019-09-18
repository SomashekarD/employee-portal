package com.som.employeePortal;

import java.util.concurrent.CountDownLatch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * The Class EmployeePortalApplication.
 */
@SpringBootApplication
public class EmployeePortalApplication {

	/** The Constant logger. */
	private static final Logger logger = LogManager.getLogger(EmployeePortalApplication.class);
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		ApplicationContext applicationContext = null;
		try {
			applicationContext = new SpringApplicationBuilder(EmployeePortalApplication.class).build().run(args);
		} catch (final Exception e) {
			logger.error("Internal Error. shutting down Employee Portal application !!!!!");
			System.exit(-1);
		}
		logger.info("Employee Portal application started ...");
		awaitTermination(applicationContext);
	}

	/**
	 * Await termination.
	 *
	 * @param applicationContext the application context
	 */
	private static void awaitTermination(ApplicationContext applicationContext) {
		final CountDownLatch closeLatch = applicationContext.getBean(CountDownLatch.class);
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				logger.info("ShutDown Request Received..Closing Down Application");
				closeLatch.countDown();
			}
		});
		try {
			closeLatch.await();
		} catch (InterruptedException e) {
			logger.info("Inerrupted");
		}
	}

	/**
	 * Close latch.
	 *
	 * @return the count down latch
	 */
	@Bean
	public CountDownLatch closeLatch() {
		CountDownLatch cc = new CountDownLatch(1);
		return cc;
	}
}
