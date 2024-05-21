package ru.snowadv.chat_impl.presentation.file_chooser

import android.app.Activity
import android.content.Intent
import android.webkit.MimeTypeMap
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import ru.snowadv.model.InputStreamOpener
import ru.snowadv.presentation.file.FileUtils.getInputStreamOpener
import java.io.InputStream

internal object FragmentFileChooser {
    fun <T: Fragment> T.registerFileChooserLauncher(
        onSuccess: (mimeType: String?, inputStreamOpener: InputStreamOpener, extension: String?) -> Unit,
        onFailure: () -> Unit,
    ) = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val uri = result.data?.data
        val ctx = context
        if (result.resultCode == Activity.RESULT_OK && uri != null && ctx != null) {
            val mimeType = ctx.contentResolver.getType(uri)
            val extension = MimeTypeMap.getSingleton()?.getExtensionFromMimeType(mimeType)
            ctx.contentResolver.getInputStreamOpener(uri).let { opener ->
                onSuccess(mimeType, opener, extension)
            }
        } else {
            onFailure()
        }
    }
}