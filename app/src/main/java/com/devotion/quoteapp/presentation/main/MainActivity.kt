package com.devotion.quoteapp.presentation.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.devotion.quoteapp.R
import com.devotion.quoteapp.data.datasource.QuoteApiDataSource
import com.devotion.quoteapp.data.datasource.QuoteDataSource
import com.devotion.quoteapp.data.repository.QuoteRepository
import com.devotion.quoteapp.data.repository.QuoteRepositoryImpl
import com.devotion.quoteapp.data.source.network.services.QuoteApiServices
import com.devotion.quoteapp.databinding.ActivityMainBinding
import com.devotion.quoteapp.presentation.main.adapter.QuoteAdapter
import com.devotion.quoteapp.utils.GenericViewModelFactory
import com.devotion.quoteapp.utils.proceedWhen

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val adapter: QuoteAdapter by lazy {
        QuoteAdapter()
    }

    private val viewModel: MainViewModel by viewModels {
        val s = QuoteApiServices.invoke()
        val ds: QuoteDataSource = QuoteApiDataSource(s)
        val rp: QuoteRepository = QuoteRepositoryImpl(ds)
        GenericViewModelFactory.create(MainViewModel(rp))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupRecyclerView()
        getQuotes()
        observeData()
    }

    private fun setupRecyclerView() {
        binding.rvQuote.adapter = adapter
    }

    private fun getQuotes() {
        viewModel.getRandomQuotes()
    }

    private fun observeData() {
        viewModel.responseLiveData.observe(this) { result ->
            result.proceedWhen(
                doOnLoading = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = true
                    binding.layoutState.tvError.isVisible = false
                    binding.rvQuote.isVisible = false
                },
                doOnSuccess = {
                    binding.layoutState.root.isVisible = false
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = false
                    binding.rvQuote.isVisible = true
                    result.payload?.let { result ->
                        adapter.submitData(result)
                    }
                },
                doOnError = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    binding.layoutState.tvError.text = result.exception?.message.orEmpty()
                    binding.rvQuote.isVisible = false
                },
                doOnEmpty = {
                    binding.layoutState.root.isVisible = true
                    binding.layoutState.pbLoading.isVisible = false
                    binding.layoutState.tvError.isVisible = true
                    binding.layoutState.tvError.text = getString(R.string.text_cart_is_empty)
                    binding.rvQuote.isVisible = false
                }
            )
        }
    }
}