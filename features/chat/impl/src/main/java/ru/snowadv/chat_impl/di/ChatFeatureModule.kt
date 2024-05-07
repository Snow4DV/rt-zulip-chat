package ru.snowadv.chat_impl.di

import android.content.Context
import coil.ImageLoader
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.noties.markwon.Markwon
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.coil.CoilImagesPlugin
import io.noties.markwon.image.destination.ImageDestinationProcessor
import ru.snowadv.chat_impl.domain.repository.EmojiRepository
import ru.snowadv.chat_impl.domain.repository.MessageRepository
import ru.snowadv.chat_api.presentation.ChatScreenFactory
import ru.snowadv.chat_impl.R
import ru.snowadv.chat_impl.data.repository.ChatRepositoryImpl
import ru.snowadv.chat_impl.presentation.chat.elm.ChatActorElm
import ru.snowadv.chat_impl.presentation.chat.elm.ChatCommandElm
import ru.snowadv.chat_impl.presentation.chat.elm.ChatEffectElm
import ru.snowadv.chat_impl.presentation.chat.elm.ChatEventElm
import ru.snowadv.chat_impl.presentation.chat.elm.ChatReducerElm
import ru.snowadv.chat_impl.presentation.chat.elm.ChatStateElm
import ru.snowadv.chat_impl.presentation.chat.markwon.RelativeUrlMarkwonPlugin
import ru.snowadv.chat_impl.presentation.emoji_chooser.elm.EmojiChooserActorElm
import ru.snowadv.chat_impl.presentation.emoji_chooser.elm.EmojiChooserCommandElm
import ru.snowadv.chat_impl.presentation.emoji_chooser.elm.EmojiChooserEffectElm
import ru.snowadv.chat_impl.presentation.emoji_chooser.elm.EmojiChooserEventElm
import ru.snowadv.chat_impl.presentation.emoji_chooser.elm.EmojiChooserReducerElm
import ru.snowadv.chat_impl.presentation.emoji_chooser.elm.EmojiChooserStateElm
import ru.snowadv.chat_impl.presentation.feature.ChatScreenFactoryImpl
import ru.snowadv.presentation.util.DateFormatter
import ru.snowadv.presentation.util.DateTimeFormatter
import ru.snowadv.presentation.util.impl.DayDateFormatter
import ru.snowadv.presentation.util.impl.LocalizedDateTimeFormatter
import vivid.money.elmslie.core.store.Actor
import vivid.money.elmslie.core.store.dsl.ScreenDslReducer

@Module
internal interface ChatFeatureModule {
    @Binds
    fun bindChatScreenFactoryImpl(chatScreenFactoryImpl: ChatScreenFactoryImpl): ChatScreenFactory

    @Binds
    fun bindChatActorElm(chatActorElm: ChatActorElm): Actor<ChatCommandElm, ChatEventElm>

    @Binds
    fun bindChatReducerElm(chatReducerElm: ChatReducerElm): ScreenDslReducer<ChatEventElm, ChatEventElm.Ui, ChatEventElm.Internal, ChatStateElm, ChatEffectElm, ChatCommandElm>

    @Binds
    fun bindEmojiChooserActorElm(emojiChooserActorElm: EmojiChooserActorElm): Actor<EmojiChooserCommandElm, EmojiChooserEventElm>

    @Binds
    fun bindEmojiChooserReducerElm(emojiChooserReducerElm: EmojiChooserReducerElm): ScreenDslReducer<EmojiChooserEventElm, EmojiChooserEventElm.Ui, EmojiChooserEventElm.Internal, EmojiChooserStateElm, EmojiChooserEffectElm, EmojiChooserCommandElm>

    @Binds
    fun bindMessageRepoImpl(messageRepoImpl: ChatRepositoryImpl): MessageRepository

    @Binds
    fun bindEmojiRepoImpl(messageRepoImpl: ChatRepositoryImpl): EmojiRepository

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