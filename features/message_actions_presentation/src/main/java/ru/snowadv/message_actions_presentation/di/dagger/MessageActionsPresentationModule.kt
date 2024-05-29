package ru.snowadv.message_actions_presentation.di.dagger

import dagger.Binds
import dagger.Module
import ru.snowadv.message_actions_presentation.action_chooser.presentation.elm.ActionChooserActorElm
import ru.snowadv.message_actions_presentation.action_chooser.presentation.elm.ActionChooserCommandElm
import ru.snowadv.message_actions_presentation.action_chooser.presentation.elm.ActionChooserEffectElm
import ru.snowadv.message_actions_presentation.action_chooser.presentation.elm.ActionChooserEventElm
import ru.snowadv.message_actions_presentation.action_chooser.presentation.elm.ActionChooserReducerElm
import ru.snowadv.message_actions_presentation.action_chooser.presentation.elm.ActionChooserStateElm
import ru.snowadv.message_actions_presentation.action_chooser.ui.elm.ActionChooserEffectUiElm
import ru.snowadv.message_actions_presentation.action_chooser.ui.elm.ActionChooserElmUiMapper
import ru.snowadv.message_actions_presentation.action_chooser.ui.elm.ActionChooserEventUiElm
import ru.snowadv.message_actions_presentation.action_chooser.ui.elm.ActionChooserStateUiElm
import ru.snowadv.message_actions_presentation.action_chooser.ui.feature.ActionChooserDialogFactoryImpl
import ru.snowadv.message_actions_presentation.api.screen_factory.ActionChooserDialogFactory
import ru.snowadv.message_actions_presentation.api.screen_factory.EmojiChooserDialogFactory
import ru.snowadv.message_actions_presentation.api.screen_factory.MessageEditorDialogFactory
import ru.snowadv.message_actions_presentation.api.screen_factory.MessageTopicChangerDialogFactory
import ru.snowadv.message_actions_presentation.emoji_chooser.presentation.elm.EmojiChooserActorElm
import ru.snowadv.message_actions_presentation.emoji_chooser.presentation.elm.EmojiChooserCommandElm
import ru.snowadv.message_actions_presentation.emoji_chooser.presentation.elm.EmojiChooserEffectElm
import ru.snowadv.message_actions_presentation.emoji_chooser.presentation.elm.EmojiChooserEventElm
import ru.snowadv.message_actions_presentation.emoji_chooser.presentation.elm.EmojiChooserReducerElm
import ru.snowadv.message_actions_presentation.emoji_chooser.presentation.elm.EmojiChooserStateElm
import ru.snowadv.message_actions_presentation.emoji_chooser.ui.elm.EmojiChooserEffectUiElm
import ru.snowadv.message_actions_presentation.emoji_chooser.ui.elm.EmojiChooserElmUiMapper
import ru.snowadv.message_actions_presentation.emoji_chooser.ui.elm.EmojiChooserEventUiElm
import ru.snowadv.message_actions_presentation.emoji_chooser.ui.elm.EmojiChooserStateUiElm
import ru.snowadv.message_actions_presentation.emoji_chooser.ui.feature.EmojiChooserDialogFactoryImpl
import ru.snowadv.message_actions_presentation.message_editor.presentation.elm.MessageEditorActorElm
import ru.snowadv.message_actions_presentation.message_editor.presentation.elm.MessageEditorCommandElm
import ru.snowadv.message_actions_presentation.message_editor.presentation.elm.MessageEditorEffectElm
import ru.snowadv.message_actions_presentation.message_editor.presentation.elm.MessageEditorEventElm
import ru.snowadv.message_actions_presentation.message_editor.presentation.elm.MessageEditorReducerElm
import ru.snowadv.message_actions_presentation.message_editor.presentation.elm.MessageEditorStateElm
import ru.snowadv.message_actions_presentation.message_editor.ui.feature.MessageEditorDialogFactoryImpl
import ru.snowadv.message_actions_presentation.message_topic_changer.presentation.elm.MessageTopicChangerActorElm
import ru.snowadv.message_actions_presentation.message_topic_changer.presentation.elm.MessageTopicChangerCommandElm
import ru.snowadv.message_actions_presentation.message_topic_changer.presentation.elm.MessageTopicChangerEffectElm
import ru.snowadv.message_actions_presentation.message_topic_changer.presentation.elm.MessageTopicChangerEventElm
import ru.snowadv.message_actions_presentation.message_topic_changer.presentation.elm.MessageTopicChangerReducerElm
import ru.snowadv.message_actions_presentation.message_topic_changer.presentation.elm.MessageTopicChangerStateElm
import ru.snowadv.message_actions_presentation.message_topic_changer.ui.feature.MessageTopicChangerDialogFactoryImpl
import ru.snowadv.presentation.elm.ElmMapper
import vivid.money.elmslie.core.store.Actor
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer

@Module
internal interface MessageActionsPresentationModule {
    @Binds
    fun bindActionChooserActor(impl: ActionChooserActorElm): Actor<ActionChooserCommandElm, ActionChooserEventElm>
    @Binds
    fun bindActionChooserReducer(impl: ActionChooserReducerElm): ScreenDslReducer<ActionChooserEventElm, ActionChooserEventElm.Ui, ActionChooserEventElm.Internal, ActionChooserStateElm, ActionChooserEffectElm, ActionChooserCommandElm>
    @Binds
    fun bindActionChooserDialogFactory(impl: ActionChooserDialogFactoryImpl): ActionChooserDialogFactory
    @Binds
    fun bindEmojiChooserActorElm(emojiChooserActorElm: EmojiChooserActorElm): Actor<EmojiChooserCommandElm, EmojiChooserEventElm>
    @Binds
    fun bindEmojiChooserReducer(emojiChooserReducerElm: EmojiChooserReducerElm): ScreenDslReducer<EmojiChooserEventElm, EmojiChooserEventElm.Ui, EmojiChooserEventElm.Internal, EmojiChooserStateElm, EmojiChooserEffectElm, EmojiChooserCommandElm>
    @Binds
    fun bindEmojiChooserDialogFactory(impl: EmojiChooserDialogFactoryImpl): EmojiChooserDialogFactory
    @Binds
    fun bindEmojiChooserMapper(mapper: EmojiChooserElmUiMapper): ElmMapper<EmojiChooserStateElm, EmojiChooserEffectElm, EmojiChooserEventElm, EmojiChooserStateUiElm, EmojiChooserEffectUiElm, EmojiChooserEventUiElm>
    @Binds
    fun bindActionChooserMapper(mapper: ActionChooserElmUiMapper): ElmMapper<ActionChooserStateElm, ActionChooserEffectElm, ActionChooserEventElm, ActionChooserStateUiElm, ActionChooserEffectUiElm, ActionChooserEventUiElm>
    @Binds
    fun bindMessageEditorActor(actor: MessageEditorActorElm): Actor<MessageEditorCommandElm, MessageEditorEventElm>
    @Binds
    fun bindMessageEditorReducer(reducer: MessageEditorReducerElm): ScreenDslReducer<MessageEditorEventElm, MessageEditorEventElm.Ui, MessageEditorEventElm.Internal, MessageEditorStateElm, MessageEditorEffectElm, MessageEditorCommandElm>
    @Binds
    fun bindMessageEditorDialogFactoryImpl(impl: MessageEditorDialogFactoryImpl): MessageEditorDialogFactory
    @Binds
    fun bindMessageTopicChangerActor(actor: MessageTopicChangerActorElm): Actor<MessageTopicChangerCommandElm, MessageTopicChangerEventElm>
    @Binds
    fun bindMessageTopicChangerReducer(reducer: MessageTopicChangerReducerElm): ScreenDslReducer<MessageTopicChangerEventElm, MessageTopicChangerEventElm.Ui, MessageTopicChangerEventElm.Internal, MessageTopicChangerStateElm, MessageTopicChangerEffectElm, MessageTopicChangerCommandElm>
    @Binds
    fun bindMessageTopicChangerDialogFactoryImpl(impl: MessageTopicChangerDialogFactoryImpl): MessageTopicChangerDialogFactory
}
