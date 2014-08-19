/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.usf.cims.token

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.jwt.JwtHelper
/**
 *
 * @author james
 */
class UsfAuthenticationToken extends AbstractAuthenticationToken {
    private static final long serialVersionUID = 1L;
    private final Object principal;
    private Object details;
 
    Collection  authorities;
    String token
    
    public UsfAuthenticationToken( String jwtToken) {
        super(null);
        super.setAuthenticated(true); // must use super, as we override
        token = jwtToken

        UsfTokenUserDetailsService adapter = new UsfTokenUserDetailsService();
        
        // Loads userdetails in for the principal and details
        principal = adapter.loadUserByToken(token)
        // Details gets a copy of the UserDetails object as well
        details = principal
        authorities = (Collection) principal.getAuthorities() 
    }
 
    @Override
    public Object getCredentials() {
        return "";
    }
 
    @Override
    public Object getPrincipal() {
        return principal;
    }
 
    @Override
    public Collection getAuthorities() {
        return authorities;
    }	
    
}

