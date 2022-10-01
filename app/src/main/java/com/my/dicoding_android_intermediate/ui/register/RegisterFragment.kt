package com.my.dicoding_android_intermediate.ui.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.my.dicoding_android_intermediate.R
import com.my.dicoding_android_intermediate.databinding.FragmentRegisterBinding
import com.my.dicoding_android_intermediate.utils.animateVisibility
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private var loginJob: Job = Job()
    private val viewModel: RegisterFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setActions()
    }

    private fun setActions() {
        binding.apply {
            backLogin.setOnClickListener { view ->
                view.findNavController().navigate(R.id.action_registerFragment_to_loginFragment2)
            }
            register.setOnClickListener {
                handleRegister(email, fullName, password)
            }
        }
    }

    private fun handleRegister(email: EditText, fullName: EditText, password: EditText) {
        val myEmail = email.text.toString().trim()
        val myName = fullName.text.toString().trim()
        val myPassword = password.text.toString().trim()
        setLoadingState(true)

        lifecycleScope.launchWhenResumed {
            if (loginJob.isActive) loginJob.cancel()
            loginJob = launch {
                viewModel.registerUser(myName, myEmail, myPassword).collect { result ->
                    result.onSuccess { value ->
                        Toast.makeText(
                            requireContext(),
                            getString(R.string.registerSuccess),
                            Toast.LENGTH_SHORT
                        ).show()
                        findNavController().navigate(R.id.action_registerFragment_to_loginFragment2)
                    }
                    result.onFailure {
                        Snackbar.make(
                            binding.root,
                            getString(R.string.registerError),
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
            email.isEnabled = !isLoading
            password.isEnabled = !isLoading
            fullName.isEnabled = !isLoading
            register.isEnabled = !isLoading

            if (isLoading) {
                viewLoading.animateVisibility(true)
            } else {
                viewLoading.animateVisibility(false)
            }
        }
    }


}