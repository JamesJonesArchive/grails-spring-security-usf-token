/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.usf.cims.token

import org.springframework.security.authentication.AbstractAuthenticationToken
/**
 *
 * @author james
 */
class UsfAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 1L;
    private final Object principal;
    Collection  authorities;
    String token
    /**
    * Holds the API token, passed by the client via a custom HTTP header
    */
    public UsfAuthenticationToken( String jwtToken) {
        super(null);
        super.setAuthenticated(true); // must use super, as we override
        token = jwtToken
        println "token anyone"
        // Loads userdetails in for the principal and details
        principal = loadUserByToken(token)
        // Details gets a copy of the UserDetails object as well
        details = principal
        authorities = (Collection) principal.getAuthorities() 
    }
 
    @Override
    public Object getCredentials() {
        return token;
    }
 
    @Override
    public Object getPrincipal() {
        return principal;
    }
     
}

