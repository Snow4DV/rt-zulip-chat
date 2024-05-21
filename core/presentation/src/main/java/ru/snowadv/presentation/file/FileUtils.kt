package ru.snowadv.presentation.file

import android.content.ContentResolver
import android.net.Uri
import ru.snowadv.model.InputStreamOpener

object FileUtils {
    fun ContentResolver.getInputStreamOpener(uri: Uri): InputStreamOpener {
        return InputStreamOpener {
            openInputStream(uri)
        }
    }
}