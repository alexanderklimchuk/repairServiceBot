package by.ts.management.rest.model;


public class TaskNotification {

  private String name;
  private String phone;
  private String issue;
  private String flat;

  public TaskNotification() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getIssue() {
    return issue;
  }

  public void setIssue(String issue) {
    this.issue = issue;
  }

  public String getFlat() {
    return flat;
  }

  public void setFlat(String flat) {
    this.flat = flat;
  }

  @Override
  public String toString() {
    return String.format("Заявка: %s\nИмя: %s\nНомер квартиры: %s\nТелефон: %s",
        issue, name, flat, phone);
  }
}
