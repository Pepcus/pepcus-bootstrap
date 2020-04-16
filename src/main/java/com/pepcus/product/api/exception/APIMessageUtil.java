package com.pepcus.product.api.exception;

import static com.pepcus.product.api.exception.APIErrorCodes.createMessageWithParameters;


/**
 * Utility to handle API's response messages using resource bundles 
 * 
 */
public class APIMessageUtil {

    /**
     * Prepare error message for the apiErrorCode from resource bundle
     * It will replace all dynamic arguments from error message by paramList values 
     * E.g. 
     *   '{0}' is a required parameter and paramList = userName then output of method will be as 
     *   'userName' is a required parameter.
     * @param apiErrorCode
     * @param paramList
     */
    public static String getMessageFromResourceBundle(MessageResourceHandler resourceHandler, APIErrorCodes apiErrorCode, String...paramList) {
        String errorMessage = resourceHandler.getApplicationMessage(apiErrorCode.name());
        return createMessageWithParameters(errorMessage, paramList);
    }

    /**
     * To fetch message from resource bundle for given message key and replace dynamic parameters with passed parameter values
     * 
     * @param resourceHandler
     * @param messageKey
     * @param paramList
     * @return
     */
    public static String getMessageFromResourceBundle(MessageResourceHandler resourceHandler, String messageKey, String...paramList) {
        String errorMessage = resourceHandler.getApplicationMessage(messageKey);
        return createMessageWithParameters(errorMessage, paramList);
    }
   
}
