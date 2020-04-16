package com.pepcus.product.api.exception;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * File to fetch Tenant Configured Language messages
 */
public class MessageResourceHandler {

  private static final String MESSAGE_FILE_BASE = "messages.errors.errors";

  private Locale locale = null;

  public MessageResourceHandler(Locale locale) {
    this.locale = locale;
  }

  /**
   * Method to fetch Application Message as per Locale.
   * 
   * @param msg
   * @return
   */
  public String getApplicationMessage(String msg) {
    try {
      return getResourceBundle(MESSAGE_FILE_BASE).getString(msg);
    } catch (Exception e) {
      locale = Locale.ENGLISH;
      return getResourceBundle(MESSAGE_FILE_BASE).getString(msg);
    }
  }

  /**
   * Method to fetch Resource bundle based on language.
   * 
   * @param baseName
   * @return
   */
  private ResourceBundle getResourceBundle(String baseName) {
    return ResourceBundle.getBundle(baseName, locale);
  }

}
