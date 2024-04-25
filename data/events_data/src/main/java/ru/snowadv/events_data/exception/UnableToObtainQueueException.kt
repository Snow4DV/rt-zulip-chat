package ru.snowadv.events_data.exception

class UnableToObtainQueueException: Exception("Can't obtain event queue because it is outdated & was garbage-collected by server")