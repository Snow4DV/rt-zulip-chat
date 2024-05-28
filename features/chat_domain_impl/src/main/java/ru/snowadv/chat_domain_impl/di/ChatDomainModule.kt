package ru.snowadv.chat_domain_impl.di

import dagger.Binds
import dagger.Module
import ru.snowadv.chat_domain_api.use_case.AddReactionUseCase
import ru.snowadv.chat_domain_api.use_case.GetCurrentMessagesUseCase
import ru.snowadv.chat_domain_api.use_case.GetEmojisUseCase
import ru.snowadv.chat_domain_api.use_case.ListenToChatEventsUseCase
import ru.snowadv.chat_domain_api.use_case.LoadMoreMessagesUseCase
import ru.snowadv.chat_domain_api.use_case.RemoveReactionUseCase
import ru.snowadv.chat_domain_api.use_case.SendFileUseCase
import ru.snowadv.chat_domain_api.use_case.SendMessageUseCase
import ru.snowadv.chat_domain_impl.use_case.AddReactionUseCaseImpl
import ru.snowadv.chat_domain_impl.use_case.GetCurrentMessagesUseCaseImpl
import ru.snowadv.chat_domain_impl.use_case.GetEmojisUseCaseImpl
import ru.snowadv.chat_domain_impl.use_case.ListenToChatEventsUseCaseImpl
import ru.snowadv.chat_domain_impl.use_case.LoadMoreMessagesUseCaseImpl
import ru.snowadv.chat_domain_impl.use_case.RemoveReactionUseCaseImpl
import ru.snowadv.chat_domain_impl.use_case.SendFileUseCaseImpl
import ru.snowadv.chat_domain_impl.use_case.SendMessageUseCaseImpl

@Module
internal interface ChatDomainModule {
    @Binds
    abstract fun bindAddReactionUseCase(addReactionUseCaseImpl: AddReactionUseCaseImpl): AddReactionUseCase

    @Binds
    abstract fun bindGetCurrentMessagesUseCase(getCurrentMessagesUseCaseImpl: GetCurrentMessagesUseCaseImpl): GetCurrentMessagesUseCase

    @Binds
    abstract fun bindGetEmojisUseCase(getEmojisUseCaseImpl: GetEmojisUseCaseImpl): GetEmojisUseCase

    @Binds
    abstract fun bindListenToChatEventsUseCase(listenToChatEventsUseCaseImpl: ListenToChatEventsUseCaseImpl): ListenToChatEventsUseCase

    @Binds
    abstract fun bindLoadMoreMessagesUseCase(loadMoreMessagesUseCaseImpl: LoadMoreMessagesUseCaseImpl): LoadMoreMessagesUseCase

    @Binds
    abstract fun bindRemoveReactionUseCase(removeReactionUseCaseImpl: RemoveReactionUseCaseImpl): RemoveReactionUseCase

    @Binds
    abstract fun bindSendFileUseCase(sendFileUseCaseImpl: SendFileUseCaseImpl): SendFileUseCase

    @Binds
    abstract fun bindSendMessageUseCase(sendMessageUseCaseImpl: SendMessageUseCaseImpl): SendMessageUseCase
}