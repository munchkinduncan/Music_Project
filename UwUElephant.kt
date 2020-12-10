package com.zetcode

import org.jetbrains.exposed.dao.id.IntIdTable

object UwUElephant : IntIdTable() {
    val username = text("Usernames")
    val password = text("Passwords")
    val scores = integer("scores")
}