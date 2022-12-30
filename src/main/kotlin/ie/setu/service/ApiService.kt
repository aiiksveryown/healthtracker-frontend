package ie.setu.service

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.joda.JodaModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import ie.setu.auth.AccessManager.adminUser
import ie.setu.domain.Admin
import ie.setu.domain.AdminAuthParams
import ie.setu.utils.jsonToObject
import io.javalin.http.Context
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest

object ApiService {
    private val apiUrl = System.getenv("API_URL") ?: "http://localhost:7001"

    private val mapper = jacksonObjectMapper()
        .registerModule(JodaModule())
        .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)

    fun roleRefresh(ctx: Context) : HttpResponse<JsonNode> {
        val path = "/api/admins/login/refresh"
        val url = "$apiUrl$path"
        // Make http call to api refresh endpoint and return Admin object
        val token = ctx.adminUser?.token
        return Unirest.post(url)
            .header("Authorization", "Bearer $token")
            .body(ctx.adminUser)
            .asJson()
    }

    fun login(ctx: Context) {
        val path = ctx.path()
        val url = "$apiUrl$path"
        // Make http call to api login endpoint and return Admin object
        val admin = jsonToObject<AdminAuthParams>(ctx.body())
        val response = Unirest.post(url)
            .body(admin)
            .asJson()
        println(response.status)
        println(response.body)
        if (response.status == 200) {
            println("Login successful")
            ctx.sessionAttribute("USER_INFO", jsonToObject<Admin>(response.body.toString()))
        }
        ctx.status(response.status)
        ctx.result(mapper.writeValueAsString(response.body.toString()))
    }

    fun get(ctx: Context) {
        val path = ctx.path()
        val queryParams = ctx.queryString()
        val token = ctx.adminUser?.token
        val url = "$apiUrl$path?$queryParams"
        val response: HttpResponse<String> = Unirest.get(url)
            .header("Authorization", "Bearer $token")
            .asString()
        ctx.result(mapper.writeValueAsString(response.body.toString()))
        ctx.status(response.status)
    }

    fun post(ctx: Context) {
        val path = ctx.path()
        if (path == "/api/admins/login") {
            return login(ctx)
        }
        val url = "$apiUrl$path"
        val token = ctx.adminUser?.token
        val response: HttpResponse<JsonNode> = Unirest.post(url)
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer $token")
            .body(ctx.body())
            .asJson()
        ctx.status(response.status)
        ctx.result(mapper.writeValueAsString(response.body.toString()))
    }

    fun patch(ctx: Context) {
        val path = ctx.path()
        val url = "$apiUrl$path"
        val token = ctx.adminUser?.token
        val response: HttpResponse<JsonNode> = Unirest.patch(url)
            .header("Content-Type", "application/json")
            .header("Authorization", "Bearer $token")
            .body(ctx.body())
            .asJson()
        ctx.status(response.status)
        ctx.result(mapper.writeValueAsString(response.body.toString()))
    }

    fun delete(ctx: Context) {
        val path = ctx.path()
        val url = "$apiUrl$path"
        val token = ctx.adminUser?.token
        val response: HttpResponse<JsonNode> = Unirest.delete(url)
            .header("Authorization", "Bearer $token")
            .asJson()
        ctx.status(response.status)
        ctx.result(mapper.writeValueAsString(response.body.toString()))
    }
}