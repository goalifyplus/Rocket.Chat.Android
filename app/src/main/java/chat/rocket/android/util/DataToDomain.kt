package com.goalify.chat.android.util

interface DataToDomain<Data, Domain> {
    fun translate(data: Data): Domain
}
