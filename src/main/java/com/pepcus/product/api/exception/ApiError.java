package com.pepcus.product.api.exception;

import lombok.Data;

/**
 * Error Response Object
 */
@Data
public class ApiError {

  private ErrorObject error;

  public ApiError() {}

  public ApiError(ErrorObject error) {
    this.error = error;
  }
}
