package mjc.woo.internprojectkotlin

import android.app.Activity
import mjc.woo.internprojectkotlin.R

class UserDetailText(
    var userId: String,
    userName: String?,
    userCompany: String?,
    userEmail: String?,
    userFollowers: String,
    userFollowing: String,
    activity: Activity
) {
    var userName: String? = null
    var userCompany: String? = null
    var userEmail: String? = null
    var userFollowers: String
    var userFollowing: String

    init{
        this.userName = userName
        this.userCompany = userCompany
        this.userEmail = userEmail
        this.userFollowers = activity.getString(R.string.followers).plus(userFollowers)
        this.userFollowing = activity.getString(R.string.following).plus(userFollowing)
    }
}