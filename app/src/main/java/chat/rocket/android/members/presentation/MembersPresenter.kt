package com.goalify.chat.android.members.presentation

import com.goalify.chat.android.core.lifecycle.CancelStrategy
import com.goalify.chat.android.members.viewmodel.MemberViewModel
import com.goalify.chat.android.members.viewmodel.MemberViewModelMapper
import com.goalify.chat.android.server.domain.GetCurrentServerInteractor
import com.goalify.chat.android.server.infraestructure.RocketChatClientFactory
import com.goalify.chat.android.util.extensions.launchUI
import com.goalify.chat.android.util.retryIO
import chat.rocket.common.RocketChatException
import chat.rocket.common.model.roomTypeOf
import chat.rocket.common.util.ifNull
import chat.rocket.core.RocketChatClient
import chat.rocket.core.internal.rest.getMembers
import javax.inject.Inject

class MembersPresenter @Inject constructor(
    private val view: MembersView,
    private val navigator: MembersNavigator,
    private val strategy: CancelStrategy,
    serverInteractor: GetCurrentServerInteractor,
    factory: RocketChatClientFactory,
    private val mapper: MemberViewModelMapper
) {
    private val client: RocketChatClient = factory.create(serverInteractor.get()!!)

    fun loadChatRoomsMembers(chatRoomId: String, chatRoomType: String, offset: Long = 0) {
        launchUI(strategy) {
            try {
                view.showLoading()

                val members = retryIO("getMembers($chatRoomId, $chatRoomType, $offset)") {
                    client.getMembers(chatRoomId, roomTypeOf(chatRoomType), offset, 60)
                }
                val memberViewModels = mapper.mapToViewModelList(members.result)
                view.showMembers(memberViewModels, members.total)
            } catch (ex: RocketChatException) {
                ex.message?.let {
                    view.showMessage(it)
                }.ifNull {
                    view.showGenericErrorMessage()
                }
            } finally {
                view.hideLoading()
            }
        }
    }

    fun toMemberDetails(memberViewModel: MemberViewModel) {
        val avatarUri = memberViewModel.avatarUri.toString()
        val realName = memberViewModel.realName.toString()
        val username = "@${memberViewModel.username}"
        val email = memberViewModel.email ?: ""
        val utcOffset = memberViewModel.utcOffset.toString()

        navigator.toMemberDetails(avatarUri, realName, username, email, utcOffset)
    }
}