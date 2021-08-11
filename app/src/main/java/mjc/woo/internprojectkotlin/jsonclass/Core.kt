package mjc.woo.internprojectkotlin.jsonclass

data class Core(
    val limit: Int,
    val remaining: String,
    val reset: Int,
    val resource: String,
    val used: Int
)