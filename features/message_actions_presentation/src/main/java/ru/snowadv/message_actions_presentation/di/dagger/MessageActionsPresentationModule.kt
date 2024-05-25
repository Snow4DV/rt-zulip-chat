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
}
