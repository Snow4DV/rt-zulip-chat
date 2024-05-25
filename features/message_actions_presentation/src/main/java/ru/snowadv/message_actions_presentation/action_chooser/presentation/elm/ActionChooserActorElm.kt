package ru.snowadv.message_actions_presentation.action_chooser.presentation.elm

import dagger.Reusable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import ru.snowadv.chat_domain_api.use_case.LoadMessageUseCase
import ru.snowadv.chat_domain_api.use_case.RemoveMessageUseCase

import ru.snowadv.message_actions_presentation.navigation.MessageActionsRouter
import ru.snowadv.model.Resource
import vivid.money.elmslie.core.store.Actor
import javax.inject.Inject

@Reusable
internal class ActionChooserActorElm @Inject constructor(
    private val loadMessageUseCase: LoadMessageUseCase,
    private val messageActionsRouter: MessageActionsRouter,
    private val removeMessageUseCase: RemoveMessageUseCase,
) : Actor<ActionChooserCommandElm, ActionChooserEventElm>() {
    override fun execute(command: ActionChooserCommandElm): Flow<ActionChooserEventElm> {
        return when(command) {
            is ActionChooserCommandElm.LoadRawMessageToCopy -> loadMessageUseCase(command.messageId, null, false)
                .mapEvents(
                    eventMapper = { res ->
                        when(res) {
                            is Resource.Error -> ActionChooserEventElm.Internal.LoadingCopyMessageError(res.throwable, res.error)
                            is Resource.Loading -> ActionChooserEventElm.Internal.LoadingMessageToCopy
                            is Resource.Success -> ActionChooserEventElm.Internal.CopyMessageLoaded(res.data.content)
                        }
                    },
                    errorMapper = { error ->
                        ActionChooserEventElm.Internal.LoadingCopyMessageError(error, null)
                    }
                )
            is ActionChooserCommandElm.OpenProfile -> flow { messageActionsRouter.openProfile(command.userId) }
            is ActionChooserCommandElm.RemoveMessage -> removeMessageUseCase(command.messageId)
                .mapEvents(
                    eventMapper = { res ->
                        when(res) {
                            is Resource.Error -> ActionChooserEventElm.Internal.MessageRemovalError(res.throwable, res.error)
                            is Resource.Loading -> ActionChooserEventElm.Internal.MessageRemoving
                            is Resource.Success -> ActionChooserEventElm.Internal.MessageRemoved
                        }
                    },
                    errorMapper = { error ->
                        ActionChooserEventElm.Internal.MessageRemovalError(error, null)
                    }
                )
        }
    }


}