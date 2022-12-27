package ie.setu.config

import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.*
import io.javalin.vue.VueComponent

class JavalinConfig {
    fun startJavalinService(): Javalin {
        val app = Javalin.create {config ->
            config.staticFiles.enableWebjars()
//            config.vue.vueAppName = "Health-tracker"
        }.apply {
            exception(Exception::class.java) { e, _ -> e.printStackTrace() }
            error(404) { ctx -> ctx.json("404 - Not Found") }
        }.start(getRemoteAssignedPort())

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
            get("/", VueComponent("<home-page></home-page>"))
            get("/users", VueComponent("<user-overview></user-overview>"))
            get("/users/{user-id}", VueComponent("<user-profile></user-profile>"))
            get("/users/{user-id}/activities", VueComponent("<user-activity-overview></user-activity-overview>"))
            get("/activities", VueComponent("<activity-overview></activity-overview>"))
            get("/api-url") { ctx -> ctx.json(mapOf("url" to apiUrl)) }
        }
    }
}