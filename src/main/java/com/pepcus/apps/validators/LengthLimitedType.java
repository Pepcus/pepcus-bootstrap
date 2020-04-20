package com.pepcus.apps.validators;

/**
 * LengthLimitedType class to hold size related attributes for validation
 * 
 * @author Sandeep.Vishwakarma
 *
 */
class LengthLimitedType extends ValidationType {

  private int sizeLimited;

  public LengthLimitedType(int code) {
    super(code);
  }

  public int getSizeLimited() {
    return sizeLimited;
  }

  public void setSizeLimited(int sizeLimited) {
    this.sizeLimited = sizeLimited;
  }

}
