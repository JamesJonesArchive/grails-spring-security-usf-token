grails-spring-security-usf-token
==============================

Grails USF Token plugin customized for the University of South Florida. This
uses webtoken feature of USF AuthTransferService to translate CAS backed token 
authentication.

##Table of Contents

1. [Introduction](#introduction)
2. [Usage](#usage)
3. [Configuration](#configuration)

##Introduction

The Spring-Security-USF-Token plugin builds on the [spring-security-cas](http://grails.org/plugin/spring-security-cas) plugin. It depends on the [Spring Security Core plugin](http://grails.org/plugin/spring-security-core).

Your application must be registered on the AuthTransferService service with it's unique webappid.

Once you have configured a AuthTransferService server and have configured your Grails application(s) as clients, you can authenticate to any application that is a client of the AuthTransfer server and be automatically authenticated to all other clients.

##Usage

> **Note:** This guide assumes that you are using the webtoken feature of AuthTransferService on authtest.it.usf.edu for development and testing and webauth.usf.edu for production

There isn't much that you need to do in your application to be a CAS client. Just install this plugin, run the configuration script, and modify any of the default values you want in Config.groovy. These are described in detail in [<i class="icon-share"></i> configuration](#configuration) but in most cases, you won't need to change anything during development.

###Installation

Download the latest version of the plugin zip file from [GitHub](https://github.com/jamjon3/grails-spring-security-usf-token/raw/master/grails-spring-security-usf-token-0.1.zip), drop it into the lib directory of your Grails project and update `grails-app/conf/BuildConfig.groovy`:

```
  plugins {
        compile ":spring-security-core:1.2.7.3"
        compile ":spring-security-usf-token:0.1"
  }
```

###grails usf-token-config

After you have installed the plugin, run this command to add the necessary configuration options:

```
grails usf-token-config
```

The following lines will be added to your Config.groovy:

```
grails.plugins.springsecurity.userLookup.userDomainClassName = 'edu.usf.cims.token.UsfTokenUserDetails'
grails.plugins.springsecurity.token.serverUrlPrefix = 'https://authtest.it.usf.edu/AuthTransferService/webtoken'
grails.plugins.springsecurity.token.webappId = 'http://localhost:8080/${appName}/'
grails.plugins.springsecurity.token.loginUri = '/login'
grails.plugins.springsecurity.token.validateUri = '/validate'
grails.plugins.springsecurity.token.filterProcessesUrl = '/j_spring_token_security_check'
grails.plugins.springsecurity.token.authorityAttribute = 'eduPersonEntitlement'
grails.plugins.springsecurity.token.usernameAttribute = 'sub'
grails.plugins.springsecurity.token.tokenHeader = 'X-Auth-Token'
grails.plugins.springsecurity.token.key = 'a5e3051a58a742948f80a6ff83d51ac' //unique value for each app
```

Moving to a Test or Production server
Once you have tested your app on localhost and are ready to run `grails war`, open a support ticket in [ServiceNow](http://usffl.service-now.com/) and include the following information:

  * Short Description: SSO AuthTransferService WebToken Project: Application Name
  * Your Name
  * Your Email address
  * Office phone number
  * Application Name
  * Short description of the service
  * Value used for `grails.plugins.springsecurity.token.serviceUrlPrefix`
  * List of attributes that need to be released

##Configuration

There are a few configuration options for the USF Token plugin.

> **Note:** All of these property overrides must be specified in `grails-app/conf/Config.groovy` using the `grails.plugins.springsecurity` prefix, for example:
> 
> ```
grails.plugins.springsecurity.token.serverUrlPrefix =
     'https://authtest.it.usf.edu/AuthTransferService/webtoken'
``` 
> 

| Name	                        | Default	                 | Meaning                                                                                                                                                                                               |
| ----------------------------- | ------------------------------ | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
|<sub>userLookup.userDomainClassName</sub> | <sub>`edu.usf.cims.token.UsfTokenUserDetails`</sub>	 | <sub>SpringSecurity User Class </sub>                                                                                                                                                                            |
|<sub>token.serverUrlPrefix</sub>	        | <sub>`https://authtest.it.usf.edu/AuthTransferService/webtoken`</sub>  | <sub>the 'root' of all webtoken server URLs    </sub>                                                                                                                                                                 |
|<sub>token.webappId</sub>	        | <sub>`http://localhost:8080/${appName}/`</sub>  | <sub>the 'root' of registered application  </sub>                                                                                                                                                                 |
|<sub>token.loginUri</sub>	                | <sub>`/login`</sub>	                 | <sub>the login URI, relative to `token.serverUrlPrefix`, e.g. `/login` </sub>                                                                                                                                      |
|<sub>token.validateUri</sub>	                | <sub>`/validate`</sub>	                 | <sub>the validate URI, relative to `token.serverUrlPrefix`, e.g. `/validate` </sub>                                                                                                                                      |
|<sub>token.filterProcessesUrl</sub>	| <sub>`/j_spring_token_security_check`</sub>	| <sub>the URL that the filter intercepts for login</sub> |
|<sub>token.authorityAttribute</sub>	                | <sub>`eduPersonEntitlement`</sub>	                 | <sub>the attribute of the JWT containing the GrantedAuthorities </sub>                                                                                                                                      |
|<sub>token.usernameAttribute</sub>	                | <sub>`sub`</sub>	                 | <sub>the attribute of the JWT containing the username </sub>                                                                                                                                      |
|<sub>token.key</sub>	| <sub>random value</sub>	| <sub>used by `CasAuthenticationProvider` to identify tokens it previously authenticated. Generated automatically by `grails usf-cas-config`</sub> |

grails.plugins.springsecurity.userLookup.userDomainClassName = 'edu.usf.cims.token.UsfTokenUserDetails'