class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?"{
            constraints {
                // apply constraints here
            }
        }

        "/services/tokenTest"(controller:"tokenTest",parseRequest: true){ 
            action = [GET:"getUsername", PUT:"addRuleSet", DELETE:"error", POST:"error"] 
        } // "/"(view:"/index")
        "/"(uri:"/index.html")
        "500"(view:'/error')
    }
}
