package ru.snowadv.auth_domain_impl.use_case

import dagger.Reusable
import javax.inject.Inject

@Reusable
internal class ValidatePasswordUseCaseImpl @Inject constructor():
    ru.snowadv.auth_domain_api.use_case.ValidatePasswordUseCase {
    /**
     * We can't know for sure how security model is configured on the server.
     * That's why we'll just check if it is not blank
     */
    override fun invoke(password: String): Boolean {
        return password.isNotBlank()
    }
}