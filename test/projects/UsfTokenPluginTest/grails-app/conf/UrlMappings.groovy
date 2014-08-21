class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }

        "/services/tokenTest"(controller:"tokenTest",parseRequest: true){ 
            action = [GET:"getUsername", PUT:"error", DELETE:"error", POST:"error"] 
        } 
        "/services/tokenTest/attributes"(controller:"tokenTest",parseRequest: true){ 
            action = [GET:"getAttributes", PUT:"error", DELETE:"error", POST:"error"] 
        } 
        "/services/tokenTest/eppa"(controller:"tokenTest",parseRequest: true){ 
            action = [GET:"getEppa", PUT:"error", DELETE:"error", POST:"error"] 
        } 
        "/"(uri:"/index.html")
        "500"(view:'/error')
    }
}
