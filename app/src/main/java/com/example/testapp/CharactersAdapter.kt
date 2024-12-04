package com.example.testapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.testapp.databinding.ItemCharacterBinding

class CharacterAdapter : RecyclerView.Adapter<CharacterAdapter.CharacterViewHolder>() {

    private val characters = mutableListOf<CharacterEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val binding = ItemCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CharacterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        holder.bind(characters[position])
    }

    override fun getItemCount(): Int = characters.size

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(newCharacters: List<CharacterEntity>) {
        characters.clear()
        characters.addAll(newCharacters)
        notifyDataSetChanged()
    }

    class CharacterViewHolder(private val binding: ItemCharacterBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(character: CharacterEntity) {
            binding.tvName.text = "Имя: ${character.name}"
            binding.tvCulture.text = "Национальность: ${character.culture}"
            binding.tvBorn.text = "Информация о рождении: ${character.born}"
            binding.tvTitles.text = "Титулы: ${character.titles}"
            binding.tvAliases.text = "Прозвища: ${character.aliases}"
            binding.tvPlayedBy.text = "Актёр / актриса: ${character.playedBy}"
        }
    }
}