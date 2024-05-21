package ru.snowadv.auth_impl.presentation.login

import android.content.Context
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import ru.snowadv.auth_impl.R
import ru.snowadv.auth_impl.databinding.FragmentLoginBinding
import ru.snowadv.auth_impl.presentation.login.elm.LoginEffectElm
import ru.snowadv.auth_impl.presentation.login.elm.LoginEventElm
import ru.snowadv.auth_impl.presentation.login.elm.LoginStateElm
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import ru.snowadv.presentation.view.setTextIfChanged
import vivid.money.elmslie.core.store.Store

internal class LoginFragmentRenderer :
    ElmFragmentRenderer<LoginFragment, FragmentLoginBinding, LoginEventElm, LoginEffectElm, LoginStateElm> {


    override fun LoginFragment.onRendererViewCreated(
        binding: FragmentLoginBinding,
        store: Store<LoginEventElm, LoginEffectElm, LoginStateElm>
    ) {
        initListeners(store, binding)
    }

    override fun LoginFragment.renderStateByRenderer(
        state: LoginStateElm,
        binding: FragmentLoginBinding
    ) {
        binding.email.setTextIfChanged(state.email)
        binding.password.setTextIfChanged(state.password)

        binding.buttonLogin.isEnabled = !state.loading

        binding.actionProgressBar.isVisible = state.loading
    }

    override fun LoginFragment.handleEffectByRenderer(
        effect: LoginEffectElm,
        binding: FragmentLoginBinding,
        store: Store<LoginEventElm, LoginEffectElm, LoginStateElm>
    ) {
        when (effect) {
            is LoginEffectElm.ShowInternetErrorWithRetry -> {
                showErrorWithRetryAndCustomText(
                    rootView = binding.root,
                    action = {
                        store.accept(LoginEventElm.Ui.OnLoginButtonClicked)
                    },
                    textResId = R.string.auth_error,
                )
            }

            is LoginEffectElm.ShowValidationError -> {
                if(effect.invalidEmail) {
                    showErrorInTextInput(
                        textInputLayout = binding.emailBox,
                        textInputEditText = binding.email,
                        errorResId = R.string.wrong_email,
                        context = requireContext(),
                    )
                }

                if(effect.invalidPassword) {
                    showErrorInTextInput(
                        textInputLayout = binding.passwordBox,
                        textInputEditText = binding.password,
                        errorResId = R.string.wrong_password,
                        context = requireContext(),
                    )
                }
            }
        }
    }

    private fun initListeners(
        store: Store<LoginEventElm, LoginEffectElm, LoginStateElm>,
        binding: FragmentLoginBinding,
    ) {
        binding.buttonLogin.setOnClickListener {
            store.accept(LoginEventElm.Ui.OnLoginButtonClicked)
        }
        binding.email.addTextChangedListener {
            it?.toString()?.let { email ->
                store.accept(LoginEventElm.Ui.ChangedEmailField(email))
            }
        }
        binding.password.addTextChangedListener {
            it?.toString()?.let { password ->
                store.accept(LoginEventElm.Ui.ChangedPasswordField(password))
            }
        }
    }

    private fun showErrorInTextInput(
        textInputLayout: TextInputLayout,
        textInputEditText: TextInputEditText,
        errorResId: Int,
        context: Context,
    ) {
        textInputLayout.isErrorEnabled = true
        textInputEditText.error = context.getString(errorResId)
    }
}