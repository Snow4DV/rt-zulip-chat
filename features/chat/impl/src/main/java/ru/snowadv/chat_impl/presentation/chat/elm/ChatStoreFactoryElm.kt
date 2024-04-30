package ru.snowadv.chat_impl.presentation.chat.elm

import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store

internal class ChatStoreFactoryElm(
    private val actor: ChatActorElm,
    private val streamName: String,
    private val topicName: String,
) {

    fun create(): Store<ChatEventElm, ChatEffectElm, ChatStateElm> {
        return ElmStore(
            initialState = ChatStateElm(stream = streamName, topic = topicName, eventQueueData = null),
            actor = actor,
            reducer = ChatReducerElm(),
            startEvent = ChatEventElm.Ui.Init,
        )
    }
}