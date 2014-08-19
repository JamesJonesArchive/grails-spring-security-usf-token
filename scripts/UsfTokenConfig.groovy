import java.security.MessageDigest

includeTargets << grailsScript("Init")

target(main: "Creates artifacts for the Spring Security USF Token plugin") {
    def appDir = "$basedir/grails-app"
	def configFile = new File(appDir, 'conf/Config.groovy')
	def appName = Ant.project.properties.'base.name'
	
	
	MessageDigest md5 = MessageDigest.getInstance("MD5");
	String var_dt = new Date().getTime() / 1000
	md5.update(var_dt.getBytes());
	BigInteger hash = new BigInteger(1, md5.digest());
	String uniqKey = hash.toString(16);
	
	if (configFile.exists()) {
		configFile.withWriterAppend {
			it.writeLine '\n// Added by the Spring Security USF Token plugin:'
			it.writeLine "grails.plugins.springsecurity.userLookup.userDomainClassName = 'edu.usf.cims.token.UsfTokenUserDetails'"
                        it.writeLine "grails.plugins.springsecurity.token.webappId = 'http://localhost:8080/${appName}/'"
                        it.writeLine "grails.plugins.springsecurity.token.serverUrlPrefix = 'https://authtest.it.usf.edu/AuthTransferService/webtoken'"
			it.writeLine "grails.plugins.springsecurity.token.loginUri = '/login'"
                        it.writeLine "grails.plugins.springsecurity.token.validateUri = '/validate'"
			it.writeLine "grails.plugins.springsecurity.token.authorityAttribute = 'eduPersonEntitlement'"
                        it.writeLine "grails.plugins.springsecurity.token.usernameAttribute = 'sub'"
                        it.writeLine "grails.plugins.springsecurity.token.tokenHeader = 'X-Auth-Token'"
			it.writeLine "grails.plugins.springsecurity.token.key = '${uniqKey}' //unique value for each app"
		}
	}
	
	ant.echo """
	********************************************************
	* Your grails-app/conf/Config.groovy has been updated. *
	*                                                      *
	* Please verify that the values are correct.           *
	********************************************************
	"""
}

setDefaultTarget(main)