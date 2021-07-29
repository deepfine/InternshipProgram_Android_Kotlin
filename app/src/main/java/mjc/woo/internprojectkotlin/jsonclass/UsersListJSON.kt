package mjc.woo.internprojectkotlin.jsonclass

data class UsersListJSON(
    val incomplete_results: Boolean,
    val items: List<Item>,
    val total_count: Int
)