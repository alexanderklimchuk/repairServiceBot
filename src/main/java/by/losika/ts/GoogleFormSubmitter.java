package by.losika.ts;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.ParameterizedMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GoogleFormSubmitter {

  @Value("${bot.googleform.url}")
  private String googleFormUrl;

  private final Logger logger = LogManager.getLogger(GoogleFormSubmitter.class);

  public void submitGoogleForm(Map<String,String> progress){

    HttpClient httpClient = HttpClients.createDefault();

    final String flat = progress.get("Квартира");
    final String name = progress.get("Имя");
    final String phone = progress.get("Телефон");
    final String issue = progress.get("Описание проблемы");

    HttpPost httppost = new HttpPost(googleFormUrl);
    ArrayList<NameValuePair> postParameters = new ArrayList<>();

    postParameters.add(new BasicNameValuePair("entry.1353619488", flat));
    postParameters.add(new BasicNameValuePair("entry.1220660911", name));
    postParameters.add(new BasicNameValuePair("entry.2081955572", phone));
    postParameters.add(new BasicNameValuePair("entry.1680239416", issue));

    try {
      httppost.setEntity(new UrlEncodedFormEntity(postParameters, "UTF-8"));
      httpClient.execute(httppost);
      logger.info(new ParameterizedMessage("New task successfully submitted. Flat: {}; Text: {}; ", flat, issue));
    } catch (IOException e) {
      logger.error(new ParameterizedMessage("Issue during task submitting happened. Flat: {}; Text: {}; ", flat, issue), e);
    }
  }
}
