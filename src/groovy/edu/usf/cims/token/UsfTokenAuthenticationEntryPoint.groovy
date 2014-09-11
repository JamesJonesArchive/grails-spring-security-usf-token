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
import org.springframework.security.web.util.ELRequestMatcher
import org.springframework.security.web.util.RequestMatcher
import org.apache.commons.logging.LogFactory
import grails.converters.*
import javax.servlet.ServletException
import java.util.LinkedHashMap
import java.util.Map
import java.util.regex.Pattern
/**
 *
 * @author james
 */
class UsfTokenAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final logger = LogFactory.getLog(this)
    private final Map<String, String> optionsHeaders = new LinkedHashMap<String, String>();
    private static final RequestMatcher requestMatcher = new ELRequestMatcher("hasHeader('X-Requested-With','XMLHttpRequest')");
    def loginUrl
    def webappId
    def cors
    
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,AuthenticationException authException ) throws IOException, ServletException {
        println "Running the UsfTokenAuthenticationEntryPoint"
        String contentType = request.getContentType();
        logger.info(contentType);
        // response.sendError( HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized" );
        if(isPreflight(request)){
            def headers = [:]
            String origin = request.getHeader("Origin");
            if (origin != null) {
                //must have origin; per W3C spec, supposed to terminate further processing for both preflight and actual requests. We'll just skip adding headers
                //return false;
                boolean originMatches = true
                if(cors?.allow?.origin?.regex) {
                    originMatches = Pattern.compile(cors.allow.origin.regex, Pattern.CASE_INSENSITIVE).matcher(origin).matches();
                } 
                // cors.headers = ['Access-Control-Allow-Origin': '*']
                if(originMatches) {
                    if(cors?.headers) {
                        if(!cors.headers.collect{k,v -> k.toLowerCase()}.contains('access-control-allow-origin')) {
                            response.addHeader('Access-Control-Allow-Origin', origin)
                        }
                        if(!cors.headers.collect{k,v -> k.toLowerCase()}.contains('access-control-allow-credentials')) {
                            response.addHeader('Access-Control-Allow-Credentials', "true")
                        }
                        if(!cors.headers.collect{k,v -> k.toLowerCase()}.contains('access-control-allow-headers')) {
                            response.addHeader('Access-Control-Allow-Headers', "origin, authorization, accept, content-type, x-requested-with")
                        }
                        if(!cors.headers.collect{k,v -> k.toLowerCase()}.contains('access-control-allow-methods')) {
                            response.addHeader('Access-Control-Allow-Methods', "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS")
                        }
                        if(!cors.headers.collect{k,v -> k.toLowerCase()}.contains('access-control-max-age')) {
                            response.addHeader('Access-Control-Max-Age', "3600")
                        }
                        cors.headers.each{ k,v -> 
                            response.addHeader(k, v)
                        }                        
                    } else {
                        response.addHeader('Access-Control-Allow-Origin', origin)
                        response.addHeader("Access-Control-Allow-Credentials", "true");
                        response.addHeader("Access-Control-Allow-Headers", "origin, authorization, accept, content-type, x-requested-with");
                        response.addHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, DELETE, TRACE, OPTIONS");
                        response.addHeader("Access-Control-Max-Age", "3600");
                    }
                    if(cors?.expose?.headers) {
                        resp.addHeader("Access-Control-Expose-Headers", cors.expose.headers);
                    }
                }
            }
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            println "showing unauthorized 401"
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
            println errorResp as JSON
            response.getWriter().write((errorResp as JSON).toString());
        }
    }
    /**
     * Checks if this is a X-domain pre-flight request.
     * @param request
     * @return
     */
    private boolean isPreflight(HttpServletRequest request) {
        return "OPTIONS".equals(request.getMethod());
    }
    /**
     * Checks if it is a rest request
     * @param request
     * @return
     */
    protected boolean isRestRequest(HttpServletRequest request) {
        return requestMatcher.matches(request);
    }
}

