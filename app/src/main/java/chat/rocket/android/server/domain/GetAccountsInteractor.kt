package com.goalify.chat.android.server.domain

import javax.inject.Inject

class GetAccountsInteractor @Inject constructor(val repository: AccountsRepository) {
    suspend fun get() = repository.load()
}