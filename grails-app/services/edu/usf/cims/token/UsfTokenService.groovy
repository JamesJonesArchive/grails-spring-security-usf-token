package edu.usf.cims.token

import grails.converters.*

class UsfTokenService {
    static transactional = false
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
}
