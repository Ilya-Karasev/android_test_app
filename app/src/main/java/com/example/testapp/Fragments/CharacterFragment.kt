package com.example.testapp.Fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testapp.CharacterAdapter
import com.example.testapp.CharactersViewModel
import com.example.testapp.R
import com.example.testapp.databinding.FragmentCharacterBinding
import com.example.testapp.User
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CharacterFragment : Fragment() {
    private var _binding: FragmentCharacterBinding? = null
    private val binding get() = _binding ?: throw IllegalStateException("Binding should not be accessed after destroying view")
    private val viewModel: CharactersViewModel by viewModels()

    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCharacterBinding.inflate(inflater, container, false)
        binding.recyclerViewCharacters.layoutManager = LinearLayoutManager(requireContext())

        val args = CharacterFragmentArgs.fromBundle(requireArguments())
        user = args.user

        val adapter = CharacterAdapter()
        binding.recyclerViewCharacters.adapter = adapter

        // Подписка на обновления списка персонажей с пагинацией
        lifecycleScope.launch {
            viewModel.characters
                .collectLatest { characters ->
                    adapter.submitList(characters)
                }
        }

        lifecycleScope.launch {
            viewModel.isOnFirstPage.collectLatest { isOnFirstPage ->
                binding.btnPreviousPage.isEnabled = !isOnFirstPage
            }
        }

        lifecycleScope.launch {
            viewModel.isOnLastPage.collectLatest { isOnLastPage ->
                binding.btnNextPage.isEnabled = !isOnLastPage
            }
        }

        lifecycleScope.launch {
            viewModel.currentPageFlow.collectLatest { page ->
                binding.btnCurrentPage.text = page.toString()
            }
        }

        binding.btnNextPage.setOnClickListener {
            lifecycleScope.launch {
                binding.btnNextPage.isEnabled = false
                viewModel.loadCharactersFromDatabase(page = ++viewModel.currentPage)
                binding.btnNextPage.isEnabled = true
            }
        }

        binding.btnPreviousPage.setOnClickListener {
            lifecycleScope.launch {
                binding.btnPreviousPage.isEnabled = false
                viewModel.loadCharactersFromDatabase(page = --viewModel.currentPage)
                binding.btnPreviousPage.isEnabled = true
            }
        }

        binding.btnRefresh.setOnClickListener {
            lifecycleScope.launch {
                viewModel.clearCache()
                viewModel.loadAllCharactersFromApi()
            }
        }

        binding.btnLogout.setOnClickListener {
            val action = CharacterFragmentDirections.actionCharacterFragmentToSignInFragment(user)
            findNavController().navigate(action)
        }

        binding.btnProfile.setOnClickListener {
            val action = CharacterFragmentDirections.actionCharacterFragmentToProfileFragment(user)
            findNavController().navigate(action)
        }

        binding.btnHome.setOnClickListener {
            val action = CharacterFragmentDirections.actionCharacterFragmentToHomeFragment(user)
            findNavController().navigate(action)
        }

        lifecycleScope.launch {
            val hasData = viewModel.checkLocalDatabase()
            if (!hasData) {
                viewModel.loadAllCharactersFromApi()
            }
            viewModel.loadCharactersFromDatabase(page = 1)
        }

        binding.btnCurrentPage.setOnClickListener {
            showPageSelectionDialog()
        }

        return binding.root
    }

    private fun showPageSelectionDialog() {
        lifecycleScope.launch {
            val totalPages = viewModel.getTotalPages()
            var selectedPage = viewModel.currentPage

            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Выбор страницы")
                .setView(R.layout.dialog_select_page)
                .setPositiveButton("OK") { _, _ ->
                    if (selectedPage != viewModel.currentPage) {
                        viewModel.loadCharactersFromDatabase(page = selectedPage)
                    }
                }
                .setNegativeButton("Отмена", null)
                .create()

            dialog.setOnShowListener {
                val pageNumberView = dialog.findViewById<TextView>(R.id.page_number_view)
                val btnIncrease = dialog.findViewById<Button>(R.id.btn_increase_page)
                val btnDecrease = dialog.findViewById<Button>(R.id.btn_decrease_page)

                pageNumberView?.text = selectedPage.toString()

                btnIncrease?.setOnClickListener {
                    if (selectedPage < totalPages) {
                        selectedPage++
                        pageNumberView?.text = selectedPage.toString()
                    }
                }

                btnDecrease?.setOnClickListener {
                    if (selectedPage > 1) {
                        selectedPage--
                        pageNumberView?.text = selectedPage.toString()
                    }
                }
            }

            dialog.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

//        val directory = requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?:
//        requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)