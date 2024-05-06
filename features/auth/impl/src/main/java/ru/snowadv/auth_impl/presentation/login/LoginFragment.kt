package ru.snowadv.auth_impl.presentation.login

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import ru.snowadv.auth_impl.databinding.FragmentLoginBinding
import ru.snowadv.auth_impl.di.AuthFeatureComponentHolder
import ru.snowadv.auth_impl.presentation.login.elm.LoginEffectElm
import ru.snowadv.auth_impl.presentation.login.elm.LoginEventElm
import ru.snowadv.auth_impl.presentation.login.elm.LoginStateElm
import ru.snowadv.presentation.activity.hideKeyboard
import ru.snowadv.presentation.elm.BaseFragment
import ru.snowadv.presentation.fragment.ElmFragmentRenderer
import ru.snowadv.presentation.fragment.ErrorHandlingFragment
import ru.snowadv.presentation.fragment.impl.SnackbarErrorHandlingFragment
import vivid.money.elmslie.android.renderer.elmStoreWithRenderer
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

internal class LoginFragment : BaseFragment<LoginEventElm, LoginEffectElm, LoginStateElm>(),
    ElmFragmentRenderer<LoginFragment, FragmentLoginBinding, LoginEventElm, LoginEffectElm, LoginStateElm> by LoginFragmentRenderer(),
    ErrorHandlingFragment by SnackbarErrorHandlingFragment() {

    companion object {
        fun newInstance(): Fragment = LoginFragment()
    }


    private var _binding: FragmentLoginBinding? = null
    private val binding get() = requireNotNull(_binding) {"Binding wasn't initialized"}
    override val store: Store<LoginEventElm, LoginEffectElm, LoginStateElm> by elmStoreWithRenderer(elmRenderer = this) {
        AuthFeatureComponentHolder.getComponent().loginStoreFactory.create()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        return FragmentLoginBinding.inflate(inflater, container, false).also { _binding = it }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onRendererViewCreated(binding, store)
    }

    override fun render(state: LoginStateElm) {
        renderStateByRenderer(state, binding)
    }

    override fun handleEffect(effect: LoginEffectElm) {
        handleEffectByRenderer(effect, binding, store)
    }
}