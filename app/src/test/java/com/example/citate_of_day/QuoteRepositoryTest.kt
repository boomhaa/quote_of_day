package com.example.citate_of_day

import com.example.citate_of_day.data.Quote
import com.example.citate_of_day.data.QuoteApi
import com.example.citate_of_day.repository.QuoteRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class QuoteRepositoryTest {

    @Mock
    private lateinit var mockApi: QuoteApi

    private lateinit var repository: QuoteRepository

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        repository = QuoteRepository(mockApi)
    }

    @Test
    fun `getRandomQuote returns random quote when api returns quotes`() = runTest {
        // Arrange
        val quotes = listOf(
            Quote("Test quote 1", "Author 1"),
            Quote("Test quote 2", "Author 2")
        )
        `when`(mockApi.getQuotes()).thenReturn(quotes)

        // Act
        val result = repository.getRandomQuote()

        // Assert
        assert(quotes.contains(result))
    }

    @Test
    fun `getRandomQuote returns system quote when api returns empty list`() = runTest {
        // Arrange
        `when`(mockApi.getQuotes()).thenReturn(emptyList())

        // Act
        val result = repository.getRandomQuote()

        // Assert
        assertEquals("No quotes available", result.q)
        assertEquals("System", result.a)
    }

    @Test
    fun `getRandomQuote returns system quote when api returns null`() = runTest {
        // Arrange
        `when`(mockApi.getQuotes()).thenReturn(null)

        // Act
        val result = repository.getRandomQuote()

        // Assert
        assertEquals("No quotes available", result.q)
        assertEquals("System", result.a)
    }
}
