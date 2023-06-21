package me.sepehrasadiyan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@ComponentScan(basePackages = "me.sepehrasadiyan.*")
@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class WalletApplication {

	public static void main(String[] args) {
		System.setProperty("java.net.preferIPv4Stack", "true");
		SpringApplication.run(WalletApplication.class, args);
	}



}
