package ie.setu.config

import io.javalin.security.RouteRole

enum class Roles : RouteRole {
    UNAUTHENTICATED,
    ADMIN,
    MANAGER,
}