package ru.snowadv.chat_presentation.chat.ui.markwon

import dagger.Reusable
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.image.destination.ImageDestinationProcessorRelativeToAbsolute
import ru.snowadv.model.BaseUrlProvider
import javax.inject.Inject

@Reusable
internal class RelativeUrlMarkwonPlugin @Inject constructor(private val baseUrlProvider: BaseUrlProvider): AbstractMarkwonPlugin() {

    override fun configureConfiguration(builder: MarkwonConfiguration.Builder) {
        builder.imageDestinationProcessor(ImageDestinationProcessorRelativeToAbsolute(baseUrlProvider.baseUrl))
    }
}