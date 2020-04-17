package com.pepcus.apps.api.response;

import static com.pepcus.apps.api.constant.ApplicationConstants.DEFAULT_LIMIT;
import static com.pepcus.apps.api.constant.ApplicationConstants.DEFAULT_OFFSET;
import static com.pepcus.apps.api.constant.ApplicationConstants.LIMIT_PARAM;
import static com.pepcus.apps.api.constant.ApplicationConstants.OFFSET_PARAM;
import static com.pepcus.apps.api.constant.ApplicationConstants.SORT_PARAM;
import static com.pepcus.apps.api.constant.ApplicationConstants.SUCCESS_DELETED;
import static com.pepcus.apps.api.constant.ApplicationConstants.TOTAL_RECORDS;
import static com.pepcus.apps.api.utils.APIMessageUtil.getMessageFromResourceBundle;
import static com.pepcus.apps.api.utils.RequestUtils.getRequestAttribute;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.pepcus.apps.api.db.entities.SearchableEntity;
import com.pepcus.apps.api.exception.APIError;
import com.pepcus.apps.api.exception.APIErrorCodes;
import com.pepcus.apps.api.exception.MessageResourceHandler;
import com.pepcus.apps.api.utils.EntitySearchUtil;


/**
 * This class is a single global response handler component wrapping for all 
 * the responses from APIs, prepare a wrapper over response object and put additional informations those are 
 * useful for API consumer like status and code etc.
 * 
 */
@ControllerAdvice ("com.pepcus.apps.api.controllers")
public class APIResponseBodyHandler implements ResponseBodyAdvice<Object> {

    private static Logger logger = LoggerFactory.getLogger(APIResponseBodyHandler.class);

    @Autowired
    MessageResourceHandler resourceHandler;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    /**
     * This method to identify response body and inject additional information to response. 
     * 
     *  @param body
     *  @param returnType
     *  @param selectedContenctType
     *  @param selectedConverterType
     *  @param request
     *  @param response
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
                    ServerHttpResponse response) {

        if (body instanceof APIError || body instanceof Exception || body instanceof DefaultOAuth2AccessToken) {
            return body;
        }
        ServletServerHttpRequest httpRequest = (ServletServerHttpRequest) request;
        ServletServerHttpResponse httpResponse = (ServletServerHttpResponse) response;

        APIResponse apiResponse = new APIResponse();
        if (body instanceof APIResponse) {
            apiResponse = (APIResponse)body;
        }
        int statusCode = httpResponse.getServletResponse().getStatus();
        apiResponse.setCode(String.valueOf(statusCode));
        apiResponse.setStatus(HttpStatus.valueOf(statusCode).name());
        apiResponse.setRequestReferenceId((String) httpRequest.getServletRequest().getAttribute("jobId"));
        /*
         * TODO: Currently there are references those are Company specific, we need to make them generic so 
         * the same code reference will be used by all APIs for different entities as well.
         */
        if (body instanceof List) {
            if ((List)body == null || ((List)body).isEmpty()) {
                apiResponse.setMessage(getMessageFromResourceBundle(resourceHandler, APIErrorCodes.NO_RECORDS_FOUND));
            } else {
                setListData((List) body, httpRequest, apiResponse);
            }
        } else {
            if (statusCode == HttpStatus.ACCEPTED.value()) {
                if (body instanceof Integer) {
                    apiResponse.setMessage(getMessageFromResourceBundle(resourceHandler, SUCCESS_DELETED, "id", body.toString()));
                }
            } else if (body instanceof SearchableEntity) {
                apiResponse.setSearchEntity((SearchableEntity)body);
            }
            if (body instanceof InputStreamResource || body instanceof byte[]) {
                return body;
            }
        }
        /*if (logger.isDebugEnabled()) {
			logger.debug("Request processed and response is " + apiResponse);
		}*/
        return apiResponse;
    }

    /**
     * To set list of company specific information into ApiResponse object 
     * 
     * @param list
     * @param httpRequest
     * @param apiResponse
     */
    private void setListData(List list, ServletServerHttpRequest httpRequest, APIResponse apiResponse) {

        String limit = httpRequest.getServletRequest().getParameter(LIMIT_PARAM);
        limit = StringUtils.isNotBlank(limit) ? limit : String.valueOf(DEFAULT_LIMIT);
        apiResponse.setLimit(limit);

        String offset = httpRequest.getServletRequest().getParameter(OFFSET_PARAM);
        offset = StringUtils.isNotBlank(offset) ? offset : String.valueOf(DEFAULT_OFFSET);
        apiResponse.setOffset(offset);

        String sort = httpRequest.getServletRequest().getParameter(SORT_PARAM);
        apiResponse.setSort(EntitySearchUtil.getFormattedString(sort));

        Object totalRecObj = getRequestAttribute(TOTAL_RECORDS);
        if (totalRecObj != null) {
            apiResponse.setTotalRecords(String.valueOf(totalRecObj));
        }
        apiResponse.setList(list);
    }

}
