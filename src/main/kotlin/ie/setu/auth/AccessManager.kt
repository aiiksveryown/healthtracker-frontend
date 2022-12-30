package ie.setu.auth

import ie.setu.utils.*
import ie.setu.domain.Admin
import ie.setu.config.Roles
import ie.setu.service.ApiService

import io.javalin.http.Context
import io.javalin.http.Handler
import io.javalin.security.RouteRole

object AccessManager {
    private fun Context.refreshAdminInfo() {
        val apiUrl = System.getenv("API_URL") ?: "http://localhost:7001"
        // if it's not a test-related login we refresh the userinfo (but only if it's already set)
        this.adminUser?.let {
            val refreshAdmin = ApiService.roleRefresh(this)
            this.adminUser = jsonToObject(refreshAdmin.body.toString())
        }
    }

    var Context.adminUser: Admin?
        get() = this.sessionAttribute("USER_INFO")

        set(userInfo) = this.sessionAttribute("USER_INFO", userInfo)

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