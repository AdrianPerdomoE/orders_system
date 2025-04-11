package com.adpe.orders_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.adpe.orders_system.config.EnvConfig;

@SpringBootApplication
public class OrdersSystemApplication {

	public static void main(String[] args) {
		// Load environment variables from .env file if not running in Docker
		new EnvConfig().loadEnv();
		SpringApplication.run(OrdersSystemApplication.class, args);
	}

}
