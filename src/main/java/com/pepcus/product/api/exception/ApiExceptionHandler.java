package com.pepcus.product.api.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


/**
 * Global Exception Handler Class
 */
@ControllerAdvice
public class ApiExceptionHandler {

  /**
   * Method to handle Application Exception
   * 
   * @param request
   * @param me
   * @return
   */
  @ExceptionHandler(ApplicationException.class)
  @ResponseStatus(BAD_REQUEST)
  @ResponseBody
  public ApiError handleBadRequestException(HttpServletRequest request, ApplicationException me) {
    return new ApiError(new ErrorObject(me.getType().getCode(), me.getMessage()));
  }


  /**
   * Method to handle Invalid Arguments
   * 
   * @param ex
   * @return
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(BAD_REQUEST)
  @ResponseBody
  public ApiError handleBadRequestException(MethodArgumentNotValidException ex) {
    return new ApiError(new ErrorObject(APIErrorCodes.BAD_REQUEST.getCode(),
        APIErrorCodes.BAD_REQUEST.createMessageWithParams(ex.getParameter().getParameterName())));
  }


  /**
   * Method to handle Argument Type mismatch
   * 
   * @param ex
   * @return
   */
  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(BAD_REQUEST)
  @ResponseBody
  public ApiError handleBadRequestException(MethodArgumentTypeMismatchException ex) {
    return new ApiError(new ErrorObject(APIErrorCodes.BAD_REQUEST.getCode(),
        APIErrorCodes.BAD_REQUEST.createMessageWithParams(ex.getName())));
  }

  /**
   * Method to handle Missing Request Header
   * 
   * @param ex
   * @return
   */
  @ExceptionHandler(MissingRequestHeaderException.class)
  @ResponseStatus(BAD_REQUEST)
  @ResponseBody
  public ApiError handleBadRequestException(MissingRequestHeaderException ex) {
    return new ApiError(new ErrorObject(APIErrorCodes.BAD_REQUEST.getCode(),
        APIErrorCodes.BAD_REQUEST.createMessageWithParams(ex.getHeaderName())));
  }



  /**
   * Method to handle Invalid method type
   * 
   * @param ex
   * @return
   */
  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  @ResponseStatus(METHOD_NOT_ALLOWED)
  @ResponseBody
  public ApiError handleBadRequestException(HttpRequestMethodNotSupportedException ex) {
    return new ApiError(
        new ErrorObject(APIErrorCodes.INVALID_METHOD_TYPE.getCode(), APIErrorCodes.INVALID_METHOD_TYPE.getMessage()));
  }


  /**
   * Method to handle Missing Request Header
   * 
   * @param ex
   * @return
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(INTERNAL_SERVER_ERROR)
  @ResponseBody
  public ApiError handleBadRequestException(Exception ex) {
    return new ApiError(
        new ErrorObject(APIErrorCodes.APPLICATION_ERROR.getCode(), APIErrorCodes.APPLICATION_ERROR.getMessage()));
  }

}
