package ru.snowadv.chat_domain_impl.di

import dagger.Binds
import dagger.Module
import ru.snowadv.chat_domain_api.use_case.AddReactionUseCase
import ru.snowadv.chat_domain_api.use_case.GetCurrentMessagesUseCase
import ru.snowadv.chat_domain_api.use_case.GetEmojisUseCase
import ru.snowadv.chat_domain_api.use_case.ListenToChatEventsUseCase
import ru.snowadv.chat_domain_api.use_case.LoadMessageUseCase
import ru.snowadv.chat_domain_api.use_case.LoadMoreMessagesUseCase
import ru.snowadv.chat_domain_api.use_case.RemoveReactionUseCase
import ru.snowadv.chat_domain_api.use_case.SendFileUseCase
import ru.snowadv.chat_domain_api.use_case.SendMessageUseCase
import ru.snowadv.chat_domain_impl.use_case.AddReactionUseCaseImpl
import ru.snowadv.chat_domain_impl.use_case.GetCurrentMessagesUseCaseImpl
import ru.snowadv.chat_domain_impl.use_case.GetEmojisUseCaseImpl
import ru.snowadv.chat_domain_impl.use_case.ListenToChatEventsUseCaseImpl
import ru.snowadv.chat_domain_impl.use_case.LoadMessageUseCaseImpl
import ru.snowadv.chat_domain_impl.use_case.LoadMoreMessagesUseCaseImpl
import ru.snowadv.chat_domain_impl.use_case.RemoveReactionUseCaseImpl
import ru.snowadv.chat_domain_impl.use_case.SendFileUseCaseImpl
import ru.snowadv.chat_domain_impl.use_case.SendMessageUseCaseImpl

@Module
internal interface ChatDomainModule {
    @Binds
    fun bindAddReactionUseCaseImpl(addReactionUseCaseImpl: AddReactionUseCaseImpl): AddReactionUseCase

    @Binds
    fun bindGetCurrentMessagesUseCaseImpl(getCurrentMessagesUseCaseImpl: GetCurrentMessagesUseCaseImpl): GetCurrentMessagesUseCase

    @Binds
    fun bindGetEmojisUseCaseImpl(getEmojisUseCaseImpl: GetEmojisUseCaseImpl): GetEmojisUseCase

    @Binds
    fun bindListenToChatEventsUseCaseImpl(listenToChatEventsUseCaseImpl: ListenToChatEventsUseCaseImpl): ListenToChatEventsUseCase

    @Binds
    fun bindLoadMoreMessagesUseCaseImpl(loadMoreMessagesUseCaseImpl: LoadMoreMessagesUseCaseImpl): LoadMoreMessagesUseCase

    @Binds
    fun bindRemoveReactionUseCaseImpl(removeReactionUseCaseImpl: RemoveReactionUseCaseImpl): RemoveReactionUseCase

    @Binds
    fun bindSendFileUseCaseImpl(sendFileUseCaseImpl: SendFileUseCaseImpl): SendFileUseCase

    @Binds
    fun bindSendMessageUseCaseImpl(sendMessageUseCaseImpl: SendMessageUseCaseImpl): SendMessageUseCase

    @Binds
    fun bindLoadMessageUseCaseImpl(loadMessageUseCaseImpl: LoadMessageUseCaseImpl): LoadMessageUseCase
}