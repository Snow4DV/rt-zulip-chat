package ru.snowadv.chat_presentation.di.dagger

import android.content.Context
import coil.ImageLoader
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.noties.markwon.Markwon
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.coil.CoilImagesPlugin
import ru.snowadv.chat_presentation.R
import ru.snowadv.chat_presentation.api.ChatScreenFactory
import ru.snowadv.chat_presentation.chat.presentation.elm.ChatActorElm
import ru.snowadv.chat_presentation.chat.presentation.elm.ChatCommandElm
import ru.snowadv.chat_presentation.chat.presentation.elm.ChatEffectElm
import ru.snowadv.chat_presentation.chat.presentation.elm.ChatEventElm
import ru.snowadv.chat_presentation.chat.presentation.elm.ChatReducerElm
import ru.snowadv.chat_presentation.chat.presentation.elm.ChatStateElm
import ru.snowadv.chat_presentation.chat.ui.elm.ChatEffectUiElm
import ru.snowadv.chat_presentation.chat.ui.elm.ChatElmUiMapper
import ru.snowadv.chat_presentation.chat.ui.elm.ChatEventUiElm
import ru.snowadv.chat_presentation.chat.ui.elm.ChatStateUiElm
import ru.snowadv.chat_presentation.emoji_chooser.presentation.elm.EmojiChooserActorElm
import ru.snowadv.chat_presentation.emoji_chooser.presentation.elm.EmojiChooserCommandElm
import ru.snowadv.chat_presentation.emoji_chooser.presentation.elm.EmojiChooserEffectElm
import ru.snowadv.chat_presentation.emoji_chooser.presentation.elm.EmojiChooserEventElm
import ru.snowadv.chat_presentation.emoji_chooser.presentation.elm.EmojiChooserReducerElm
import ru.snowadv.chat_presentation.emoji_chooser.presentation.elm.EmojiChooserStateElm
import ru.snowadv.chat_presentation.chat.ui.feature.ChatScreenFactoryImpl
import ru.snowadv.chat_presentation.chat.ui.markwon.RelativeUrlMarkwonPlugin
import ru.snowadv.chat_presentation.emoji_chooser.ui.elm.EmojiChooserEffectUiElm
import ru.snowadv.chat_presentation.emoji_chooser.ui.elm.EmojiChooserElmUiMapper
import ru.snowadv.chat_presentation.emoji_chooser.ui.elm.EmojiChooserEventUiElm
import ru.snowadv.chat_presentation.emoji_chooser.ui.elm.EmojiChooserStateUiElm
import ru.snowadv.presentation.elm.ElmMapper
import ru.snowadv.presentation.util.DateFormatter
import ru.snowadv.presentation.util.DateTimeFormatter
import ru.snowadv.presentation.util.impl.DayDateFormatter
import ru.snowadv.presentation.util.impl.LocalizedDateTimeFormatter
import vivid.money.elmslie.core.store.Actor
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer

@Module
internal interface ChatPresentationModule {
    @Binds
    fun bindChatActorElm(chatActorElm: ChatActorElm): Actor<ChatCommandElm, ChatEventElm>
    @Binds
    fun bindChatReducerElm(chatReducerElm: ChatReducerElm): ScreenDslReducer<ChatEventElm, ChatEventElm.Ui, ChatEventElm.Internal, ChatStateElm, ChatEffectElm, ChatCommandElm>
    @Binds
    fun bindChatScreenFactoryImpl(chatScreenFactoryImpl: ChatScreenFactoryImpl): ChatScreenFactory
    @Binds
    fun bindEmojiChooserActorElm(emojiChooserActorElm: EmojiChooserActorElm): Actor<EmojiChooserCommandElm, EmojiChooserEventElm>
    @Binds
    fun bindEmojiChooserReducer(emojiChooserReducerElm: EmojiChooserReducerElm): ScreenDslReducer<EmojiChooserEventElm, EmojiChooserEventElm.Ui, EmojiChooserEventElm.Internal, EmojiChooserStateElm, EmojiChooserEffectElm, EmojiChooserCommandElm>
    @Binds
    fun bindChatElmMapperImpl(chatElmUiMapper: ChatElmUiMapper): ElmMapper<ChatStateElm, ChatEffectElm, ChatEventElm, ChatStateUiElm, ChatEffectUiElm, ChatEventUiElm>
    @Binds
    fun bindEmojiChooserElmMapperImpl(emojiChooserElmUiMapper: EmojiChooserElmUiMapper): ElmMapper<EmojiChooserStateElm, EmojiChooserEffectElm, EmojiChooserEventElm, EmojiChooserStateUiElm, EmojiChooserEffectUiElm, EmojiChooserEventUiElm>

    companion object {
        @Provides
        @Reusable
        fun provideSplitterDateFormatter(appContext: Context): DateFormatter {
            return DayDateFormatter(appContext)
        }

        @Provides
        @Reusable
        fun provideLocalizedDateTimeFormatter(appContext: Context): DateTimeFormatter {
            return LocalizedDateTimeFormatter(appContext)
        }

        @Provides
        @Reusable
        fun provideMarkwon(
            appContext: Context,
            coilImageLoader: ImageLoader,
            relativeUrlMarkwonPlugin: RelativeUrlMarkwonPlugin,
        ): Markwon {
            return Markwon.builder(appContext)
                .usePlugin(relativeUrlMarkwonPlugin)
                .usePlugin(JLatexMathPlugin.create(appContext.resources.getDimension(R.dimen.message_box_text_size)))
                .usePlugin(StrikethroughPlugin.create())
                .usePlugin(TablePlugin.create(appContext))
                .usePlugin(HtmlPlugin.create())
                .usePlugin(CoilImagesPlugin.create(appContext, coilImageLoader))
                .build()
        }
    }

}
