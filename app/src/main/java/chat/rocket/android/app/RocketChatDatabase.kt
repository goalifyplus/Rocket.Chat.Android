package com.goalify.chat.android.app

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

import com.goalify.chat.android.server.infraestructure.ServerDao
import com.goalify.chat.android.server.infraestructure.ServerEntity

@Database(entities = arrayOf(ServerEntity::class), version = 1, exportSchema = false)
abstract class RocketChatDatabase : RoomDatabase() {
    abstract fun serverDao(): ServerDao
}
