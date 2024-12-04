package com.example.testapp.Fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.testapp.User
import com.example.testapp.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private val TAG = "ProfileFragment"
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding should not be accessed after destroying view")

    private lateinit var user: User

    @SuppressLint("MissingInflatedId", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        Log.d(TAG, "вызов onCreate")

        val args = ProfileFragmentArgs.fromBundle(requireArguments())
        user = args.user

        val maskedPassword = "*".repeat(user.password.length)

        binding.tvEmail.text = "Эл.почта: ${user.email}"
        binding.tvPassword.text = "Пароль: $maskedPassword"

        binding.btnHome.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToHomeFragment(user)
            findNavController().navigate(action)
        }

        return binding.root
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d(TAG, "вызов onDestroyView")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "вызов onDestroy")
    }
}