package com.pepcus.product.api.exception;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Getter;

/**
 * Enum class for Error Codes
 */
public enum APIErrorCodes {

  // Common Errors
  APPLICATION_ERROR(1001),
  BAD_REQUEST(1002),
  INVALID_METHOD_TYPE(1003),
  NOT_FOUND(1004),

  // Validation Error Codes
  INVALID_DATE_FORMAT(1101),


  // User Error Codes
  INVALID_CREDENTIAL(2101),
  INVALID_TENANT_USER(2102),
  INVALID_TOKEN(2103),
  EXPIRED_TOKEN(2104);


  @Getter
  private final Integer code;

  APIErrorCodes(Integer intCode) {
    this.code = intCode;
  }

  public String getMessage() {
    MessageResourceHandler bundle = new MessageResourceHandler(Locale.ENGLISH);
    return bundle.getApplicationMessage(this.name());
  }

  /**
   * Prepare error message for the apiErrorCode from resource bundle
   * 
   * @param resourceHandler
   * @param apiErrorCode
   * @param paramList
   * @return
   */
  public static String getMessage(APIErrorCodes apiErrorCode, String... paramList) {
    MessageResourceHandler bundle = new MessageResourceHandler(Locale.ENGLISH);
    String errorMessage = bundle.getApplicationMessage(apiErrorCode.name());
    return createMessageWithParameters(errorMessage, paramList);
  }

  /**
   * Method will replace dynamic parameters from the message with the param values
   * 
   * @param paramList
   * @return
   */
  public String createMessageWithParams(String... paramList) {
    String errorMessage = getMessage();
    return createMessageWithParameters(errorMessage, paramList);
  }

  /**
   * Method will replace dynamic parameters from the message with the param values
   * 
   * @param errorMessage
   * @param paramList
   * @return
   */
  public static String createMessageWithParameters(String errorMessage, String... paramList) {
    if (paramList != null && paramList.length > 0) {
      Pattern pattern = Pattern.compile("\\{(\\d+)([^\\}.]*)\\.\\.(n?)(\\d*)\\}", Pattern.DOTALL);
      Matcher matcher = pattern.matcher(errorMessage);
      if (matcher.find()) {
        int offset = Integer.parseInt(matcher.group(1));
        String sep = matcher.group(2);
        int length = ("n".equals(matcher.group(3)) ? paramList.length
            : Math.min(paramList.length, Integer.parseInt(matcher.group(4)) - offset + 1));
        StringBuilder sb = new StringBuilder();
        for (int i = offset; i < length + offset; i++) {
          if (i > offset) {
            sb.append(sep);
          }
          sb.append("{").append(i).append("}");
        }
        errorMessage = matcher.replaceAll(sb.toString());
      }
      // It will replace "{0}, {1},{2}... {n}" number of parameter from
      // error message
      for (int i = 0; i < paramList.length; i++) {
        String param = paramList[i];
        errorMessage = errorMessage.replace("{" + i + "}", param);
      }
    }
    return errorMessage;
  }

}
