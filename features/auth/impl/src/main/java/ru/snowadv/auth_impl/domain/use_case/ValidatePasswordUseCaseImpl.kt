package ru.snowadv.auth_impl.domain.use_case

import dagger.Reusable
import ru.snowadv.auth_api.domain.use_case.ValidatePasswordUseCase
import javax.inject.Inject

@Reusable
internal class ValidatePasswordUseCaseImpl @Inject constructor(): ValidatePasswordUseCase {
    /**
     * We can't know for sure how security model is configured on the server.
     * That's why we'll just check if it is not blank
     */
    override fun invoke(password: String): Boolean {
        return password.isNotBlank()
    }
}