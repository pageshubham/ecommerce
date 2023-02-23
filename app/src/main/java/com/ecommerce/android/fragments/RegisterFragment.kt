package com.ecommerce.android.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ecommerce.android.R
import com.ecommerce.android.databinding.FragmentRegisterBinding
import com.ecommerce.android.data.User
import com.ecommerce.android.util.RegisterValidation
import com.ecommerce.android.util.Resource
import com.ecommerce.android.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.txtLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.apply {
            btnRegister.setOnClickListener {
                val user = User(
                    edtFirst.text.toString().trim(),
                    edtLast.text.toString().trim(),
                    edtEmail.text.toString().trim()
                )
                val password = edtPassword.text.toString().trim()
                viewModel.createAccountWithEmailAndPassword(user, password)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.register.collectLatest {
                when (it) {
                    is Resource.Loading -> {
                        binding.progressBar.isVisible = true
                    }
                    is Resource.Success -> {

                    }
                    is Resource.Error -> {

                    }
                    else -> Unit
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.validation.collectLatest {
                if (it.email is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.edtEmail.apply {
                            requestFocus()
                            error = it.email.message
                        }
                    }
                }

                if (it.password is RegisterValidation.Failed) {
                    withContext(Dispatchers.Main) {
                        binding.edtPassword.apply {
                            requestFocus()
                            error = it.password.message
                        }
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}