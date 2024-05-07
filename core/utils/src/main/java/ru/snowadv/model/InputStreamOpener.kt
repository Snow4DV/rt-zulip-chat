package ru.snowadv.model

import java.io.InputStream

fun interface InputStreamOpener {
    fun openInputStream(): InputStream?
}