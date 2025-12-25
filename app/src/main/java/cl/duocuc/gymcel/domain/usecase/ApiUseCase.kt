package cl.duocuc.gymcel.domain.usecase

import cl.duocuc.gymcel.data.api.placeholder.JSONPhApiService
import cl.duocuc.gymcel.data.api.placeholder.modelo.Post

suspend fun useApi(api: JSONPhApiService) {
    // GET /posts
    val postsResponse = api.getPosts()
    if (postsResponse.isSuccessful) {
        val posts = postsResponse.body().orEmpty()
        println("GET posts -> ${posts.size} posts recibidos")

        posts.take(3).forEach { //solo 3
            println(" - [${it.id}] ${it.title}")
        }
        println("...")
    } else {
        println("GET posts -> error ${postsResponse.code()}")
    }

    // GET /posts/{id}
    val postId = 1L
    val postByIdResponse = api.getPostById(postId)
    if (postByIdResponse.isSuccessful) {
        val post = postByIdResponse.body()
        println("GET postById -> $post")
    } else {
        println("GET postById -> error ${postByIdResponse.code()}")
    }

    // POST /posts
    val newPost = Post(
        userId = 99L,
        title = "Post de prueba",
        body = "Este post fue creado solo para demostrar el uso de la API"
    )

    val createResponse = api.createPost(newPost)
    val createdPost = if (createResponse.isSuccessful) {
        createResponse.body().also {
            println("POST createPost -> creado: $it")
        }
    } else {
        println("POST createPost -> error ${createResponse.code()}")
        null
    }

    // PUT /posts/{id}
    if (createdPost != null) {
        val updatedPost = createdPost.copy(
            title = "Post actualizado",
            body = "Contenido actualizado solo para demo"
        )

        val updateResponse = api.updatePostById(1L, updatedPost)
        if (updateResponse.isSuccessful) {
            println("PUT updatePostById -> ${updateResponse.body()}")
        } else {
            println("PUT updatePostById -> error ${updateResponse.code()}")
        }
    }

    // DELETE /posts/{id}
    val deleteId = 1L
    val deleteResponse = api.deletePostById(deleteId)
    if (deleteResponse.isSuccessful) {
        println("DELETE deletePostById -> post $deleteId eliminado")
    } else {
        println("DELETE deletePostById -> error ${deleteResponse.code()}")
    }
}
