package ru.snowadv.message_actions_presentation.action_chooser.presentation.elm

import ru.snowadv.message_actions_presentation.action_chooser.presentation.model.MessageAction
import ru.snowadv.message_actions_presentation.api.model.ActionChooserResult
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer
import javax.inject.Inject

internal class ActionChooserReducerElm @Inject constructor():
    ScreenDslReducer<ActionChooserEventElm, ActionChooserEventElm.Ui, ActionChooserEventElm.Internal, ActionChooserStateElm, ActionChooserEffectElm, ActionChooserCommandElm>(
        uiEventClass = ActionChooserEventElm.Ui::class,
        internalEventClass = ActionChooserEventElm.Internal::class,
    ) {
    override fun Result.internal(event: ActionChooserEventElm.Internal) {
        when(event) {
            is ActionChooserEventElm.Internal.CopyMessageLoaded -> {
                state {
                    changeActionLoadingState<MessageAction.CopyMessage>(newLoadingState = true)
                }
                effects {
                    +ActionChooserEffectElm.CopyMessageToClipboard(event.content)
                    +ActionChooserEffectElm.CloseWithResult(ActionChooserResult.CopiedMessage(state.messageId))
                }
            }
            is ActionChooserEventElm.Internal.LoadingCopyMessageError -> {
                state {
                    changeActionLoadingState<MessageAction.CopyMessage>(newLoadingState = false)
                }
                effects {
                    +ActionChooserEffectElm.FinishWithError(event.error, event.errorMessage)
                }
            }
            ActionChooserEventElm.Internal.LoadingMessageToCopy -> state {
                changeActionLoadingState<MessageAction.CopyMessage>(newLoadingState = true)
            }
            is ActionChooserEventElm.Internal.MessageRemovalError -> {
                state {
                    changeActionLoadingState<MessageAction.RemoveMessage>(newLoadingState = false)
                }
                effects {
                    +ActionChooserEffectElm.FinishWithError(event.error, event.errorMessage)
                }
            }
            ActionChooserEventElm.Internal.MessageRemoved -> {
                state {
                    changeActionLoadingState<MessageAction.RemoveMessage>(newLoadingState = false)
                }
                effects {
                    +ActionChooserEffectElm.CloseWithResult(ActionChooserResult.RemovedMessage(state.messageId))
                }
            }
            ActionChooserEventElm.Internal.MessageRemoving -> state {
                changeActionLoadingState<MessageAction.RemoveMessage>(newLoadingState = true)
            }

            is ActionChooserEventElm.Internal.OpenedProfile -> effects {
                +ActionChooserEffectElm.CloseWithResult(ActionChooserResult.OpenedProfile(event.userId))
            }
        }
    }

    override fun Result.ui(event: ActionChooserEventElm.Ui) {
        when(event) {
            is ActionChooserEventElm.Ui.OnActionChosen -> {
                when(event.action) {
                    is MessageAction.AddReaction -> effects {
                        +ActionChooserEffectElm.CloseWithResult(ActionChooserResult.AddReaction(state.messageId))
                    }
                    is MessageAction.CopyMessage -> commands {
                        +ActionChooserCommandElm.LoadRawMessageToCopy(state.messageId, state.streamName)
                    }
                    is MessageAction.EditMessage -> effects {
                        +ActionChooserEffectElm.CloseWithResult(ActionChooserResult.EditMessage(state.messageId))
                    }
                    is MessageAction.MoveMessage -> effects {
                        +ActionChooserEffectElm.CloseWithResult(ActionChooserResult.MoveMessage(state.messageId))
                    }
                    is MessageAction.OpenSenderProfile -> commands {
                        +ActionChooserCommandElm.OpenProfile(state.senderId)
                    }
                    is MessageAction.RemoveMessage -> commands {
                        +ActionChooserCommandElm.RemoveMessage(state.messageId)
                    }
                }
            }
        }
    }

    private inline fun <reified T : MessageAction> ActionChooserStateElm.changeActionLoadingState(newLoadingState: Boolean): ActionChooserStateElm {
        return copy(
            actions = actions.map {
                if (it is T) {
                    it.with(loading = newLoadingState)
                } else {
                    it
                }
            }
        )
    }
}