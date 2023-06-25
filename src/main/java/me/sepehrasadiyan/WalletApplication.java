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

	/**
	 * @author Sepehr Asadiyan
	 * @author SepehrAsadiyan
	 * @version 85.0
	 */

	public static void main(String[] args) {
		SpringApplication.run(WalletApplication.class, args);
	}



}
