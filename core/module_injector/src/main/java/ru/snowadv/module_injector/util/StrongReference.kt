package ru.snowadv.module_injector.util

import java.lang.ref.Reference
import java.lang.ref.ReferenceQueue
import java.lang.ref.WeakReference

internal class StrongReference<T>(referent: T) : WeakReference<T>(referent) {
    private val strongReference = referent
}

