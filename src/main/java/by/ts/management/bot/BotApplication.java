package by.ts.management.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"by.ts.management.bot","by.ts.management.rest", "by.ts.management.service"})
public class BotApplication {
  public static void main(String[] args) {
    SpringApplication.run(BotApplication.class, args);
  }
}