package com.pepcus.apps.security.permission;

import static com.pepcus.apps.constant.ApplicationConstants.API_ACCESS_TOKEN;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerExceptionResolver;
import com.pepcus.apps.constant.ApplicationConstants;
import com.pepcus.apps.model.AppAuthData;
import com.pepcus.apps.security.oauth2.services.AppTokenEnhancer;
import com.pepcus.apps.utils.RequestUtil;

/**
 * Filter used to collect data from authorization token required for application. for role based
 * access control
 * 
 * @author Sandeep.Vishwakarma
 *
 */
public class AppAuthDataCollectFilter extends BasicAuthenticationFilter {

  private HandlerExceptionResolver resolver;
  
  private AppTokenEnhancer tokenEnhancer;

  
  public AppAuthDataCollectFilter(AuthenticationManager authenticationManager,
      AppTokenEnhancer tokenEnhancer,
      HandlerExceptionResolver resolver) {
    super(authenticationManager);
    this.tokenEnhancer = tokenEnhancer;
    this.resolver = resolver;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    try {
      Object tokenObj = request.getAttribute(OAuth2AuthenticationDetails.ACCESS_TOKEN_VALUE);
      if (tokenObj != null) {
        String token = (String) tokenObj;
        if (StringUtils.isNotBlank(token)) {
          // Setting accessToken to request attribute for use in Handbook Get Content API.
          request.setAttribute(API_ACCESS_TOKEN, token);
          AppAuthData appAuthData = tokenEnhancer.getAppAuthData(token);

          // Setting data to request attributes which we can fetch in future
          RequestUtil.setRequestAttribute(ApplicationConstants.APP_AUTH_DATA, appAuthData);
        }
      }
    } catch (Exception ex) {
      resolver.resolveException(request, response, null, ex);
      return;
    }
    chain.doFilter(request, response);
  }

  /**
   * Build Authority for permission
   * 
   * @param permissionList
   * @return
   */
  public static Collection<? extends GrantedAuthority> getAuthorities(List<String> permissionList) {
    List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    if (!CollectionUtils.isEmpty(permissionList)) {
      permissionList.forEach(permission -> {
        if (StringUtils.isNotBlank(permission))
          authorities.add(new SimpleGrantedAuthority(permission));
      });
    }
    return authorities;
  }

}
