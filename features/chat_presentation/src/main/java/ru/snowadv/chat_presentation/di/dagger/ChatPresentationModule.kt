package ru.snowadv.chat_presentation.di.dagger

import android.content.Context
import coil.ImageLoader
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.noties.markwon.LinkResolverDef
import io.noties.markwon.Markwon
import io.noties.markwon.ext.latex.JLatexMathPlugin
import io.noties.markwon.ext.strikethrough.StrikethroughPlugin
import io.noties.markwon.ext.tables.TablePlugin
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.coil.CoilImagesPlugin
import io.noties.markwon.movement.MovementMethodPlugin
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
import ru.snowadv.chat_presentation.chat.ui.feature.ChatScreenFactoryImpl
import ru.snowadv.chat_presentation.chat.ui.markwon.CoilImagesStore
import ru.snowadv.chat_presentation.chat.ui.markwon.NoLongClickLinkMovementMethod
import ru.snowadv.chat_presentation.chat.ui.markwon.RelativeUrlLinkResolverPlugin
import ru.snowadv.chat_presentation.chat.ui.markwon.RelativeUrlMarkwonPlugin
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
    fun bindChatElmMapperImpl(chatElmUiMapper: ChatElmUiMapper): ElmMapper<ChatStateElm, ChatEffectElm, ChatEventElm, ChatStateUiElm, ChatEffectUiElm, ChatEventUiElm>

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
            relativeUrlMarkwonPlugin: RelativeUrlMarkwonPlugin,
            linkMovementMethod: NoLongClickLinkMovementMethod,
            coilImagesPlugin: CoilImagesPlugin,
            relativeUrlLinkResolver: RelativeUrlLinkResolverPlugin
        ): Markwon {
            return Markwon.builder(appContext)
                .usePlugin(relativeUrlMarkwonPlugin)
                .usePlugin(MovementMethodPlugin.create(linkMovementMethod))
                .usePlugin(JLatexMathPlugin.create(appContext.resources.getDimension(R.dimen.message_box_text_size)))
                .usePlugin(StrikethroughPlugin.create())
                .usePlugin(TablePlugin.create(appContext))
                .usePlugin(relativeUrlLinkResolver)
                .usePlugin(HtmlPlugin.create())
                .usePlugin(coilImagesPlugin)
                .build()
        }

        @Provides
        @Reusable
        fun provideCoilImagesPlugin(
            coilImageLoader: ImageLoader,
            coilImagesStore: CoilImagesStore,
        ): CoilImagesPlugin {
            return CoilImagesPlugin.create(
                coilImagesStore, coilImageLoader
            )
        }
    }

}
