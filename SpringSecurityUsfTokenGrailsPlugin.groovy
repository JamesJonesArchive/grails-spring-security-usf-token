import org.codehaus.groovy.grails.plugins.springsecurity.SecurityFilterPosition
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import edu.usf.cims.token.UsfTokenAuthenticationProvider
import edu.usf.cims.token.UsfAuthenticationToken
import edu.usf.cims.token.UsfTokenUserDetailsService
import edu.usf.cims.token.UsfTokenService
import edu.usf.cims.token.UsfTokenAuthenticationFilter
import edu.usf.cims.token.UsfTokenAuthenticationManager
import org.springframework.security.cas.authentication.NullStatelessTicketCache
import org.springframework.security.cas.web.CasAuthenticationEntryPoint
import org.springframework.security.cas.web.CasAuthenticationFilter
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class SpringSecurityUsfTokenGrailsPlugin {
    // the plugin version
    def version = "0.1.0"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.2.1 > *"
    // Map of dependencies for the plugin
    Map dependsOn = [springSecurityCore: '1.2.7.3 > *']
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp",
        'src/docs/**',
        'scripts/CreateCasTestApps.groovy',
        'grails-app/views/casTest/**',
        'test/**'
    ]

    // TODO Fill in these fields
    def title = "USF CAS Token Spring Security Plugin" // Headline display name of the plugin
    def author = "James Jones"
    def authorEmail = "james@mail.usf.edu"    
    
    def description = '''\
Brief summary/description of the plugin.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/grails-spring-security-usf-token"

    // Extra (optional) plugin metadata

    // License: one of 'APACHE', 'GPL2', 'GPL3'
    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
    def organization = [ name: "USF", url: "http://www.usf.edu/" ]

    // Any additional developers beyond the author specified above.
    //    def developers = [ [ name: "Joe Bloggs", email: "joe@bloggs.net" ]]

    // Location of the plugin's issue tracker.
    //    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMYPLUGIN" ]

    // Online location of the plugin's browseable source code.
    //    def scm = [ url: "http://svn.codehaus.org/grails-plugins/" ]
    def loadAfter = ['springSecurityCore'] 
    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before
        def conf = SpringSecurityUtils.securityConfig
        if (!conf || !conf.active) {
            return
        }
        SpringSecurityUtils.loadSecondaryConfig 'DefaultUsfTokenSecurityConfig'
        // have to get again after overlaying DefaultUsfTokenSecurityConfig
        conf = SpringSecurityUtils.securityConfig

        if (!conf.token.active) {
            return
        }

        if (!conf.token.useSingleSignout) {
            return
        }
    }
    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
        def conf = SpringSecurityUtils.securityConfig
        if (!conf || !conf.active) {
            return
        }

        if (application.warDeployed) {
            // need to load secondary here since web.xml was already built, so
            // doWithWebDescriptor isn't called when deployed as war
 
            SpringSecurityUtils.loadSecondaryConfig 'DefaultUsfTokenSecurityConfig'
            conf = SpringSecurityUtils.securityConfig
            if (!conf.token.active) {
                return
            }
        }

        println 'Configuring Spring Security USF Token ...'
        // Replace userDetailsService with a Token version - all userdetails will come from the token
        userDetailsService(edu.usf.cims.token.UsfTokenUserDetailsService)
                
        SpringSecurityUtils.registerProvider 'usfTokenAuthenticationProvider'
        SpringSecurityUtils.registerFilter 'usfTokenAuthenticationFilter', SecurityFilterPosition.CAS_FILTER
        
        // TODO  document NullProxyGrantingTicketStorage
        // casProxyGrantingTicketStorage(ProxyGrantingTicketStorageImpl)
        
        authenticationEntryPoint(edu.usf.cims.token.UsfTokenAuthenticationEntryPoint) {
            loginUrl = conf.token.serverUrlPrefix + conf.token.loginUri
            webappId = conf.token.webappId
            cors = application.config.cors
        }
        
        accessDeniedHandler(edu.usf.cims.token.UsfTokenAccessDeniedHandler)

        authenticationSuccessHandler(org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler)
        authenticationFailureHandler(edu.usf.cims.token.UsfTokenAuthenticationFailureHandler) {
            loginUrl = conf.token.serverUrlPrefix + conf.token.loginUri
            webappId = conf.token.webappId
            cors = application.config.cors
        }
        
        usfTokenAuthenticationFilter(edu.usf.cims.token.UsfTokenAuthenticationFilter,conf.token.filterProcessesUrl) {
            authenticationSuccessHandler = ref('authenticationSuccessHandler')
            authenticationFailureHandler = ref('authenticationFailureHandler')
            rememberMeServices = ref('rememberMeServices')
            tokenHeader = conf.token.tokenHeader           
        }
        
        usfTokenService(edu.usf.cims.token.UsfTokenService)
        
        UsfTokenAuthenticationManager(edu.usf.cims.token.UsfTokenAuthenticationManager) {
            userDetailsService = ref('userDetailsService')
        }
        usfTokenAuthenticationProvider(edu.usf.cims.token.UsfTokenAuthenticationProvider) {
            userDetailsService = ref('userDetailsService')
        }
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
        // def config = ConfigurationHolder.config
        def conf = SpringSecurityUtils.securityConfig
        def application = ApplicationHolder.application
        
        def usfTokenUserDetailsService = new UsfTokenUserDetailsService()
        UsfAuthenticationToken.metaClass.loadUserByToken  = {token->  
            return usfTokenUserDetailsService.loadUserByToken(token)
        }
        
        UsfTokenAuthenticationManager.metaClass.validate = {token ->            
            return ctx.getBean('usfTokenService').validate(conf.token.serverUrlPrefix + conf.token.validateUri,conf.token.webappId,token)
        }
        UsfTokenAuthenticationProvider.metaClass.validate = {token ->
            return ctx.getBean('usfTokenService').validate(conf.token.serverUrlPrefix + conf.token.validateUri,conf.token.webappId,token)
        }
    }

    def doWithApplicationContext = { applicationContext ->
        // TODO Implement post initialization spring config (optional)
    }

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }

    def onShutdown = { event ->
        // TODO Implement code that is executed when the application shuts down (optional)
    }
}
