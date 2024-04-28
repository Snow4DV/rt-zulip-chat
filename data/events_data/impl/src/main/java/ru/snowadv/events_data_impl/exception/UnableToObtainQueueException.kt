package ru.snowadv.events_data_impl.exception

import java.io.IOException

class UnableToObtainQueueException: IOException("Can't obtain event queue because it is outdated & was garbage-collected by server")