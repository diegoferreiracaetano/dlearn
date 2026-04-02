package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.domain.repository.UserRepository
import io.ktor.server.application.call
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import org.koin.ktor.ext.inject

fun Route.userController() {
    val userRepository by inject<UserRepository>()

    route("/v1/users") {
        get {
            val users = userRepository.findAll()
            call.respond(users)
        }
    }
}
