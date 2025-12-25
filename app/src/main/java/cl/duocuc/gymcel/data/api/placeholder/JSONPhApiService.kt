package cl.duocuc.gymcel.data.api.placeholder


import cl.duocuc.gymcel.data.api.placeholder.modelo.Post
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface JSONPhApiService {

    @GET("posts")
    suspend fun getPosts(): Response<List<Post>>

    @GET("posts/{id}")
    suspend fun getPostById(@Path("id") id: Long): Response<Post>

    @POST("posts")
    suspend fun createPost(@Body post: Post): Response<Post>

    @DELETE("posts/{id}")
    suspend fun deletePostById(@Path("id") id: Long): Response<*>

    @PUT("posts/{id}")
    suspend fun updatePostById(@Path("id") id: Long, @Body post: Post): Response<Post>

}