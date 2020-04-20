package com.pepcus.apps.constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class to keep all the constants used by application
 * 
 * @author Sandeep.Vishwakarma
 *
 */
public class ApplicationConstants {

  private ApplicationConstants() {

  }

  // GENERIC CONSTANTS
  public static final int DEFAULT_OFFSET = 0;
  public static final int DEFAULT_LIMIT = 50;
  public static final String DESENDING = "-";
  public static final String ASCENDING = "+";
  public static final String SUCCESS_DELETED = "SUCCESSFULLY_DELETED";
  public static final String SUCCESS_DEACTIVATED = "SUCCESSFULLY_DEACTIVATED";
  public static final String SUCCESS_ACTIVATED = "SUCCESSFULLY_ACTIVATED";
  public static final String TOTAL_RECORDS = "totalRecords";
  public static final String LIMIT_PARAM = "limit";
  public static final String OFFSET_PARAM = "offset";
  public static final String SORT_PARAM = "sort";


  // Date Format
  public static final String DATE_FORMAT_DD_MM_YYYY = "dd-MM-yyyy";
  public static final String VALID_FORMAT_YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
  public static final String VALID_ISO_8601_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssXXX";
  public static final String VALID_ISO_8601_DATE_FORMAT_1 = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
  public static final String VALID_ISO_8601_DATE_DISPLAY_FORMAT = "yyyy-MM-dd'T'HH:mm:ss+|-hh:mm";
  public static final String MONTH_DATE_YEAR_FORMAT_HANDBOOK = "MMMM dd, yyyy";
  public static final String DATE_PATTERN = "yyyy-MM-dd";

  public static final String PATTERN_V1 = "/v1/**";


  public static final String DOT = ".";
  public static final String DATE_RANGE_SEPARATOR = "-";
  public static final String SLASH = "/";
  public static final String UTF8 = "UTF-8";
  public static final String DEFAULT_PASSWORD = "";
  public static final String UNDERSCORE = "_";
  public static final String UNDERSCORE_ESC = "\\_";
  public static final String EMPTY_STRING = "";
  public static final String NULL_STRING = "null";


  // Default Regex
  public static final String EMAIL_PATTERN =
      "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


  // Constants for encryption/decryption
  public static final String AES_PKC_PADDING = "AES/CBC/PKCS5PADDING";
  public static final String AES_ALGO = "AES";
  public static final String BCRYPT_ALGO = "BCrypt";
  public static final String BLOWFISH_PKC_PADDING = "Blowfish/CBC/PKCS5PADDING";



  // Constants for JWT token
  public static final String JWT_TOKEN_TENANT_KEY = "tenantKey";
  public static final String JWT_TOKEN_USER = "user";
  public static final String JWT_TOKEN_ROLE = "role";
  public static final String JWT_TOKEN_SUB = "sub";
  public static final String JWT_TOKEN_ISS = "iss";


  // Constants for request header
  public static final String AUTHORIZATION_HEADER = "Authorization";
  public static final String CONTENT_TYPE_HEADER = "Content-Type";
  public static final String BEARER_TOKEN = "Bearer ";


  public static final String VALUE_ZERO = "0";
  public static final String VALUE_ONE = "1";
  public static final String VALUE_TRUE = "true";
  public static final String VALUE_FALSE = "false";


  public static final String PERMISSION_PREFIX = "restapis";
  public static final String CHILD_LEVEL_PERMISSION = "child";
  public static final String SELF_ACCESS_PERMISSION = "self";



  // Constants for OAuth2
  public static final String REFRESH_TOKEN_GRANT_TYPE = "refresh_token";
  public static final String RESOURCE_OWNER_GRANT_TYPE = "password";
  public static final String IMPLICIT_GRANT_TYPE = "implicit";
  public static final String AUTHORIZATION_CODE_GRANT_TYPE = "authorization_code";
  public static final String OPEN_ID_GRANT_TYPE = "openid";
  public static final String IMPERSONATE_GRANT_TYPE = "impersonate";


  // Constants for privileges to access resource
  public static final String SCOPE_READ = "read";
  public static final String SCOPE_WRITE = "write";
  public static final String SCOPE_ALL = "all";


  // Constants for user role
  public static final String ROLE_TYPE_SYSTEM_SDMIN = "SYSTEM_ADMINISTRATOR";
  public static final String ROLE_TYPE_ADMIN = "ADMINISTRATOR";
  public static final String ROLE_TYPE_USER = "USER";



  // Search Util
  public static final String SEARCH_SPEC = "searchSpec";
  public static final String SPACE = " ";
  public static final String SMALL_OPEN_BRACKET = "(";
  public static final String CLOSE_OPEN_BRACKET = ")";
  public static final String COMMA_SEPARATOR = ",";
  public static final String REGEX_FOR_DOUBLE_QUOTES = ",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)";
  public static final String REGEX_FOR_PROJECTION_FIELDS = ",(?![^\\(\\[]*[\\]\\)])";
  public static final String DOUBLE_QUOTE = "\"";
  public static final String QUERY_SEPARATOR = "?";
  public static final String LESS_THAN = "[lt]";
  public static final String GREATER_THAN = "[gt]";
  public static final String LESS_THAN_EQUALS = "[lte]";
  public static final String GREATER_THAN_EQUALS = "[gte]";
  public static final String EQUALS_OPERATOR = "[eq]";
  public static final List<String> LIST_OPERATORS = new ArrayList<String>(
      Arrays.asList(LESS_THAN, GREATER_THAN, LESS_THAN_EQUALS, GREATER_THAN_EQUALS, EQUALS_OPERATOR));
  public static final String LAST_UPDATED_ATTRIBUTE = "lastUpdated";
  public static final String OPERATOR_START = "[";
  public static final List<String> OPERATOR_BASED_ATTRIBUTES =
      new ArrayList<String>(Arrays.asList(LAST_UPDATED_ATTRIBUTE));


  public static final String REQUEST_PARAMETERS = "REQUEST_PARAM";
  public static final String APP_AUTH_DATA = "appAuthData";

  public static final String API_VERSION = "apiVersion";
  public static final String STATUS = "status";
  public static final String CODE = "code";
  public static final String TIMESTAMP = "timestamp";
  public static final String MESSAGE = "message";
  public static final String API_ERROR_CODE = "errorCode";
  public static final String API_EXCEPTION_DETAIL = "exceptionDetail";

  public static final String ERROR_STATUS_CODE = "javax.servlet.error.status_code";

  public static final String API_ACCESS_TOKEN = "ACCESS_TOKEN_VALUE";
  public static final String LANGUAGE_EN = "en";
  public static final String LANGUAGE_ES = "es";



}
