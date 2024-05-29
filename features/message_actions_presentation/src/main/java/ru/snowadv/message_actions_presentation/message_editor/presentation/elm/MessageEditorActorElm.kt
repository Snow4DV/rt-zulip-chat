package ru.snowadv.message_actions_presentation.message_editor.presentation.elm

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import ru.snowadv.chat_domain_api.use_case.EditMessageUseCase
import ru.snowadv.chat_domain_api.use_case.LoadMessageUseCase
import ru.snowadv.model.Resource
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

@Reusable
internal class MessageEditorActorElm @Inject constructor(
    private val editMessageUseCase: EditMessageUseCase,
    private val loadMessageUseCase: LoadMessageUseCase,
) : Actor<MessageEditorCommandElm, MessageEditorEventElm>() {
    override fun execute(command: MessageEditorCommandElm): Flow<MessageEditorEventElm> {
        return when(command) {
            is MessageEditorCommandElm.EditMessage -> editMessageUseCase(
                messageId = command.messageId,
                newContent = command.newContent,
            ).mapEvents(
                eventMapper = { res ->
                    when(res) {
                        is Resource.Error -> MessageEditorEventElm.Internal.MessageEditingError(res.throwable, res.error)
                        is Resource.Loading -> MessageEditorEventElm.Internal.MessageEditing
                        is Resource.Success -> MessageEditorEventElm.Internal.MessageEdited
                    }
                },
                errorMapper = { error ->
                    MessageEditorEventElm.Internal.MessageEditingError(error, null)
                },
            )

            is MessageEditorCommandElm.LoadMessage -> loadMessageUseCase(
                messageId = command.messageId,
                streamName = command.streamName,
                applyMarkdown = false,
            ).mapEvents(
                eventMapper = { res ->
                    when(res) {
                        is Resource.Error -> MessageEditorEventElm.Internal.MessageLoadingError(res.throwable, res.error)
                        is Resource.Loading -> MessageEditorEventElm.Internal.LoadingMessage
                        is Resource.Success -> MessageEditorEventElm.Internal.MessageLoaded(res.data.content)
                    }
                },
                errorMapper = { error ->
                    MessageEditorEventElm.Internal.MessageEditingError(error, null)
                },
            )
        }
    }

}