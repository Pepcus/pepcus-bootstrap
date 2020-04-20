package com.pepcus.apps.utils;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import com.pepcus.apps.constant.ApplicationConstants;
import com.pepcus.apps.model.AppAuthData;

/**
 * Helper class to deal with HttpRequest object for application
 * 
 * @author Sandeep.Vishwakarma
 *
 */
public class RequestUtil {

  /**
   * Fetch request object from RequestContextHolder
   * 
   * @return
   */
  public static HttpServletRequest getRequest() {
    ServletRequestAttributes servletReqAttr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    if (servletReqAttr != null) {
      return servletReqAttr.getRequest();
    }
    return null;
  }

  /**
   * To set request attributes
   * 
   * @param attributeName
   * @param attributeValue
   */
  public static void setRequestAttribute(String attributeName, Object attributeValue) {
    HttpServletRequest request = getRequest();
    if (request != null) {
      request.setAttribute(attributeName, attributeValue);
    }
  }

  /**
   * To fetch attribute value from request for given attribute name
   * 
   * @param attributeName
   * @return
   */
  public static Object getRequestAttribute(String attributeName) {
    HttpServletRequest request = getRequest();
    Object attrVal = null;
    if (request != null) {
      return request.getAttribute(attributeName);
    }
    return attrVal;
  }

  /**
   * Get Request Headers Map
   * 
   * @param request
   * @return
   */
  public static Map<String, String> getRequestHeaders() {

    HttpServletRequest request = getRequest();
    Map<String, String> headers = new HashMap<String, String>();
    Enumeration<String> headerNames = request.getHeaderNames();
    Collections.list(headerNames).forEach(h -> headers.put(h, request.getHeader(h)));

    return headers;
  }

  /**
   * @return
   */
  public static AppAuthData getAppAuthData() {
    return (AppAuthData) getRequestAttribute(ApplicationConstants.APP_AUTH_DATA);
  }


}
