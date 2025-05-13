package com.aplazo.mx.creditlines;

import org.springframework.boot.SpringApplication;

public class TestAplazoBackendChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.from(AplazoBackendChallengeApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
