package com.diegoferreiracaetano.dlearn.route

import com.diegoferreiracaetano.dlearn.service.HomeService
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.routing.route

fun Route.homeRouting() {
    val homeService = HomeService()
    
    route("/home") {
        get {
            call.respond(homeService.getHome())
        }
    }
}
