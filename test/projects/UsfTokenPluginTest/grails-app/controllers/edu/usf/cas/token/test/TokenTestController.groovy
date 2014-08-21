package edu.usf.cas.token.test

import grails.converters.*

class TokenTestController {
    def usfTokenService
    
    def getUsername() { 
        def username = [username:'james']
        // def username = [username:usfTokenService.getUsername()]
        withFormat {
            html {
                return username
            }
            xml {
                render username as XML
            }
            json {
                JSON.use("deep") { render username as JSON }
            }
        }
    
    }
    def getAttributes(){
        def attributes = [attributes:usfTokenService.getAttributes()]
        withFormat {
            html {
                return attributes
            }
            xml {
                render attributes as XML
            }
            json {
                JSON.use("deep") { render attributes as JSON }
            }
        }
    }
    def getEppa() {
        def eppa = [eppa:usfTokenService.getEppa()]
        withFormat {
            html {
                return eppa
            }
            xml {
                render eppa as XML
            }
            json {
                JSON.use("deep") { render eppa as JSON }
            }
        }
    }
}
