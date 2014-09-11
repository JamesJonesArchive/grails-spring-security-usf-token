/* Copyright 2006-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package edu.usf.cims.token

import javax.servlet.http.HttpServletRequest
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUser;
import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUserDetailsService;
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils;
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.authority.GrantedAuthorityImpl
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.security.jwt.JwtHelper
import org.springframework.security.jwt.JwtHeaderHelper
import org.springframework.security.jwt.crypto.sign.MacSigner
import com.nimbusds.jose.JWSObject
import com.nimbusds.jose.crypto.MACVerifier
import com.nimbusds.jwt.SignedJWT
import grails.converters.*
import grails.util.Holders
/**
 *
 * @author james
 */
class UsfTokenUserDetailsService implements GrailsUserDetailsService {
    /**
     * When using Usf Tokens, the password attribute of the User object means nothing.
     */
    private static final String NON_EXISTENT_PASSWORD_VALUE = "NO_PASSWORD";
	
    /**
     * When using Usf Token attributes for GrantedAuthorities, you need to append the 'ROLE_'
     * prefix for Spring Security
     */
    private static final Boolean ADD_PREFIX = true
    private static final String PREFIX = "ROLE_"
    /**
     * Some Spring Security classes (e.g. RoleHierarchyVoter) expect at least
     * one role, so we give a user with no granted roles this one which gets
     * past that restriction but doesn't grant anything.
     */
    static final List NO_ROLES = [new GrantedAuthorityImpl(SpringSecurityUtils.NO_ROLE)]
    private boolean convertToUpperCase = true
    
    /** Dependency injection for creating and finding Users **/
    def userMapper
    UserDetails loadUserByUsername(String username, boolean loadRoles) throws UsernameNotFoundException {
        return loadUserByUsername(username)
    }    
    
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loadUserByUsername()
    }
    
    UserDetails loadUserByUsername() throws UsernameNotFoundException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        
        String token = request.getHeader(tokenRequestHeader);
        return this.loadUserByToken(token)
    }
    UserDetails loadUserByToken(String token) throws UsernameNotFoundException {
        println token
        def conf = SpringSecurityUtils.securityConfig
        // Get the Signed JWT (aka: JWS)
        SignedJWT signedJWT = SignedJWT.parse(token)
        // Get the claims as a JSON Object
        def claims = signedJWT.getPayload().toJSONObject()
        // Build list of entitlements as GrantedAuthorities
        def tokenAuthorities = { ->
            if(conf.token.authorityAttribute in claims) {
                return claims[conf.token.authorityAttribute].collect { authority ->
                    return (ADD_PREFIX)?new GrantedAuthorityImpl(PREFIX + (this.convertToUpperCase?authority.toUpperCase():authority)):new GrantedAuthorityImpl((this.convertToUpperCase?authority.toUpperCase():authority))
                }
            }
            return NO_ROLES
        }.call()
        // Check to make sure there is a username in the claims
        if(!(conf.token.usernameAttribute in claims)) throw new UsernameNotFoundException('User not in Token')
        return new UsfTokenUserDetails(
            claims[conf.token.usernameAttribute],
            tokenAuthorities,
            claims.inject([jwt:token]) { ta,k,v ->
                if(!(k in ['exp','iss','aud','nbf','jti','iat'])) {
                    ta[k] = v
                }
                return ta
            }
        )
    }
}


