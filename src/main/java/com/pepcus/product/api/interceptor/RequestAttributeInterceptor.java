package com.pepcus.product.api.interceptor;

import static com.pepcus.product.api.constant.ApplicationConstant.JOB_ID_PARAM;
import java.util.Locale;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import com.pepcus.product.api.constant.ApplicationConstant;
import com.pepcus.product.api.utils.DateTimeUtils;

/**
 * Intercepter to validate Tenant & Set Tenant Locale in request
 * 
 * TODO : We will need to read this from JWT Token once Auth framework is ready 
 * 
 */
public class RequestAttributeInterceptor extends HandlerInterceptorAdapter{
  
  @Value("${language}")
  String language;
  
  
  private static final Logger logger = LoggerFactory
      .getLogger(RequestAttributeInterceptor.class);


  @Override
  public boolean preHandle(HttpServletRequest request,
      HttpServletResponse response, Object handler) throws Exception {
    
    
    String jobId = UUID.randomUUID().toString();
    MDC.put(JOB_ID_PARAM, jobId);
    
    if(request.getHeader(ApplicationConstant.KEY) != null ){
      Locale locale = new Locale(language);
      request.setAttribute(ApplicationConstant.LOCALE, locale);
      request.setAttribute(JOB_ID_PARAM, jobId);
      
      logger.info("Request JobId/RequestReferenceId:: " + jobId);
      logger.info("Request URL::" + request.getRequestURL().toString());
      logger.info("API execution Start Time ::" + DateTimeUtils.getTodayInUTC());
    }
    
    return true; 
  }

}
