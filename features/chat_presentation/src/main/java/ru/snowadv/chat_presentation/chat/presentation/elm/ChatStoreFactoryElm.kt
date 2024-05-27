package ru.snowadv.chat_presentation.chat.presentation.elm

import dagger.Reusable
import vivid.money.elmslie.core.store.Actor
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.Store
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import javax.inject.Inject
import javax.inject.Provider

@Reusable
internal class ChatStoreFactoryElm @Inject constructor(
    private val actor: Actor<ChatCommandElm, ChatEventElm>,
    private val reducer: Provider<ScreenDslReducer<ChatEventElm, ChatEventElm.Ui, ChatEventElm.Internal, ChatStateElm, ChatEffectElm, ChatCommandElm>>,
) {

    fun create(
        streamId: Long,
        streamName: String,
        topicName: String?
    ): Store<ChatEventElm, ChatEffectElm, ChatStateElm> {
        return ElmStore(
            initialState = ChatStateElm(
                stream = streamName,
                topic = topicName,
                eventQueueData = null,
                sendTopic = topicName ?: "",
                streamId = streamId,
                isTopicChooserVisible = topicName == null,
            ),
            actor = actor,
            reducer = reducer.get(),
            startEvent = ChatEventElm.Ui.Init,
        )
    }
}