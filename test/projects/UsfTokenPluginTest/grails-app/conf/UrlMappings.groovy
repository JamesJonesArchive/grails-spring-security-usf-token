class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }

        "/services/tokenTest"(controller:"tokenTest",parseRequest: true){ 
            action = [GET:"getUsername", PUT:"error", DELETE:"error", POST:"error", OPTIONS: "getOptions"] 
        } 
        "/services/tokenTest/attributes"(controller:"tokenTest",parseRequest: true){ 
            action = [GET:"getAttributes", PUT:"error", DELETE:"error", POST:"error", OPTIONS: "getOptions"] 
        } 
        "/services/tokenTest/eppa"(controller:"tokenTest",parseRequest: true){ 
            action = [GET:"getEppa", PUT:"error", DELETE:"error", POST:"error", OPTIONS: "getOptions"] 
        } 
        "/"(uri:"/index.html")
        // "/"(view:"/index")
        "500"(view:'/error')
    }
}
