package org.apache.shiro.biz.web.filter.authz;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.biz.utils.StringUtils;
import org.apache.shiro.biz.utils.WebUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

public class ShiroPermissionsAuthorizationFilter extends PermissionsAuthorizationFilter {

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
		
		Subject subject = getSubject(request, response);
        //未认证
        if (null == subject.getPrincipal()) {
    		if (WebUtils.isAjaxRequest(request)) {
    	        WebUtils.writeJSONString(response, HttpServletResponse.SC_UNAUTHORIZED, "Unauthentication.");
    	        return false;
    		}
            saveRequestAndRedirectToLogin(request, response);
        //未授权
        } else {
        	if (WebUtils.isAjaxRequest(request)) {
    	        WebUtils.writeJSONString(response, HttpServletResponse.SC_FORBIDDEN, "Forbidden.");
    	        return false;
    		}else{
    			 // If subject is known but not authorized, redirect to the unauthorized URL if there is one
                // If no unauthorized URL is specified, just return an unauthorized HTTP status code
                String unauthorizedUrl = getUnauthorizedUrl();
                //SHIRO-142 - ensure that redirect _or_ error code occurs - both cannot happen due to response commit:
                if (StringUtils.hasText(unauthorizedUrl)) {
                    WebUtils.issueRedirect(request, response, unauthorizedUrl);
                } else {
                    WebUtils.toHttp(response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
    		}
        }
        return false;
	}

}