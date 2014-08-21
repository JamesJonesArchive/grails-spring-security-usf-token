/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.usf.cims.token

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.apache.commons.logging.LogFactory
import grails.converters.*
import javax.servlet.ServletException
/**
 *
 * @author james
 */
class UsfTokenAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final logger = LogFactory.getLog(this)
    def loginUrl
    def webappId
    
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,AuthenticationException authException ) throws IOException, ServletException {
        String contentType = request.getContentType();
        logger.info(contentType);
        // response.sendError( HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized" );
        
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        def errorResp = [
            tokenService: [
                loginUrl,
                { aMap ->
                    def encode = { URLEncoder.encode( "$it".toString() ) }
                    return aMap.collect { encode(it.key) + '=' + encode(it.value) }.join('&')
                }.call([
                    service: webappId,
                    redirectURL: request.getHeader("referer")
                ])
            ].join('?')
        ]
        response.getWriter().write((errorResp as JSON).toString());
    }
}

