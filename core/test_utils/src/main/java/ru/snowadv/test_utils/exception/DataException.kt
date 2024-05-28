package ru.snowadv.test_utils.exception

import java.io.IOException

/**
 * This exception can be compared in tests by equality of ID
 */
data class DataException(private val id: Int = 1) : IOException()