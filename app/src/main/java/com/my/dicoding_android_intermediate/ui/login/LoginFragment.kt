package com.my.dicoding_android_intermediate.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.my.dicoding_android_intermediate.R
import com.my.dicoding_android_intermediate.databinding.FragmentLoginBinding
import com.my.dicoding_android_intermediate.ui.main.MainActivity
import com.my.dicoding_android_intermediate.utils.Utils.Companion.TOKEN
import com.my.dicoding_android_intermediate.utils.animateVisibility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var loginJob: Job = Job()
    private val viewModel: LoginFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenCreated {
            launch {
                viewModel.getAuthToken().collect { authToken ->
                    if (!authToken.isNullOrEmpty()) {
                        Intent(requireContext(), MainActivity::class.java).also { intent ->
                            intent.putExtra(TOKEN, authToken)
                            startActivity(intent)
                            requireActivity().finish()
                        }
                    }
                }
            }
        }
        setActions()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setActions() {
        binding.apply {
            register.setOnClickListener {
                view -> view.findNavController().navigate(R.id.action_loginFragment_to_registerFragment3)
            }
            masuk.setOnClickListener {
                login()
            }
        }
    }

    private fun login() {
        val username = binding.username.text.toString().trim()
        val password = binding.password.text.toString().trim()
        setLoadingState(true)

        lifecycleScope.launchWhenResumed {
            if (loginJob.isActive) loginJob.cancel()
            loginJob = launch {
                viewModel.loginUser(username, password).collect { result ->
                    result.onSuccess { credentials ->
                        credentials.loginResult?.token?.let {
                            token -> viewModel.saveToken(token)
                            Intent(requireContext(), MainActivity::class.java).also { intent ->
                                intent.putExtra(TOKEN, token)
                                startActivity(intent)
                                requireActivity().finish()
                            }
                        }
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.loginSuccess),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    result.onFailure {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.loginError),
                            Snackbar.LENGTH_SHORT
                        ).show()

                        setLoadingState(false)
                    }
                }
            }
        }
    }

    private fun setLoadingState(isLoading: Boolean) {
        binding.apply {
            username.isEnabled = !isLoading
            password.isEnabled = !isLoading
            masuk.isEnabled = !isLoading

            if (isLoading) {
                viewLoading.animateVisibility(true)
            } else {
                viewLoading.animateVisibility(false)
            }
        }
    }


}