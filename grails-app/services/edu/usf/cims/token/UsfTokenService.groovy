package edu.usf.cims.token

import groovyx.net.http.RESTClient
import static groovyx.net.http.ContentType.*
import grails.converters.*
import grails.plugins.springsecurity.SpringSecurityService
import org.springframework.web.context.request.RequestContextHolder

class UsfTokenService extends SpringSecurityService {
    static transactional = false
    static scope = "singleton"
    
    def validate(String validateUrl,String webappId,String jwtoken) {
        def client = new RESTClient()
        client.ignoreSSLIssues()
        def result
        try {
            result = client.post([uri: validateUrl,body : [service:webappId,token:jwtoken],requestContentType : URLENC ])
            println "Is this running?"
            if(result?.data) {
                try {
                    return !result.data.containsKey('result')
                } catch(ex) {
                    return false
                }
            }
            return false            
        } catch(ex) {
            return false
        }
    }
        
    def getUsername(){
        super.authentication.name
    }
    def getAttributes(){
        RequestContextHolder.requestAttributes.request.userPrincipal.details.attributes
    }
    def getEppa() {
        RequestContextHolder.requestAttributes.request.userPrincipal.details.attributes.eduPersonPrimaryAffiliation
    }
}
