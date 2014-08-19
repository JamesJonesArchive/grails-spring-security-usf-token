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

import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUser;
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
/**
 * Allows for adding attributes from the Token 
 * 
 * New elements can be defined and passed from the user details service
 * 
 * Some values are always true so they don't need to be passed:
 *   enabled
 *   accountNonExpired
 *   credentialsNonExpired
 *   accountNonLocked
 * @author james
 */
class UsfTokenUserDetails extends GrailsUser {
    // Custom elements go in here like:
    final Map attributes
    /**
     * When using Usf Tokens, the password attribute of the User object means nothing.
     */
    private static final String NON_EXISTENT_PASSWORD_VALUE = "NO_PASSWORD";
        
    UsfTokenUserDetails(String username,Collection<GrantedAuthority> authorities,Map attributes) {
        super(
            username, 
            NON_EXISTENT_PASSWORD_VALUE, 
            true, 
            true,
            true,
            true,
            authorities, 
            username
        )
        this.attributes = attributes
    }
}


