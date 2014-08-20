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
}
