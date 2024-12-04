package com.example.testapp.Fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testapp.Chat
import com.example.testapp.ChatList
import com.example.testapp.R
import com.example.testapp.User
import com.example.testapp.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding should not be accessed after destroying view")
    private val TAG = "HomeFragment"

    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        Log.d(TAG, "вызов onCreate")

        val args = HomeFragmentArgs.fromBundle(requireArguments())
        user = args.user

        binding.rvChatList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvChatList.adapter = ChatList(getChatList())

        binding.btnLogout.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSignInFragment(user)
            findNavController().navigate(action)
        }

        binding.btnProfile.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToProfileFragment(user)
            findNavController().navigate(action)
        }

        binding.btnSearch.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToCharacterFragment(user)
            findNavController().navigate(action)
        }

        binding.btnSettings.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSettingsFragment(user)
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun getChatList(): List<Chat> {
        return listOf(
            Chat("Алексей Иванов", "Привет, как дела?", "15:30", R.drawable.profile_image),
            Chat("Мария Петрова", "Давно не виделись!", "14:50", R.drawable.profile_image)
        )
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