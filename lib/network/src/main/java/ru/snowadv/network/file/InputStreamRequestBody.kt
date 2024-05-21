package ru.snowadv.network.file

import android.content.ContentResolver
import android.net.Uri
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source
import ru.snowadv.model.InputStreamOpener
import java.io.IOException
import java.io.InputStream

class InputStreamRequestBody(
    private val mimeType: String?,
    private val inputStream: InputStreamOpener,
) : RequestBody() {

    override fun contentType(): MediaType? {
        return mimeType?.toMediaTypeOrNull()
    }

    override fun writeTo(sink: BufferedSink) {
        inputStream.openInputStream()?.source()?.use { source ->
            sink.writeAll(source)
        } ?: error("Input stream couldn't be opened")
    }
}
