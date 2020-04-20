package com.pepcus.apps.exception;

import static com.pepcus.apps.constant.ApplicationConstants.API_ERROR_CODE;
import static com.pepcus.apps.constant.ApplicationConstants.API_EXCEPTION_DETAIL;
import static com.pepcus.apps.constant.ApplicationConstants.API_VERSION;
import static com.pepcus.apps.constant.ApplicationConstants.CODE;
import static com.pepcus.apps.constant.ApplicationConstants.ERROR_STATUS_CODE;
import static com.pepcus.apps.constant.ApplicationConstants.MESSAGE;
import static com.pepcus.apps.constant.ApplicationConstants.STATUS;
import static com.pepcus.apps.constant.ApplicationConstants.TIMESTAMP;
import static com.pepcus.apps.utils.APIMessageUtil.getMessageFromResourceBundle;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;

/**
 * Wrapper class used to wrap the exceptions related with internal server error and unauthorized
 * error.
 * 
 * @author Sandeep.Vishwakarma
 *
 */
@Component
public class APIErrorAttributes extends DefaultErrorAttributes {

  @Autowired
  MessageResourceHandler resourceHandler;

  @Override
  public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
    Map<String, Object> errorAttributes = new LinkedHashMap<String, Object>();

    Integer status = this.getAttribute(requestAttributes, ERROR_STATUS_CODE);

    if (status.equals(HttpStatus.INTERNAL_SERVER_ERROR.value())) {
      APIError apiError = new APIError(HttpStatus.INTERNAL_SERVER_ERROR, APIErrorCodes.INTERNAL_SERVER_ERROR,
          this.getError(requestAttributes));

      errorAttributes.put(API_VERSION, apiError.getApiVersion());
      errorAttributes.put(STATUS, apiError.getStatus());
      errorAttributes.put(CODE, apiError.getCode());
      errorAttributes.put(TIMESTAMP, apiError.getTimestamp());
      errorAttributes.put(API_ERROR_CODE, apiError.getErrorCode());
      errorAttributes.put(API_EXCEPTION_DETAIL, apiError.getExceptionDetail());
    } else if (status.equals(HttpStatus.NOT_FOUND.value())) {
      errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTrace);
      APIError apiError = new APIError(HttpStatus.NOT_FOUND, APIErrorCodes.URL_NOT_FOUND);
      apiError.setMessage(getMessageFromResourceBundle(resourceHandler, APIErrorCodes.URL_NOT_FOUND, ""));
      errorAttributes.put(TIMESTAMP, apiError.getTimestamp());
      errorAttributes.put(MESSAGE, apiError.getMessage());
      return errorAttributes;
    } else {
      return super.getErrorAttributes(requestAttributes, includeStackTrace);
    }

    return errorAttributes;
  }

  /**
   * To return the value for the scoped request attribute of the given name, if any.
   * 
   * @param requestAttributes
   * @param name
   * @return
   */
  private Integer getAttribute(RequestAttributes requestAttributes, String name) {
    return (Integer) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
  }

}
