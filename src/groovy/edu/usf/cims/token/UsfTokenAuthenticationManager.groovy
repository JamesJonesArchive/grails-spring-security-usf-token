/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.usf.cims.token

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jwt.SignedJWT
import com.nimbusds.jose.JWSObject
import edu.usf.cims.token.UsfTokenAuthenticationException
import org.apache.commons.logging.LogFactory
/**
 *
 * @author james
 */
class UsfTokenAuthenticationManager implements AuthenticationManager {
    private static final logger = LogFactory.getLog(this)
    def userDetailsService
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        logger.info("AuthenticationManager authenticate invoked")
        // Verify the token is valid
        if (authentication.token) {
            logger.debug "Trying to validate token ${authentication.token}"
            // Run validation here
            def conf = SpringSecurityUtils.securityConfig
            SignedJWT signedJWT = SignedJWT.parse(authentication.token);
            if(!signedJWT.verify(new MACVerifier(conf.token.sharedSecret))) {
                logger.debug "ERROR: Token could not be verified!"
                throw new UsfTokenAuthenticationException("ERROR: Token could not be verified!")
                return null
            }            
            boolean validated = validate(authentication.token)
            if(!validated) {
                logger.debug "ERROR: Token could not be validated!"
                throw new UsfTokenAuthenticationException("ERROR: Token could not be validated!")
                return null
            }
            return authentication
        } else {
            throw new UsfTokenAuthenticationException("Missing token")
            return null
        }
        return authentication;
    }
}

