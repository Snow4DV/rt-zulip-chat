package ru.snowadv.events_impl.data.exception

import java.io.IOException

class UnableToObtainQueueException: IOException("Can't obtain event queue because it is outdated & was garbage-collected by server")