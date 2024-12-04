package com.example.testapp.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.testapp.User
import com.example.testapp.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding should not be accessed after destroying view")
    private val TAG = "SignUpFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        Log.d(TAG, "вызов onCreate")

        binding.etUsername.addTextChangedListener(textWatcher)
        binding.etEmail.addTextChangedListener(textWatcher)
        binding.etPassword.addTextChangedListener(textWatcher)

        binding.btnRegister.setOnClickListener {
            if (binding.btnRegister.isEnabled) {
                val username = binding.etUsername.text.toString()
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()

                val user = User(username, email, password)

                val action = SignUpFragmentDirections.actionSignUpFragmentToSignInFragment(user)
                findNavController().navigate(action)
            }
        }

        binding.tvReturn.setOnClickListener {
            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment(User("", "", "")))
        }

        return binding.root
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            updateRegisterButtonState()
        }
        override fun afterTextChanged(s: Editable?) {}
    }

    private fun updateRegisterButtonState() {
        val isUsernameValid = binding.etUsername.text.length >= 8
        val isEmailValid = binding.etEmail.text.length >= 8
        val isPasswordValid = binding.etPassword.text.length >= 8
        binding.btnRegister.isEnabled = isUsernameValid && isEmailValid && isPasswordValid
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "вызов onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "вызов onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "вызов onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "вызов onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "вызов onDestroy")
    }
}