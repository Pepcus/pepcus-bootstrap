package com.pepcus.product.api.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import com.pepcus.product.api.constant.ApplicationConstant;


/**
 * Utility class for Date & Time
 * 
 */
public class DateTimeUtils {


  /**
   * Method to convert date from String to given date format
   * 
   * @param stringDate
   * @param format
   * @return
   */
  public static Date getDate(String stringDate, String format) {
    Date date = null;
    SimpleDateFormat sdf = new SimpleDateFormat(format);
    try {
      date = sdf.parse(stringDate);
    } catch (ParseException e) {
      // nothing to do
    }
    return date;
  }
  
  /**
   * This will return current date and time in UTC
   * 
   * @return
   */
  public static String getTodayInUTC() {
    DateTimeFormatter format = DateTimeFormatter.ofPattern(ApplicationConstant.DATE_TIME_PATTERN);
    ZonedDateTime utcDateTime = ZonedDateTime.now(ZoneOffset.UTC);
    return format.format(utcDateTime);
}


}
