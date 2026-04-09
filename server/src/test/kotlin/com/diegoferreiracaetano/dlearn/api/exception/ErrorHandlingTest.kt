package com.diegoferreiracaetano.dlearn.api.exception

import com.diegoferreiracaetano.dlearn.domain.error.AppError
import com.diegoferreiracaetano.dlearn.domain.error.AppErrorCode
import com.diegoferreiracaetano.dlearn.domain.error.AppException
import com.diegoferreiracaetano.dlearn.domain.models.ChallengeCode
import com.diegoferreiracaetano.dlearn.domain.models.ChallengeError
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import org.junit.Test
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import kotlin.test.assertEquals

class ErrorHandlingTest {

    private fun buildTestModule() = module {
        single { ChallengeMapper() }
    }

    @Test
    fun `given AppException with UNAUTHORIZED when thrown should return 401`() = testApplication {
        application {
            install(Koin) { modules(buildTestModule()) }
            install(ContentNegotiation) { json() }
            configureStatusPages()
            routing {
                get("/test") { throw AppException(AppError(AppErrorCode.UNAUTHORIZED)) }
            }
        }

        assertEquals(HttpStatusCode.Unauthorized, client.get("/test").status)
    }

    @Test
    fun `given AppException with INVALID_TOKEN when thrown should return 401`() = testApplication {
        application {
            install(Koin) { modules(buildTestModule()) }
            install(ContentNegotiation) { json() }
            configureStatusPages()
            routing {
                get("/test") { throw AppException(AppError(AppErrorCode.INVALID_TOKEN)) }
            }
        }

        assertEquals(HttpStatusCode.Unauthorized, client.get("/test").status)
    }

    @Test
    fun `given AppException with EXPIRED_TOKEN when thrown should return 401`() = testApplication {
        application {
            install(Koin) { modules(buildTestModule()) }
            install(ContentNegotiation) { json() }
            configureStatusPages()
            routing {
                get("/test") { throw AppException(AppError(AppErrorCode.EXPIRED_TOKEN)) }
            }
        }

        assertEquals(HttpStatusCode.Unauthorized, client.get("/test").status)
    }

    @Test
    fun `given AppException with INVALID_CREDENTIALS when thrown should return 401`() = testApplication {
        application {
            install(Koin) { modules(buildTestModule()) }
            install(ContentNegotiation) { json() }
            configureStatusPages()
            routing {
                get("/test") { throw AppException(AppError(AppErrorCode.INVALID_CREDENTIALS)) }
            }
        }

        assertEquals(HttpStatusCode.Unauthorized, client.get("/test").status)
    }

    @Test
    fun `given AppException with FORBIDDEN when thrown should return 403`() = testApplication {
        application {
            install(Koin) { modules(buildTestModule()) }
            install(ContentNegotiation) { json() }
            configureStatusPages()
            routing {
                get("/test") { throw AppException(AppError(AppErrorCode.FORBIDDEN)) }
            }
        }

        assertEquals(HttpStatusCode.Forbidden, client.get("/test").status)
    }

    @Test
    fun `given AppException with NOT_FOUND when thrown should return 404`() = testApplication {
        application {
            install(Koin) { modules(buildTestModule()) }
            install(ContentNegotiation) { json() }
            configureStatusPages()
            routing {
                get("/test") { throw AppException(AppError(AppErrorCode.NOT_FOUND)) }
            }
        }

        assertEquals(HttpStatusCode.NotFound, client.get("/test").status)
    }

    @Test
    fun `given AppException with USER_NOT_FOUND when thrown should return 404`() = testApplication {
        application {
            install(Koin) { modules(buildTestModule()) }
            install(ContentNegotiation) { json() }
            configureStatusPages()
            routing {
                get("/test") { throw AppException(AppError(AppErrorCode.USER_NOT_FOUND)) }
            }
        }

        assertEquals(HttpStatusCode.NotFound, client.get("/test").status)
    }

    @Test
    fun `given AppException with MOVIE_NOT_FOUND when thrown should return 404`() = testApplication {
        application {
            install(Koin) { modules(buildTestModule()) }
            install(ContentNegotiation) { json() }
            configureStatusPages()
            routing {
                get("/test") { throw AppException(AppError(AppErrorCode.MOVIE_NOT_FOUND)) }
            }
        }

        assertEquals(HttpStatusCode.NotFound, client.get("/test").status)
    }

    @Test
    fun `given AppException with USER_ALREADY_EXISTS when thrown should return 409`() = testApplication {
        application {
            install(Koin) { modules(buildTestModule()) }
            install(ContentNegotiation) { json() }
            configureStatusPages()
            routing {
                get("/test") { throw AppException(AppError(AppErrorCode.USER_ALREADY_EXISTS)) }
            }
        }

        assertEquals(HttpStatusCode.Conflict, client.get("/test").status)
    }

    @Test
    fun `given AppException with EMAIL_ALREADY_IN_USE when thrown should return 409`() = testApplication {
        application {
            install(Koin) { modules(buildTestModule()) }
            install(ContentNegotiation) { json() }
            configureStatusPages()
            routing {
                get("/test") { throw AppException(AppError(AppErrorCode.EMAIL_ALREADY_IN_USE)) }
            }
        }

        assertEquals(HttpStatusCode.Conflict, client.get("/test").status)
    }

    @Test
    fun `given AppException with BAD_REQUEST code when thrown should return 400`() = testApplication {
        application {
            install(Koin) { modules(buildTestModule()) }
            install(ContentNegotiation) { json() }
            configureStatusPages()
            routing {
                get("/test") { throw AppException(AppError(AppErrorCode.BAD_REQUEST)) }
            }
        }

        assertEquals(HttpStatusCode.BadRequest, client.get("/test").status)
    }

    @Test
    fun `given a SecurityException when thrown should return 401`() = testApplication {
        application {
            install(Koin) { modules(buildTestModule()) }
            install(ContentNegotiation) { json() }
            configureStatusPages()
            routing {
                get("/test") { throw SecurityException("Not allowed") }
            }
        }

        assertEquals(HttpStatusCode.Unauthorized, client.get("/test").status)
    }

    @Test
    fun `given a ChallengeException when thrown should return 428`() = testApplication {
        application {
            install(Koin) { modules(buildTestModule()) }
            install(ContentNegotiation) { json() }
            configureStatusPages()
            routing {
                get("/test") {
                    throw ChallengeException(
                        ChallengeError(
                            code = ChallengeCode.CHALLENGE_REQUIRED,
                            message = "OTP required",
                            challengeToken = "txn-123",
                        ),
                    )
                }
            }
        }

        assertEquals(HttpStatusCode(428, "Precondition Required"), client.get("/test").status)
    }

    @Test
    fun `given an IllegalArgumentException when thrown should return 400`() = testApplication {
        application {
            install(Koin) { modules(buildTestModule()) }
            install(ContentNegotiation) { json() }
            configureStatusPages()
            routing {
                get("/test") { throw IllegalArgumentException("bad input") }
            }
        }

        assertEquals(HttpStatusCode.BadRequest, client.get("/test").status)
    }

    @Test
    fun `given a NoSuchElementException when thrown should return 404`() = testApplication {
        application {
            install(Koin) { modules(buildTestModule()) }
            install(ContentNegotiation) { json() }
            configureStatusPages()
            routing {
                get("/test") { throw NoSuchElementException("not found") }
            }
        }

        assertEquals(HttpStatusCode.NotFound, client.get("/test").status)
    }

    @Test
    fun `given a generic RuntimeException when thrown should return 500`() = testApplication {
        application {
            install(Koin) { modules(buildTestModule()) }
            install(ContentNegotiation) { json() }
            configureStatusPages()
            routing {
                get("/test") { throw IllegalStateException("unexpected error") }
            }
        }

        assertEquals(HttpStatusCode.InternalServerError, client.get("/test").status)
    }

    @Test
    fun `given a RuntimeException with X-Challenge-Preference header when thrown should still return 500`() = testApplication {
        application {
            install(Koin) { modules(buildTestModule()) }
            install(ContentNegotiation) { json() }
            configureStatusPages()
            routing {
                get("/test") { throw IllegalStateException("unexpected error") }
            }
        }

        val response = client.get("/test") {
            header("X-Challenge-Preference", "OTP_EMAIL")
        }
        assertEquals(HttpStatusCode.InternalServerError, response.status)
    }

    @Test
    fun `given AppException with CHALLENGE_REQUIRED code when thrown should return 400`() = testApplication {
        application {
            install(Koin) { modules(buildTestModule()) }
            install(ContentNegotiation) { json() }
            configureStatusPages()
            routing {
                get("/test") { throw AppException(AppError(AppErrorCode.CHALLENGE_REQUIRED)) }
            }
        }

        assertEquals(HttpStatusCode.BadRequest, client.get("/test").status)
    }
}
