package com.kuzmin.route

import com.kuzmin.Model.PostModel
import com.kuzmin.Model.UserModel
import com.kuzmin.Repository.PostRepository
import com.kuzmin.dto.*
import com.kuzmin.dto.MediaResponseDto.Companion.fromModel
import com.kuzmin.dto.PostResponseDto.Companion.fromModel
import com.kuzmin.dto.UserResponseDto.Companion.fromModel
import com.kuzmin.service.FileService
import com.kuzmin.service.UserService
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import org.kodein.di.generic.instance
import org.kodein.di.ktor.kodein

class RoutingV1(val userService : UserService, private val staticPath: String, private val fileService: FileService) {

    fun setup(configuration: Routing) {

        with(configuration) {
            val repo by kodein().instance<PostRepository>()
            route("/api/v1") {
                static("/static") {
                    files(staticPath)
                }
                get("/") {
                    call.respondText("Server working", ContentType.Text.Plain)
                }

                authenticate {
                    route("/media") {
                        post {
                            val multipart = call.receiveMultipart()
                            val response = fileService.save(multipart)
                            call.respond(response)

                        }
                    }
                    route("/me") {
                        get {
                            val me = call.authentication.principal<UserModel>()
                            call.respond(UserResponseDto.fromModel(me!!))
                        }
                    }

                    get {
                        val response = repo.getAll().map { PostResponseDto.fromModel(it) }
                        call.respond(response)
                    }
                    get("/{id}") {
                        val id =
                            call.parameters["id"]?.toLongOrNull()
                                ?: throw ParameterConversionException("id", "Long")
                        val model = repo.getById(id) ?: throw NotFoundException()
                        val response = PostResponseDto.fromModel(model)
                        call.respond(response)
                    }
                    post("/like") {
                        val request = call.receive<PostRequestDto>()
                        val response = repo.likeById(request.id) ?: throw NotFoundException()
                        call.respond(response)
                    }
                    post("/dislike") {
                        val request = call.receive<PostRequestDto>()
                        val response = repo.dislikeById(request.id) ?: throw NotFoundException()
                        call.respond(response)
                    }
                    get("/posts") {
                        val response = repo.getAll()
                        call.respond(response)
                    }

                    post("/new") {
                        val request = call.receive<NewPostDto>()
                        print(request.toString())
                        val response = repo.new(request.txt.toString(), request.author) ?: throw NotFoundException()
                        call.respond(response)
                    }
                    post("/changePassword") {
                        val input = call.receive<PasswordChangeRequestDto>()
                        val me = call.authentication.principal<UserModel>()
                        if (me != null) {
                            val response = userService.changePassword(input.old, input.new, me.id)
                            call.respond("Пароль успешно изменён")
                        }
                    }
                    post("/repost") {
                        val request = call.receive<PostResponseDto>()
                        val model =
                            PostModel(
                                txt = request.txt,
                                repost = request.repost
                            )
                        val response = repo.repost(model) ?: throw NotFoundException()
                        call.respond(response)
                    }
                    post("/{id}/like"){
                        val id = call.parameters["id"]?.toLongOrNull()
                            ?: throw ParameterConversionException("id", "Long")
                        val me = call.authentication.principal<UserModel>()
                        val response = repo.likeById(id) ?: throw NotFoundException()
                        print(response)
                        call.respond(response)
                    }
                    delete("/{id}/likes"){
                        val id = call.parameters["id"]?.toLongOrNull()?: throw ParameterConversionException("id","Long")
                        val me = call.authentication.principal<UserModel>()
                        val response = repo.dislikeById(id)?: throw  NotFoundException()
                        call.respond(response)
                    }
                    post("posts/old") {
                        val id = call.receive<Long>()
                        val response = repo.getOld(id)
                        call.respond(response)
                    }
                }
                post("/authentication") {
                    val input = call.receive<AuthenticationRequestDto>()
                    val response = userService.authenticate(input)
                    call.respond(response)
                }
                post("/registration") {
                    val input = call.receive<AuthenticationRequestDto>()
                    val response = userService.addUser(input.username, input.password)
                    call.respond(response)
                }
            }
        }
    }
}