package com.devotion.quoteapp.data.repository

import com.devotion.quoteapp.data.datasource.QuoteDataSource
import com.devotion.quoteapp.data.mapper.toQuotes
import com.devotion.quoteapp.data.model.Quote
import com.devotion.quoteapp.utils.ResultWrapper
import com.devotion.quoteapp.utils.proceedFlow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

interface QuoteRepository {
    suspend fun getRandomQuotes(): Flow<ResultWrapper<List<Quote>>>
}

class QuoteRepositoryImpl(private val dataSource: QuoteDataSource) : QuoteRepository {
    override suspend fun getRandomQuotes(): Flow<ResultWrapper<List<Quote>>> {
        return proceedFlow {
            dataSource.getRandomQuotes().toQuotes()
        }.onStart {
            emit(ResultWrapper.Loading())
            delay(2000)
        }
    }
}