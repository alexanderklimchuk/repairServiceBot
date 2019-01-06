package by.losika.ts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BotApplication {

  @Autowired
  static GoogleFormSubmitter submitter;

  public BotApplication(GoogleFormSubmitter submitter){
    this.submitter = submitter;
  }

  public static void main(String[] args) {
    SpringApplication.run(BotApplication.class, args);

    BotApplication botApp = new BotApplication(submitter);



  }
}