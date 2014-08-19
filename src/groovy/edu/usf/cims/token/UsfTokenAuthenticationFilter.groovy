/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.usf.cims.token

import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.web.util.NestedServletException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.apache.commons.logging.LogFactory
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.authentication.LockedException
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
// import org.springframework.security.web.util.matcher.AntPathRequestMatcher
/**
 *
 * @author james
 */
class UsfTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final logger = LogFactory.getLog(this)
    public UsfTokenAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
        // super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(defaultFilterProcessesUrl));
        setAuthenticationManager(new UsfTokenAuthenticationManager());
        // setAuthenticationSuccessHandler(new AuthenticationSuccessHandler());
    }
    
    public final String SECURITY_TOKEN_HEADER = "X-Auth-Token";
    
    /**
     * Attempt to authenticate request - basically just pass over to another method to authenticate request headers
     */
    @Override public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String token = request.getHeader(SECURITY_TOKEN_HEADER);
        logger.info("token found:"+token);
        AbstractAuthenticationToken userAuthenticationToken = authUserByToken(token);
        if(userAuthenticationToken == null) throw new AuthenticationServiceException(MessageFormat.format("Error | {0}", "Bad Token"));
        return userAuthenticationToken;
    }
    /**
     * authenticate the user based on token
     * @return
     */
    private AbstractAuthenticationToken authUserByToken(String token) {
        if(token==null) {
            return null;
        }
        AbstractAuthenticationToken authToken = new UsfAuthenticationToken(token);
        try {
            return authToken;
        } catch (Exception e) {
            logger.error("Authenticate user by token error: ", e);
        }
        return authToken;
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult);

        getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }
    @Override
    public void doFilter(ServletRequest req, ServletResponse res,FilterChain chain) throws IOException, ServletException {
        super.doFilter(req, res, chain);
    }
}



