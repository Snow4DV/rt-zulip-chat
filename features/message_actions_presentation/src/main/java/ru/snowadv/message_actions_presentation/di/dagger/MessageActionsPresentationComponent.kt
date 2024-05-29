package ru.snowadv.message_actions_presentation.di.dagger

import dagger.Component
import ru.snowadv.message_actions_presentation.action_chooser.ui.ActionChooserBottomSheetDialog
import ru.snowadv.message_actions_presentation.action_chooser.ui.ActionChooserRenderer
import ru.snowadv.message_actions_presentation.di.holder.MessageActionsPresentationAPI
import ru.snowadv.message_actions_presentation.di.holder.MessageActionsPresentationDependencies
import ru.snowadv.message_actions_presentation.emoji_chooser.ui.EmojiChooserBottomSheetDialog
import ru.snowadv.message_actions_presentation.emoji_chooser.ui.EmojiChooserRenderer
import ru.snowadv.message_actions_presentation.message_editor.ui.MessageEditorBottomSheetDialog
import ru.snowadv.message_actions_presentation.message_topic_changer.ui.MessageTopicChangerBottomSheetDialog
import ru.snowadv.module_injector.general.PerScreen

@Component(dependencies = [MessageActionsPresentationDependencies::class], modules = [MessageActionsPresentationModule::class])
@PerScreen
internal interface MessageActionsPresentationComponent : MessageActionsPresentationAPI {

    fun inject(emojiChooserRenderer: EmojiChooserRenderer)
    fun inject(emojiChooserBottomSheetDialog: EmojiChooserBottomSheetDialog)
    fun inject(actionChooserRenderer: ActionChooserRenderer)
    fun inject(actionChooserBottomSheetDialog: ActionChooserBottomSheetDialog)
    fun inject(messageEditor: MessageEditorBottomSheetDialog)
    fun inject(topicChanger: MessageTopicChangerBottomSheetDialog)

    companion object {
        fun initAndGet(deps: MessageActionsPresentationDependencies): MessageActionsPresentationComponent {
            return DaggerMessageActionsPresentationComponent.builder()
                .messageActionsPresentationDependencies(deps)
                .build()
        }
    }
}