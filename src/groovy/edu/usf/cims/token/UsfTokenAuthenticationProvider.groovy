/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.usf.cims.token

import org.springframework.security.*
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.providers.*
import org.springframework.security.userdetails.*
/**
 *
 * @author james
 */
class UsfTokenAuthenticationProvider implements AuthenticationProvider {
    def userDetailsService
    def validateUrl
    def webappId
    def usfTokenService
    
    // private TicketValidator ticketValidator;
    Authentication authenticate(Authentication customAuth) {
        def userDetails = userDetailsService.loadUserByUsername(customAuth.principal)
        // Verify Token???
        if(userDetails.attributes.containsKey('jwt')?usfTokenService.validate(validateUrl,webappId,userDetails.attributes['jwt']):false) {
            // customAuth.authorities = userDetails.authorities
            return customAuth
        } 
        return null
    }

    boolean supports(Class authentication) {
        return UsfTokenAuthenticationProvider.class.isAssignableFrom(authentication)
    }
    
}

