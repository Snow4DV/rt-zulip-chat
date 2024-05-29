package ru.snowadv.chat_presentation.chat.ui.markwon

import android.view.View
import androidx.core.net.toUri
import dagger.Reusable
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.LinkResolverDef
import io.noties.markwon.MarkwonConfiguration
import okhttp3.HttpUrl.Companion.toHttpUrl
import ru.snowadv.model.BaseUrlProvider
import javax.inject.Inject

@Reusable
internal class RelativeUrlLinkResolverPlugin @Inject constructor(private val baseUrlProvider: BaseUrlProvider) : AbstractMarkwonPlugin() {
    override fun configureConfiguration(builder: MarkwonConfiguration.Builder) {
        builder.linkResolver(RelativeUrlLinkResolver(baseUrlProvider))
    }
    private class RelativeUrlLinkResolver constructor(private val baseUrlProvider: BaseUrlProvider): LinkResolverDef() {
        override fun resolve(view: View, link: String) {
            val uri = link.toUri()
            when {
                uri.isRelative -> {
                    baseUrlProvider.baseUrl.toHttpUrl().resolve(link)?.toString()?.let { newLink ->
                        super.resolve(view, newLink)
                    }
                }
                else  -> super.resolve(view,link)
            }
        }
    }
}

