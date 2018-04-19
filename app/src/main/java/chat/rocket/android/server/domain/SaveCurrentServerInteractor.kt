package com.goalify.chat.android.server.domain

import javax.inject.Inject

class SaveCurrentServerInteractor @Inject constructor(private val repository: CurrentServerRepository) {
    fun save(url: String) = repository.save(url)
}