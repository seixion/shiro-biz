package org.apache.shiro.biz.authc.token;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 验证码支持
 * @author <a href="https://github.com/vindell">vindell</a>
 */
public interface CaptchaAuthenticationToken extends AuthenticationToken {

	String getCaptcha();
	
}
