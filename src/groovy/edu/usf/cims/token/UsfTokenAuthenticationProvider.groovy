/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.usf.cims.token

import org.springframework.security.*
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.providers.*
import org.springframework.security.userdetails.*
import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jwt.SignedJWT
import com.nimbusds.jose.JWSObject
import edu.usf.cims.token.UsfTokenAuthenticationException
import org.apache.commons.logging.LogFactory
/**
 *
 * @author james
 */
class UsfTokenAuthenticationProvider implements AuthenticationProvider {
    private static final logger = LogFactory.getLog(this)
    def userDetailsService
    
    Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Assert.isInstanceOf(UsfAuthenticationToken, authentication, "Only UsfAuthenticationToken is supported")
        logger.debug("AuthenticationProvider authenticate invoked!");
        println "running provider"
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
                return null
            }
            return authentication
        }
        return null
    }

    boolean supports(Class authentication) {
        return UsfTokenAuthenticationProvider.class.isAssignableFrom(authentication)
    }
    
}

