package ru.snowadv.auth_presentation.login

import android.content.Context
import android.text.TextWatcher
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import ru.snowadv.auth_presentation.R
import ru.snowadv.auth_presentation.databinding.FragmentLoginBinding
import ru.snowadv.auth_presentation.login.elm.LoginEffectElm
import ru.snowadv.auth_presentation.login.elm.LoginEventElm
import ru.snowadv.auth_presentation.login.elm.LoginStateElm
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import ru.snowadv.presentation.view.EditTextUtils.afterTextChanged
import ru.snowadv.presentation.view.setTextIfChanged
import ru.snowadv.presentation.view.setTextIfEmpty
import ru.snowadv.presentation.view.setTextRemovingTextWatcherIfChanged
import vivid.money.elmslie.core.store.Store

internal class LoginFragmentRenderer :
    ElmFragmentRenderer<LoginFragment, FragmentLoginBinding, LoginEventElm, LoginEffectElm, LoginStateElm> {

    private var _emailTextWatcher: TextWatcher? = null
    private val emailTextWatcher get() = requireNotNull(_emailTextWatcher) { "Email text watcher wasn't initialized" }
    private var _passwordTextWatcher: TextWatcher? = null
    private val passwordTextWatcher get() = requireNotNull(_passwordTextWatcher) { "Password text watcher wasn't initialized" }



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
        binding.email.setTextRemovingTextWatcherIfChanged(state.email, emailTextWatcher)
        binding.password.setTextRemovingTextWatcherIfChanged(state.password, passwordTextWatcher)

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
        binding.email.afterTextChanged { email ->
            store.accept(LoginEventElm.Ui.ChangedEmailField(email))
        }.also { _emailTextWatcher = it }
        binding.password.afterTextChanged { password ->
            store.accept(LoginEventElm.Ui.ChangedPasswordField(password))
        }.also { _passwordTextWatcher = it }
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

    override fun LoginFragment.onDestroyRendererView() {
        _emailTextWatcher = null
        _passwordTextWatcher = null
    }
}