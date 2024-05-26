package ru.snowadv.message_actions_presentation.message_topic_changer.presentation.elm

import dagger.Reusable
import ru.snowadv.message_actions_presentation.action_chooser.presentation.model.MessageAction
import ru.snowadv.message_actions_presentation.message_editor.presentation.elm.MessageEditorCommandElm
import ru.snowadv.message_actions_presentation.message_editor.presentation.elm.MessageEditorEffectElm
import ru.snowadv.message_actions_presentation.message_editor.presentation.elm.MessageEditorEventElm
import ru.snowadv.message_actions_presentation.message_editor.presentation.elm.MessageEditorStateElm
import ru.snowadv.model.ScreenState
import vivid.money.elmslie.core.store.Actor
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import javax.inject.Inject
import javax.inject.Provider

@Reusable
internal class MessageTopicChangerStoreFactoryElm @Inject constructor(
    private val actor: Actor<MessageTopicChangerCommandElm, MessageTopicChangerEventElm>,
    private val reducer: Provider<ScreenDslReducer<MessageTopicChangerEventElm, MessageTopicChangerEventElm.Ui, MessageTopicChangerEventElm.Internal, MessageTopicChangerStateElm, MessageTopicChangerEffectElm, MessageTopicChangerCommandElm>>,
) {

    fun create(
        messageId: Long,
        streamId: Long,
        topicName: String,
    ): ElmStore<MessageTopicChangerEventElm, MessageTopicChangerStateElm, MessageTopicChangerEffectElm, MessageTopicChangerCommandElm> {
        return ElmStore(
            initialState = MessageTopicChangerStateElm(
                messageId = messageId,
                streamId = streamId,
                currentTopicName = topicName,
            ),
            actor = actor,
            reducer = reducer.get(),
            startEvent = MessageTopicChangerEventElm.Ui.Init,
        )
    }
}