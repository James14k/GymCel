package cl.duocuc.gymcel.data.api.placeholder.modelo

data class Post(
    val userId: Long,
    val id: Long? = 0L,
    val title: String,
    val body: String
)
