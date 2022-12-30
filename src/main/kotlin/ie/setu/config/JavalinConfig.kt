package ie.setu.config

import ie.setu.auth.*
import ie.setu.utils.*
import ie.setu.service.ApiService
import ie.setu.config.Roles.*

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.http.servlet.cacheAndSetSessionAttribute
import io.javalin.vue.VueComponent
import org.jetbrains.exposed.sql.Database

class JavalinConfig {
    fun startJavalinService(dbConnection: Database): Javalin {
        val app = Javalin.create {config ->
            config.jetty.sessionHandler {Session.sqlSessionHandler("org.postgresql.Driver", dbConnection.url)}
            config.staticFiles.enableWebjars()
            config.accessManager(AccessManager::manage)
        }.apply {
//            exception(Exception::class.java) { e, _ -> e.printStackTrace() }
            error(404) { ctx -> ctx.json("404 - Not Found") }
        }.start(getRemoteAssignedPort())

        ErrorExceptionMapping.register(app)
        registerRoutes(app)
        return app
    }

    private fun getRemoteAssignedPort(): Int {
        val remotePort = System.getenv("PORT")
        return if (remotePort != null) {
            Integer.parseInt(remotePort)
        } else 7000
    }

    private fun registerRoutes(app: Javalin) {
        val apiUrl = System.getenv("API_URL")
        app.routes {
            // The @routeComponent that we added in layout.html earlier will be replaced
            // by the String inside VueComponent. This means a call to / will load
            // the layout and display our <home-page> component.
            get("/", VueComponent("<home-page></home-page>"), ADMIN, MANAGER)
            get("/login", VueComponent("<login-page></login-page>"), UNAUTHENTICATED)
            get("/logout", {ctx -> ctx.sessionAttribute("USER_INFO", null)}, UNAUTHENTICATED)
            get("/users", VueComponent("<user-overview></user-overview>"), ADMIN, MANAGER)
            get("/users/{user-id}", VueComponent("<user-profile></user-profile>"), ADMIN, MANAGER)
            get("/users/{user-id}/activities", VueComponent("<user-activity-overview></user-activity-overview>"), ADMIN, MANAGER)
            get("/activities", VueComponent("<activity-overview></activity-overview>"), ADMIN, MANAGER)

            path("/api/*") {
                get(ApiService::get, ADMIN, MANAGER, UNAUTHENTICATED)
                path("/admin/login") {
                    post(ApiService::post, UNAUTHENTICATED)
                }
                post(ApiService::post, ADMIN, MANAGER, UNAUTHENTICATED)
                patch(ApiService::patch, ADMIN, MANAGER)
                delete(ApiService::delete, ADMIN, MANAGER)
            }
        }
    }
}