package com.pepcus.product.api.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Data;


/**
 * Error Object
 */
@Data
@JsonInclude(Include.NON_NULL)
public class ErrorObject {

  private Integer errorCode;
  private String message;

  public ErrorObject() {}

  public ErrorObject(Integer errorCode, String message) {
    this.errorCode = errorCode;
    this.message = message;
  }

}
