package com.pepcus.apps.validators;

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
