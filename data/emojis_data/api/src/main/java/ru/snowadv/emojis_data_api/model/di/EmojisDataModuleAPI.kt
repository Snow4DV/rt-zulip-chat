package ru.snowadv.emojis_data_api.model.di

import ru.snowadv.emojis_data_api.model.EmojiDataRepository
import ru.snowadv.module_injector.module.BaseModuleAPI

interface EmojisDataModuleAPI : BaseModuleAPI {
    val emojiDataRepo: EmojiDataRepository
}