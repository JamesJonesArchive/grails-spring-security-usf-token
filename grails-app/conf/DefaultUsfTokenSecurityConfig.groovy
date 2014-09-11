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
security {
    token {
        webappId = null // must have a registered webapp ID
        sharedSecret = null // must have the registered shared secret
        serverUrlPrefix = null // must be set, e.g. 'http://localhost:9090/cas'
        loginUri = null // must be set, e.g. '/login'
        validateUri = null // must be set, e.g. '/validate'
        filterProcessesUrl = null // must be set, e.g. '/j_spring_token_security_check'"
        authorityAttribute = null // must be set, e.g. 'eduPersonEntitlement'
        usernameAttribute = null // must be set, e.g. 'sub'
        tokenHeader = null // must be set, e.g. 'X-Auth-Token'
        key = 'grails-spring-security-usf-token'
    }
}
