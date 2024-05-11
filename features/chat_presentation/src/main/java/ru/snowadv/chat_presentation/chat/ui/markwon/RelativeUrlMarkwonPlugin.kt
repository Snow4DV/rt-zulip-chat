package ru.snowadv.chat_presentation.chat.ui.markwon

import dagger.Reusable
import io.noties.markwon.AbstractMarkwonPlugin
import io.noties.markwon.MarkwonConfiguration
import io.noties.markwon.image.destination.ImageDestinationProcessorRelativeToAbsolute
import javax.inject.Inject

@Reusable
internal class RelativeUrlMarkwonPlugin @Inject constructor(): AbstractMarkwonPlugin() {

    companion object {
        const val BASE_URL = "https://tinkoff-android-spring-2024.zulipchat.com/"
    }

    override fun configureConfiguration(builder: MarkwonConfiguration.Builder) {
        builder.imageDestinationProcessor(ImageDestinationProcessorRelativeToAbsolute(BASE_URL))
    }
}