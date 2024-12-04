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
import com.example.testapp.R
import com.example.testapp.User
import com.example.testapp.databinding.FragmentSignInBinding

class SignInFragment : Fragment() {
    private val TAG = "SignInFragment"
    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding should not be accessed after destroying view")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)

        val args = SignInFragmentArgs.fromBundle(requireArguments())
        updateSignInButtonState()

        binding.etEmail.addTextChangedListener(textWatcher)
        binding.etPassword.addTextChangedListener(textWatcher)

        binding.flSignInButton.setOnClickListener {
            if (binding.flSignInButton.isEnabled) {
                val email = binding.etEmail.text.toString()
                val password = binding.etPassword.text.toString()
                val username = args.user?.username

                val user = username?.let { it1 -> User(it1, email, password) }

                val action = user?.let { it1 ->
                    SignInFragmentDirections.actionSignInFragmentToHomeFragment(
                        it1
                    )
                }
                if (action != null) {
                    findNavController().navigate(action)
                }
            }
        }

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
        }

        return binding.root
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            updateSignInButtonState()
        }
        override fun afterTextChanged(s: Editable?) {}
    }

    private fun updateSignInButtonState() {
        val isEmailValid = binding.etEmail.text.length >= 8
        val isPasswordValid = binding.etPassword.text.length >= 8

        binding.viewSignInBackground.setBackgroundResource(
            if (isEmailValid && isPasswordValid) R.drawable.rounded_rectangle
            else R.drawable.rounded_rectangle_gray
        )
        binding.flSignInButton.isEnabled = isEmailValid && isPasswordValid
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