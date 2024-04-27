package ru.snowadv.voiceapp.glue.injector

import android.content.Context
import ru.snowadv.auth_data.api.AuthDataRepository
import ru.snowadv.channels_data.api.StreamDataRepository
import ru.snowadv.channels_data.api.TopicDataRepository
import ru.snowadv.emojis_data.api.EmojiDataRepository
import ru.snowadv.event_api.repository.EventRepository
import ru.snowadv.message_data.api.MessageDataRepository
import ru.snowadv.module_injector.dependency_holder.BaseDependencyHolder
import ru.snowadv.module_injector.dependency_holder.DependencyHolder0
import ru.snowadv.module_injector.dependency_holder.DependencyHolder1
import ru.snowadv.module_injector.module.BaseModuleDependencies
import ru.snowadv.profile.di.holder.ProfileFeatureComponentHolder
import ru.snowadv.profile.di.holder.ProfileFeatureDependencies
import ru.snowadv.profile.domain.navigation.ProfileRouter
import ru.snowadv.profile.domain.repository.ProfileRepository
import ru.snowadv.users_data.api.UserDataRepository
import ru.snowadv.voiceapp.di.holder.AppModuleAPI
import ru.snowadv.voiceapp.di.holder.AppModuleComponentHolder
import ru.snowadv.voiceapp.di.holder.AppModuleDependencies
import ru.snowadv.voiceapp.di.legacy.MainGraph

/**
 * This injector injects dependencies into other modules' component holders
 */
internal object ModulesInjector {

    fun inject(context: Context) {
        AppModuleComponentHolder.dependencyProvider = {
            class AppModuleDependencyHolder(
                override val block: (BaseDependencyHolder<AppModuleDependencies>) -> AppModuleDependencies
            ) : DependencyHolder0<AppModuleDependencies>()

            AppModuleDependencyHolder { dependencyHolder ->
                object : AppModuleDependencies {
                    override val authDataRepository: AuthDataRepository = MainGraph.mainDepsProvider.authDataRepository // TODO: temporary solution until everything is rewritten with dagger
                    override val streamDataRepository: StreamDataRepository = MainGraph.mainDepsProvider.streamDataRepository
                    override val topicDataRepository: TopicDataRepository = MainGraph.mainDepsProvider.topicDataRepository
                    override val emojiDataRepository: EmojiDataRepository = MainGraph.mainDepsProvider.emojiDataRepository
                    override val eventRepository: EventRepository = MainGraph.mainDepsProvider.eventDataRepository
                    override val messageDataRepository: MessageDataRepository = MainGraph.mainDepsProvider.messageDataRepository
                    override val userDataRepository: UserDataRepository = MainGraph.mainDepsProvider.userDataRepository
                    override val appContext: Context = context
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder
                }
            }.dependencies
        }

        ProfileFeatureComponentHolder.dependencyProvider = {
            class ProfileFeatureDependencyHolder(
                override val block: (BaseDependencyHolder<ProfileFeatureDependencies>, AppModuleAPI) -> ProfileFeatureDependencies
            ) : DependencyHolder1<AppModuleAPI, ProfileFeatureDependencies>(
                api1 = AppModuleComponentHolder.get(),
            )

            ProfileFeatureDependencyHolder { dependencyHolder, appApi ->
                object : ProfileFeatureDependencies {
                    override val router: ProfileRouter = appApi.profileRouter
                    override val repo: ProfileRepository = appApi.profileRepo
                    override val eventRepo: EventRepository = MainGraph.mainDepsProvider.eventDataRepository
                    override val dependencyHolder: BaseDependencyHolder<out BaseModuleDependencies> = dependencyHolder

                }
            }.dependencies
        }
    }
}