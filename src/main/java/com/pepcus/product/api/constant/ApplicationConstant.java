package com.pepcus.product.api.constant;

/**
 * Class to hold all Application Constants
 */
public class ApplicationConstant {
  
  /**
   * Private Constructor 
   */
  private ApplicationConstant(){}

  //Date
  public static final String DATE_PATTERN = "yyyy-MM-dd";
  public static final String DATE_PATTERN_DD_MM_YYYY = "dd/MM/yyyy";
  public static final String ISO_8601_DATE = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
  public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";

  //Default Values
  public static final String URL_SEPARATOR = "/";
  public static final String JOB_ID_PARAM = "jobId";

  //Application Configuration Constants
  public static final String LOCALE = "locale";
  public static final String FILTERS = "filters";
  public static final String TOP_FIELDS = "topFields";
  public static final String LANGUAGE = "language";
  public static final String COUNTRY = "country";

  public static final String KEY = "key";
  
  // File Upload Constants
  public static final String COMMA_SEPARATOR = ",";
  public static final String NEW_LINE = "\n";
  public static final String DOUBLE_QUOTE = "\"";
  public static final String UNDERSCORE = "_";
  public static final String REGEX_FOR_DOUBLE_QUOTES = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";

  

}
