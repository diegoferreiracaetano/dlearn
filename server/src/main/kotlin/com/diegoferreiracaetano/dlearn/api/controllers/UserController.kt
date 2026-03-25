package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.domain.repository.UserRepository
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
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
