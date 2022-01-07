package com.kuzmin

import com.kuzmin.Exception.PasswordChangeException
import com.kuzmin.Exception.UseraddException
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.gson.*
import io.ktor.features.*

import com.kuzmin.Model.PostModel
import com.kuzmin.Repository.PostRepository
import com.kuzmin.Repository.PostRepositoryInMemoryConcurrentImpl
import com.kuzmin.Repository.UserRepository
import com.kuzmin.Repository.UserRepositoryInMemoryWithMutexImpl
import com.kuzmin.route.RoutingV1
import com.kuzmin.service.*

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.gson.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.server.netty.*
import kotlinx.coroutines.runBlocking
import org.kodein.di.generic.*
import org.kodein.di.ktor.KodeinFeature
import org.kodein.di.ktor.kodein
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import javax.naming.ConfigurationException
import javax.security.auth.login.Configuration

fun main(args: Array<String>) {
    EngineMain.main(args)
}

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        gson {
            setPrettyPrinting()
            serializeNulls()
        }
    }
    install(KodeinFeature) {
        constant(tag = "upload-dir") with (environment.config.propertyOrNull("nscraft.upload.dir")?.getString() ?: throw ConfigurationException("Upload dir"))
        constant(tag = "fcm-password") with (environment.config.propertyOrNull("nscraft.fcm.password")?.getString()
            ?: throw ConfigurationException("FCM Password is not specified"))
        constant(tag = "fcm-salt") with (environment.config.propertyOrNull("nscraft.fcm.salt")?.getString()
            ?: throw ConfigurationException("FCM Salt is not specified"))

        constant(tag = "fcm-path") with (environment.config.propertyOrNull("nscraft.fcm.path")?.getString()
            ?: throw ConfigurationException("FCM JSON Path is not specified"))
        bind<PostRepository>() with singleton { PostRepositoryInMemoryConcurrentImpl() }
        bind<PostService>() with eagerSingleton { PostService(instance()) }
        bind<RoutingV1>() with eagerSingleton { RoutingV1(instance(),instance(tag = "upload-dir"),instance(),instance()) }
        bind<JWTTokenService>() with eagerSingleton { JWTTokenService() }
        bind<FileService>() with eagerSingleton { FileService(instance(tag = "upload-dir")) }
        bind<PasswordEncoder>() with eagerSingleton { BCryptPasswordEncoder() }
        bind<UserRepository>() with eagerSingleton { UserRepositoryInMemoryWithMutexImpl() }
        bind<UserService>() with eagerSingleton {
            UserService(instance(), instance(), instance()).apply {
            }
        }
        bind<FCMService>() with eagerSingleton {
            FCMService(
                instance(tag = "fcm-password"),
                instance(tag = "fcm-salt"),
                instance(tag = "fcm-path")
            ).also {
                runBlocking {

                    //it.send(1, "<TOKEN HERE>", "Your post liked!")
                }
            }
        }
    }
    install(Authentication) {
        jwt {
            val jwtService by kodein().instance<JWTTokenService>()
            verifier(jwtService.verifier)
            val userService by kodein().instance<UserService>()
            validate {

                val id = it.payload.getClaim("id").asLong()
                userService.getModelById(id)
            }
        }
    }
    install(StatusPages) {
        exception<NotImplementedError> {
            call.respond(HttpStatusCode.NotImplemented)
        }
        exception<ParameterConversionException> {
            call.respond(HttpStatusCode.BadRequest)
        }
        exception<Throwable> {
            call.respond(HttpStatusCode.InternalServerError)
        }
        exception<UseraddException> {
            call.respond(HttpStatusCode.BadRequest, it.message.toString())
        }
        exception<PasswordChangeException> {
            call.respond(HttpStatusCode.BadRequest, it.message.toString())
        }

    }
    install(Routing) {
        val routing by kodein().instance<RoutingV1>()
        routing.setup(this)
    }

}


