package ie.setu.auth

import ie.setu.utils.*
import ie.setu.domain.Admin
import ie.setu.config.Roles
import ie.setu.service.ApiService

import io.javalin.http.Context
import io.javalin.http.Handler
import io.javalin.security.RouteRole
import org.slf4j.LoggerFactory

object AccessManager {
    private fun Context.refreshAdminInfo() {
        val apiUrl = System.getenv("API_URL") ?: "http://localhost:7001"
        // if it's not a test-related login we refresh the userinfo (but only if it's already set)
        try {
            if (this.adminUser != null && this.path() != "/api/admins/login") {
                val response = ApiService.roleRefresh(this)
                if (response.status == 200) {
                    this.sessionAttribute("USER_INFO", jsonToObject<Admin>(response.body.toString()))
                }
                else if (response.status == 401) {
                    LoggerFactory.getLogger("AccessManager").info("User token expired/invalid")
                    this.sessionAttribute("USER_INFO", null)
                }
            }
        } catch (e: Exception) {
            LoggerFactory.getLogger("AccessManager").error("Error refreshing admin info", e)
            this.sessionAttribute("USER_INFO", null)
        }
    }

    var Context.adminUser: Admin?
        get() = this.sessionAttribute("USER_INFO")

        set(userInfo) = this.sessionAttribute("USER_INFO", userInfo)

    fun currentUser(ctx: Context) = ctx.adminUser?.email

    fun manage(handler: Handler, ctx: Context, permittedRoles: Set<RouteRole>) {
//        ctx.sessionAttribute("USER_INFO", null)
        ctx.refreshAdminInfo() // make sure role matches database role
        when {
            Roles.UNAUTHENTICATED in permittedRoles -> handler.handle(ctx)
            ctx.adminUser == null -> ctx.redirect("/login")
            ctx.adminUser!!.role in permittedRoles -> handler.handle(ctx) // user role fits any onf the roles for the endpoint
            else -> ctx.status(401)
        }
    }
}