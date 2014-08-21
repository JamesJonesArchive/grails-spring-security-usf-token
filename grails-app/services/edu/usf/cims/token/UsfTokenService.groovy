package edu.usf.cims.token

import grails.converters.*
import grails.plugins.springsecurity.SpringSecurityService
import org.springframework.web.context.request.RequestContextHolder

class UsfTokenService extends SpringSecurityService {
    static transactional = false
    static scope = "singleton"
    
    def validate(String validateUrl,String webappId,String jwtoken) {
        return withRest(uri: validateUrl) {
            return post(body : [webappId:webappId,token:jwtoken],requestContentType : 'application/x-www-form-urlencoded') { resp, reader ->
                try {
                    def json = JSON.parse(reader.toString())
                    return !json.containsKey('result')
                } catch(ex) {
                    return false
                }                
            }
        }
    }
    
    def getUsername(){
        super.authentication.name
    }
    def getAttributes(){
        RequestContextHolder.requestAttributes.request.userPrincipal.assertion.principal.attributes
    }
    def getEppa() {
        RequestContextHolder.requestAttributes.request.userPrincipal.assertion.principal.attributes.eduPersonPrimaryAffiliation
    }
}
