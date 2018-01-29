package ua.artjoker.mvvmsamples.core

class Profile(
        val id: String?,
        val firstName: String,
        val lastName: String,
        val photoUrl: String? = null) {

    val fullName get() = "$firstName $lastName"
}
