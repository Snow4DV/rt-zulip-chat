package ru.snowadv.message_actions_presentation.action_chooser.ui.elm

import dagger.Reusable
import ru.snowadv.message_actions_presentation.action_chooser.presentation.elm.ActionChooserEffectElm
import ru.snowadv.message_actions_presentation.action_chooser.presentation.elm.ActionChooserEventElm
import ru.snowadv.message_actions_presentation.action_chooser.presentation.elm.ActionChooserStateElm
import ru.snowadv.message_actions_presentation.action_chooser.ui.util.ActionChooserMappers.toMessageAction
import ru.snowadv.message_actions_presentation.action_chooser.ui.util.ActionChooserMappers.toUiMessageAction
import ru.snowadv.presentation.elm.ElmMapper
import javax.inject.Inject

@Reusable
internal class ActionChooserElmUiMapper @Inject constructor() :
    ElmMapper<ActionChooserStateElm, ActionChooserEffectElm, ActionChooserEventElm, ActionChooserStateUiElm, ActionChooserEffectUiElm, ActionChooserEventUiElm> {
    override fun mapState(state: ActionChooserStateElm): ActionChooserStateUiElm {
        return ActionChooserStateUiElm(
            messageId = state.messageId,
            senderId = state.senderId,
            actions = state.actions.map { it.toUiMessageAction() },
        )
    }

    override fun mapEffect(effect: ActionChooserEffectElm): ActionChooserEffectUiElm {
        return when(effect) {
            is ActionChooserEffectElm.CloseWithResult -> ActionChooserEffectUiElm.CloseWithResult(effect.result)
            is ActionChooserEffectElm.CopyMessageToClipboard -> ActionChooserEffectUiElm.CopyMessageToClipboard(effect.content)
            is ActionChooserEffectElm.ShowError -> ActionChooserEffectUiElm.ShowError(effect.throwable, effect.errorMessage)
        }
    }

    override fun mapUiEvent(uiEvent: ActionChooserEventUiElm): ActionChooserEventElm {
        return when(uiEvent) {
            is ActionChooserEventUiElm.OnActionChosen -> ActionChooserEventElm.Ui.OnActionChosen(uiEvent.action.toMessageAction())
        }
    }
}