package com.sonata.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada da aplicacao Sonata API.
 *
 * <p>A anotacao {@link SpringBootApplication} liga tres coisas:
 * <ul>
 *   <li>@Configuration  - permite declarar beans</li>
 *   <li>@EnableAutoConfiguration - configura Spring com base nas dependencias</li>
 *   <li>@ComponentScan - varre o pacote com.sonata.api por @Service, @Repository etc.</li>
 * </ul>
 */
@SpringBootApplication
public class SonataApiApplication {

    public static void main(String[] args) {
        // Sobe o servidor embutido (Tomcat) na porta definida em application.properties.
        SpringApplication.run(SonataApiApplication.class, args);
    }
}
