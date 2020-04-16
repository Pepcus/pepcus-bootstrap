package com.pepcus.product.api.exception;

import org.springframework.http.HttpStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Application Exception Object All Application Exception uses this class to create Custom Errors.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ApplicationException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private String[] errorMessageParameters;
  private String message;
  private Integer errorCode;
  private APIErrorCodes type;
  private HttpStatus httpStatus;

  public ApplicationException(String msg) {
    super(msg);
  }

  public ApplicationException(APIErrorCodes type) {
    this.type = type;
  }

  public ApplicationException(APIErrorCodes type, String message) {

    this.type = type;
    this.message = message;
  }

  public ApplicationException(APIErrorCodes type, String... params) {
    this.type = type;
    this.errorMessageParameters = params;
  }

  public static ApplicationException createBadRequest(APIErrorCodes errorCode, String... params) {
    ApplicationException appException = new ApplicationException(errorCode, errorCode.createMessageWithParams(params));
    appException.setHttpStatus(HttpStatus.BAD_REQUEST);
    return appException;
  }
  
  public static ApplicationException createBulkImportError(APIErrorCodes errorCode, String... params) {
    ApplicationException appException = new ApplicationException(errorCode, errorCode.createMessageWithParams(params));
    appException.setHttpStatus(HttpStatus.NOT_ACCEPTABLE);
    return appException;
  }
}
