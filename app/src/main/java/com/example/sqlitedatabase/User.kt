package com.example.sqlitedatabase

import java.util.*

data class User(
    var id: Int = getAutoId(),
    var name: String = "",
    var email: String = ""
){
    companion object {
        fun getAutoId():Int {
            val random = Random()
            return random.nextInt(100)
        }
    }
}