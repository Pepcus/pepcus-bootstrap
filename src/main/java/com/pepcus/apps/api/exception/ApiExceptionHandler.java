package com.pepcus.apps.api.exception;

import static com.pepcus.apps.api.utils.APIMessageUtil.getMessageFromResourceBundle;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.common.exceptions.InvalidClientException;
import org.springframework.security.oauth2.common.exceptions.UnsupportedGrantTypeException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.fasterxml.jackson.core.JsonParseException;

/**
 * This class is a single global exception handler component wrapping for all 
 * the exceptions from APIs, prepare a Response object with required exception 
 * details sending back to users.
 * 
 * @ControllerAdvice 
 * @RestController
 *
 */
@ControllerAdvice
@RestController
public class APIExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    MessageResourceHandler resourceHandler;

    /**
     * Handle MissingServletRequestParameterException when a 'required' request parameter is missing.
     *
     * @param ex      MissingServletRequestParameterException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the APIError object
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, 
            HttpHeaders headers,
            HttpStatus status, 
            WebRequest request) {
        logger.error(ex.getMessage(), ex);
        APIError apiError = new APIError(BAD_REQUEST, ex);
        apiError.setMessage(getMessageFromResourceBundle(resourceHandler, APIErrorCodes.REQUIRED_PARAMETER, ex.getParameterName()));
        return buildResponseEntity(apiError);
    }

    /**
     * Handle MissingServletRequestPartException when a 'required' request part is missing.
     *
     * @param ex      MissingServletRequestPartException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the APIError object
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {

        logger.error(ex.getMessage(), ex);
        APIError apiError = new APIError(BAD_REQUEST, ex);
        apiError.setMessage(getMessageFromResourceBundle(resourceHandler, APIErrorCodes.REQUIRED_PARAMETER, ex.getRequestPartName()));
        return buildResponseEntity(apiError);
    }

    /**
     * Handle HttpMediaTypeNotSupportedException when request JSON is invalid.
     *
     * @param ex      HttpMediaTypeNotSupportedException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the APIError object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        logger.error(ex.getMessage(), ex);
        APIError apiError = new APIError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex);
        StringBuilder builder = new StringBuilder();
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
        apiError.setMessage(getMessageFromResourceBundle(resourceHandler, APIErrorCodes.REQUIRED_PARAMETER, 
                ex.getContentType().toString(), builder.toString()));
        return buildResponseEntity(apiError);
    }

    /**
     * Handle MethodArgumentNotValidException an object fails @Valid validation.
     *
     * @param ex      the MethodArgumentNotValidException that is thrown when @Valid validation fails
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the APIError object
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        logger.error(ex.getMessage(), ex);
        APIError apiError = new APIError(BAD_REQUEST, APIErrorCodes.VALIDATION_FAILED);
        apiError.setMessage(resourceHandler.get(APIErrorCodes.VALIDATION_FAILED.name()));
        apiError.addErrorDetail(ex.getBindingResult().getFieldErrors());
        return buildResponseEntity(apiError);
    }

    /**
     * Handles javax.validation.ConstraintViolationException. Thrown when @Validated fails.
     *
     * @param ex the ConstraintViolationException
     * @return the APIError object
     */
    @ExceptionHandler(javax.validation.ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(javax.validation.ConstraintViolationException ex) {

        logger.error(ex.getMessage(), ex);
        APIError apiError = new APIError(BAD_REQUEST, APIErrorCodes.VALIDATION_FAILED, ex);
        ex.getConstraintViolations().forEach(obj -> {
            String messageTemplate = obj.getConstraintDescriptor().getMessageTemplate();
            if (StringUtils.isNotBlank(messageTemplate)) {
                apiError.setExceptionDetail(messageTemplate);
            }
        });

        apiError.addErrorDetail(ex.getConstraintViolations());
        apiError.setMessage(resourceHandler.get(APIErrorCodes.VALIDATION_FAILED.name()));
        return buildResponseEntity(apiError);
    }

    /**
     * Handles org.springframework.web.method.annotation.MethodArgumentTypeMismatchException, PropertyReferenceException
     * and IllegalArgumentException. Throws when @Validated fails
     * 
     * @param ex
     * @return
     */
    @ExceptionHandler({org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class,
        org.springframework.data.mapping.PropertyReferenceException.class,
        java.lang.IllegalArgumentException.class})
    protected ResponseEntity<Object> handleExceptions(
            Exception ex) {
        logger.error(ex.getMessage(), ex);
        APIError apiError = new APIError(BAD_REQUEST, APIErrorCodes.VALIDATION_FAILED, ex);
        if (ex instanceof MethodArgumentTypeMismatchException) {
            apiError = new APIError(BAD_REQUEST, APIErrorCodes.VALIDATION_FAILED);
            apiError.addErrorDetail((MethodArgumentTypeMismatchException)ex);
        }
        apiError.setMessage(resourceHandler.get(APIErrorCodes.VALIDATION_FAILED.name()));
        return buildResponseEntity(apiError);
    }

    /**
     * Handle HttpMessageNotWritableException.
     *
     * @param ex      HttpMessageNotWritableException
     * @param headers HttpHeaders
     * @param status  HttpStatus
     * @param request WebRequest
     * @return the APIError object
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex,
            HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error(ex.getMessage(), ex);
        APIError apiError = new APIError(HttpStatus.INTERNAL_SERVER_ERROR, APIErrorCodes.ERROR_WRITING_JSON_OUTPUT, ex);
        apiError.setMessage(resourceHandler.get(APIErrorCodes.ERROR_WRITING_JSON_OUTPUT.name()));
        return buildResponseEntity(apiError);
    }

    /**
     * Handle Exception, handle generic ApplicationException.class
     *
     * @param ex the Exception
     * @return the APIError object
     */
    @ExceptionHandler(ApplicationException.class)
    protected ResponseEntity<Object> handleAPIBadRequest(ApplicationException ex) {
        logger.error(ex.getMessage(), ex);
        APIError apiError = new APIError(ex.getHttpStatus(), ex.getApiErrorCode());
        apiError.setMessage(getMessageFromResourceBundle(resourceHandler, ex.getApiErrorCode(), ex.getErrorMessageParameters()));
        return buildResponseEntity(apiError);
    }

    /**
     * Build Resposne entity for the given error message;
     * 
     * @param apiError
     * @return
     */
    private ResponseEntity<Object> buildResponseEntity(APIError apiError) {
        logger.error("API throws an exception " + apiError);
        return new ResponseEntity<Object>(apiError, HttpStatus.valueOf(apiError.getCode()));
    }

    /**
     * Handle {@link AccessDeniedException}
     *
     * @param ex the Exception
     * @return the ApiError object
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        APIError apiError = new APIError(HttpStatus.FORBIDDEN, APIErrorCodes.ACCESS_DENIED);
        apiError.setMessage(getMessageFromResourceBundle(resourceHandler, APIErrorCodes.ACCESS_DENIED));
        return new ResponseEntity<Object>(apiError, HttpStatus.valueOf(apiError.getCode()));
    }
    
    /**
     * Handle {@link JsonParseException}
     *
     * @param ex the Exception
     * @return the ApiError object
     */
    @ExceptionHandler(JsonParseException.class)
    public ResponseEntity<Object> handleJsonParseException(JsonParseException ex) {
        logger.error(ex.getMessage(), ex);
        APIError apiError = new APIError(HttpStatus.BAD_REQUEST, ex);
        apiError.setMessage(ex.getOriginalMessage());
        return buildResponseEntity(apiError);
    }
    
    /**
     * Handle {@link InvalidClientException}
     *
     * @param ex the Exception
     * @return the ApiError object
     */
    @ExceptionHandler(InvalidClientException.class)
    public ResponseEntity<Object> handleInvalidClientException(InvalidClientException ex) {
        logger.error(ex.getMessage(), ex);
        APIError apiError = new APIError(HttpStatus.BAD_REQUEST, ex);
        apiError.setMessage(ex.getMessage());
        apiError.setExceptionDetail(ex.toString());
        return buildResponseEntity(apiError);
    }
    
    /**
     * Handle {@link UnsupportedGrantTypeException}
     *
     * @param ex the Exception
     * @return the ApiError object
     */
    @ExceptionHandler(UnsupportedGrantTypeException.class)
    public ResponseEntity<Object> handleUnsupportedGrantTypeException(UnsupportedGrantTypeException ex) {
        logger.error(ex.getMessage(), ex);
        APIError apiError = new APIError(HttpStatus.BAD_REQUEST, ex);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }
    
    /**
     * Handle {@link BadCredentialsException}
     *
     * @param ex the Exception
     * @return the ApiError object
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleBadCredentialsException(BadCredentialsException ex) {
        logger.error(ex.getMessage(), ex);
        APIError apiError = new APIError(HttpStatus.UNAUTHORIZED, ex);
        apiError.setMessage(ex.getMessage());
        return buildResponseEntity(apiError);
    }

}
