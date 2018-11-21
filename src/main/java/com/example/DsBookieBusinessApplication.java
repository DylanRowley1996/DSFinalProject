package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/*
@author Qinyuan Zhang
@date 21/11/2018
 */
@SpringBootApplication(scanBasePackages={"com.example"})
public class DsBookieBusinessApplication {

   public static void main(String[] args) {
      SpringApplication.run(DsBookieBusinessApplication.class, args);
   }

}
