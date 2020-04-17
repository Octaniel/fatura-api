package com.imposto.fatura;

import com.imposto.fatura.config.property.SpringApiProperty;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(SpringApiProperty.class)
@SpringBootApplication
public class FaturaApplication {

    public static void main(String[] args) {
        SpringApplication.run(FaturaApplication.class, args);
    }

}
