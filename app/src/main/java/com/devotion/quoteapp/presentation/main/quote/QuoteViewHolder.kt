package com.devotion.quoteapp.presentation.main.quote

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.devotion.quoteapp.R
import com.devotion.quoteapp.data.model.Quote
import com.devotion.quoteapp.databinding.ItemQuoteBinding
import com.devotion.quoteapp.utils.ViewHolderBinder

class QuoteViewHolder(private val binding: ItemQuoteBinding) : ViewHolder(binding.root),
    ViewHolderBinder<Quote> {
    override fun bind(item: Quote) {
        item.let{
            binding.tvQuote.text = itemView.rootView.context.getString(R.string.text_quote, it.quote)
            binding.tvCharacter.text = it.character
            binding.tvAnime.text = it.anime
        }
    }
}
