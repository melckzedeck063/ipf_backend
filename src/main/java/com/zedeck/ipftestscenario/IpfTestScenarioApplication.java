package com.zedeck.ipftestscenario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class IpfTestScenarioApplication {

    public static void main(String[] args) {
        SpringApplication.run(IpfTestScenarioApplication.class, args);
    }

}
