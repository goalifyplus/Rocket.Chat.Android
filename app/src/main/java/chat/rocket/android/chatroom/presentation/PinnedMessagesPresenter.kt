package com.goalify.chat.android.chatroom.presentation

import com.goalify.chat.android.chatroom.viewmodel.ViewModelMapper
import com.goalify.chat.android.core.lifecycle.CancelStrategy
import com.goalify.chat.android.server.domain.GetChatRoomsInteractor
import com.goalify.chat.android.server.domain.GetCurrentServerInteractor
import com.goalify.chat.android.server.domain.GetSettingsInteractor
import com.goalify.chat.android.server.infraestructure.RocketChatClientFactory
import com.goalify.chat.android.util.extensions.launchUI
import chat.rocket.common.RocketChatException
import chat.rocket.common.util.ifNull
import chat.rocket.core.internal.rest.getRoomPinnedMessages
import chat.rocket.core.model.Value
import chat.rocket.core.model.isSystemMessage
import timber.log.Timber
import javax.inject.Inject

class PinnedMessagesPresenter @Inject constructor(private val view: PinnedMessagesView,
                                                  private val strategy: CancelStrategy,
                                                  private val serverInteractor: GetCurrentServerInteractor,
                                                  private val roomsInteractor: GetChatRoomsInteractor,
                                                  private val mapper: ViewModelMapper,
                                                  factory: RocketChatClientFactory,
                                                  getSettingsInteractor: GetSettingsInteractor) {

    private val client = factory.create(serverInteractor.get()!!)
    private var settings: Map<String, Value<Any>> = getSettingsInteractor.get(serverInteractor.get()!!)
    private var pinnedMessagesListOffset: Int = 0

    /**
     * Load all pinned messages for the given room id.
     *
     * @param roomId The id of the room to get pinned messages from.
     */
    fun loadPinnedMessages(roomId: String) {
        launchUI(strategy) {
            try {
                val serverUrl = serverInteractor.get()!!
                val chatRoom = roomsInteractor.getById(serverUrl, roomId)
                chatRoom?.let { room ->
                    view.showLoading()
                    val pinnedMessages =
                        client.getRoomPinnedMessages(roomId, room.type, pinnedMessagesListOffset)
                    pinnedMessagesListOffset = pinnedMessages.offset.toInt()
                    val messageList = mapper.map(pinnedMessages.result.filterNot { it.isSystemMessage() })
                    view.showPinnedMessages(messageList)
                    view.hideLoading()
                }.ifNull {
                    Timber.e("Couldn't find a room with id: $roomId at current server.")
                }
            } catch (e: RocketChatException) {
                Timber.e(e)
            }
        }
    }
}