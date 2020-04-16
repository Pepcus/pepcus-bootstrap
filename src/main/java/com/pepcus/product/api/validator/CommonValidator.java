package com.pepcus.product.api.validator;

import static com.pepcus.product.api.constant.ApplicationConstant.DATE_PATTERN_DD_MM_YYYY;
import static com.pepcus.product.api.exception.ApplicationException.createBadRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.pepcus.product.api.exception.APIErrorCodes;

public class CommonValidator {

  /**
   * Validate date & date format (dd/MM/yyyy)
   * 
   * @param propName
   * @param value
   */
  public static void validateDate(String propName, String value) {
    if ((!validateDateFormat(value)) || !isValidDate(value, DATE_PATTERN_DD_MM_YYYY)) {
      throw createBadRequest(APIErrorCodes.INVALID_DATE_FORMAT, propName, DATE_PATTERN_DD_MM_YYYY);
    }
  }

  /**
   * Validate date format as (dd/MM/yyyy)
   * 
   * @param date
   * @return
   */
  private static boolean validateDateFormat(String date) {
    return (date.matches("\\d{2}/\\d{2}/\\d{4}")) ? true : false;
  }

  /**
   * Validate date for given date format
   * 
   * @param dateStr
   * @param format
   * @return
   */
  private static boolean isValidDate(String dateStr, String format) {
    boolean isValidDate = false;
    try {
      SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
      simpleDateFormat.setLenient(false);
      simpleDateFormat.parse(dateStr);
      isValidDate = true;
    } catch (ParseException e) {
      isValidDate = false;
    }
    return isValidDate;
  }
}
