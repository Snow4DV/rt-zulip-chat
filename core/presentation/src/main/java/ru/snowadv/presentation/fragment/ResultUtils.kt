package ru.snowadv.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.webkit.MimeTypeMap
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import ru.snowadv.model.InputStreamOpener
import ru.snowadv.presentation.file.FileUtils.getInputStreamOpener

object ResultUtils {
    fun registerChooserUriLauncher(
        fragment: Fragment,
        onSuccess: (mimeType: String?, opener: InputStreamOpener, extension: String?) -> Unit,
        onFailure: () -> Unit,
    ): ActivityResultLauncher<Intent> = with(fragment) {
        return registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val uri = result.data?.data
            val appCtx = context?.applicationContext // Use application context to prevent leaks!
            if (result.resultCode == Activity.RESULT_OK && uri != null && appCtx != null) {
                val mimeType = appCtx.contentResolver.getType(uri)
                val extension = MimeTypeMap.getSingleton()?.getExtensionFromMimeType(mimeType)

                appCtx.contentResolver.getInputStreamOpener(uri).let { inputStream ->
                    onSuccess(mimeType, inputStream, extension)
                }
            } else {
                onFailure()
            }
        }
    }
}