package ie.setu.auth

import org.eclipse.jetty.server.session.*

object Session {

    // use this if you need a session database
    fun sqlSessionHandler(driver: String, url: String) = SessionHandler().apply {
        sessionCache = DefaultSessionCache(this).apply { // use a NullSessionCache if you don't have sticky sessions
            sessionDataStore = JDBCSessionDataStoreFactory().apply {
                setDatabaseAdaptor(DatabaseAdaptor().apply {
                    setDriverInfo(driver, url)
                })
            }.getSessionDataStore(sessionHandler)
        }
        httpOnly = true
        // make additional changes to your SessionHandler here
    }

}