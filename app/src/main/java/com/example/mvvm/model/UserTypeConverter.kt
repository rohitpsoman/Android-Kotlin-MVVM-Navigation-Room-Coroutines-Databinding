package com.example.mvvm.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class UserTypeConverter {

    @TypeConverter
    fun fromString(value: String): User {
        val type = object : TypeToken<User>() {}.type
        return Gson().fromJson(value, type)
    }

    @TypeConverter
    fun fromUser(user: User): String {
        return Gson().toJson(user)
    }
}