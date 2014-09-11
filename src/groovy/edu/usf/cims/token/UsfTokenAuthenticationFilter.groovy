/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.usf.cims.token

import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.web.util.NestedServletException
import java.text.MessageFormat
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
import org.springframework.security.core.context.SecurityContextHolder
import edu.usf.cims.token.UsfTokenAuthenticationException
/**
 *
 * @author james
 */
class UsfTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final logger = LogFactory.getLog(this)
    def tokenHeader
    def authenticationSuccessHandler
    def authenticationFailureHandler
    public UsfTokenAuthenticationFilter(String defaultFilterProcessesUrl) {
        super(defaultFilterProcessesUrl);
        setAuthenticationManager(new UsfTokenAuthenticationManager());
    }    
    /**
     * Attempt to authenticate request - basically just pass over to another method to authenticate request headers
     */
    @Override public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        String token = request.getHeader(tokenHeader)
        logger.info("token found:"+token)
        if(token==null) {
            return null;
        }
        UsfAuthenticationToken userAuthenticationToken
        try {
            userAuthenticationToken = new UsfAuthenticationToken(token)
        } catch (Exception e) {
            logger.error("Authenticate user by token error: " + e.getMessage());
            println "Authenticate user by token error: " + e.getMessage();
            throw new UsfTokenAuthenticationException(e.getMessage())
        }
        if(userAuthenticationToken == null) throw new AuthenticationServiceException(MessageFormat.format("Error | {0}", "Bad Token"));
        return this.getAuthenticationManager().authenticate(userAuthenticationToken)
    }
    
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,Authentication authResult) throws IOException, ServletException {
        SecurityContextHolder.getContext().setAuthentication(authResult)
    }
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,HttpServletResponse response,AuthenticationException failed) {
        SecurityContextHolder.clearContext()
        logger.debug('JWT Token-based authentication failed: ' + failed.toString())
        authenticationFailureHandler.onAuthenticationFailure(request,response,failed)  
    }
    public void doFilter(ServletRequest req, ServletResponse res,FilterChain chain) throws IOException, ServletException {
        println "Is this filter running?"
        HttpServletRequest request = (HttpServletRequest) req
        HttpServletResponse response = (HttpServletResponse) res
        String token = request.getHeader(tokenHeader)
        /**
         * If token doesn't exist OR is invalid, send back to filters
         **/
        if(!token) {
            chain.doFilter(request, response)
            return                    
        }
            
        logger.debug('Request requires jwt-token-based authentication')

        Authentication authentication

        try {
            authentication = attemptAuthentication(request,response)
            if(!authentication) {
                return
            }
            println "Successful Auth"            
            successfulAuthentication(request, response, authentication)
            chain.doFilter(request, response)
        } catch(AuthenticationException failed) {
            unsuccessfulAuthentication(request, response, failed)
            return
        }
    }
}



