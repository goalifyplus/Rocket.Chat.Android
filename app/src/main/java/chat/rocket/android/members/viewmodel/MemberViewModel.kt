package com.goalify.chat.android.members.viewmodel

import com.goalify.chat.android.helper.UrlHelper
import com.goalify.chat.android.server.domain.useRealName
import chat.rocket.common.model.User
import chat.rocket.core.model.Value

class MemberViewModel(private val member: User, private val settings: Map<String, Value<Any>>, private val baseUrl: String?) {
    val avatarUri: String?
    val displayName: String
    val realName: String?
    val username: String?
    val email: String?
    val utcOffset: Float?

    init {
        avatarUri = getUserAvatar()
        displayName = getUserDisplayName()
        realName = getUserRealName()
        username = getUserUsername()
        email = getUserEmail()
        utcOffset = getUserUtcOffset()
    }

    private fun getUserAvatar(): String? {
        val username = member.username ?: "?"
        return baseUrl?.let {
            UrlHelper.getAvatarUrl(baseUrl, username, "png")
        }
    }

    private fun getUserDisplayName(): String {
        val username = member.username
        val realName = member.name
        val senderName = if (settings.useRealName()) realName else username
        return senderName ?: username.toString()
    }

    private fun getUserRealName(): String? = member.name

    private fun getUserUsername(): String? = member.username

    private fun getUserEmail(): String? = member.emails?.get(0)?.address

    private fun getUserUtcOffset(): Float? = member.utcOffset
}