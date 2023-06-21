package me.sepehrasadiyan.exception;

public class IncompleteProfileException extends NullPointerException {
  @Override
  public String getMessage() {
    return "User's profile is not complete";
  }

  @Override
  public String getLocalizedMessage() {
    return "پروفایل کاربر کامل نیست.";
  }
}
