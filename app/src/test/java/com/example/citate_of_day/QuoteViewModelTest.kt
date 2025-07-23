package com.example.citate_of_day.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.citate_of_day.data.Quote
import com.example.citate_of_day.repository.QuoteRepository
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.HttpException
import app.cash.turbine.test

@ExperimentalCoroutinesApi
class QuoteViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var mockRepository: QuoteRepository
    private lateinit var viewModel: QuoteViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        mockRepository = mockk()
        Dispatchers.setMain(testDispatcher)
        mockkStatic("android.util.Log")
        every { android.util.Log.d(any(), any()) } returns 0
        every { android.util.Log.e(any(), any()) } returns 0
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkAll()
    }

    @Test
    fun `init loads quote when autoLoadInit is true`() = runTest {
        val testQuote = Quote("Test quote", "Test author")
        coEvery { mockRepository.getRandomQuote() } returns testQuote
        viewModel = QuoteViewModel(mockRepository, autoLoadInit = true)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(testQuote, viewModel.quote.value)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `loadRandomQuote shows rate limit message when called too frequently`() = runTest {
        val testQuote = Quote("Test quote", "Test author")
        coEvery { mockRepository.getRandomQuote() } returns testQuote
        viewModel = QuoteViewModel(mockRepository, requestInterval = 5000L, autoLoadInit = false)

        viewModel.loadRandomQuote()
        testDispatcher.scheduler.advanceUntilIdle()
        viewModel.loadRandomQuote()

        assertEquals("Please, wait a few time before a new quote", viewModel.quote.value?.q)
        assertEquals("System", viewModel.quote.value?.a)
    }

    @Test
    fun `loadRandomQuote handles 429 HTTP error`() = runTest {

        val mockHttpException = mockk<HttpException>()
        every { mockHttpException.code() } returns 429
        coEvery { mockRepository.getRandomQuote() } throws mockHttpException
        viewModel = QuoteViewModel(mockRepository, autoLoadInit = false)

        viewModel.loadRandomQuote()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("The limit of requests to the server has been exceeded. Please try again later..", viewModel.quote.value?.q)
        assertEquals("System", viewModel.quote.value?.a)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `loadRandomQuote handles general exception`() = runTest {

        val exception = RuntimeException("Network error")
        coEvery { mockRepository.getRandomQuote() } throws exception
        viewModel = QuoteViewModel(mockRepository, autoLoadInit = false)

        viewModel.loadRandomQuote()
        testDispatcher.scheduler.advanceUntilIdle()

        assert(viewModel.quote.value?.q?.contains("An error occurred when uploading a quote") == true)
        assertEquals("System", viewModel.quote.value?.a)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `loadRandomQuote loads quote successfully`() = runTest {

        val testQuote = Quote("Test quote", "Test author")
        coEvery { mockRepository.getRandomQuote() } returns testQuote
        viewModel = QuoteViewModel(mockRepository, autoLoadInit = false)


        viewModel.loadRandomQuote()
        testDispatcher.scheduler.advanceUntilIdle()


        assertEquals(testQuote, viewModel.quote.value)
        assertFalse(viewModel.isLoading.value)
    }

    @Test
    fun `loadRandomQuote sets loading state correctly with turbine`() = runTest {

        val testQuote = Quote("Test quote", "Test author")
        coEvery { mockRepository.getRandomQuote() } returns testQuote
        viewModel = QuoteViewModel(mockRepository, autoLoadInit = false)

        viewModel.isLoading.test {
            assertEquals(false, awaitItem())

            viewModel.loadRandomQuote()
            assertEquals(true, awaitItem())
            assertEquals(false, awaitItem())
        }
    }
    @Test
    fun `init does not load quote when autoLoadInit is false`() = runTest {

        viewModel = QuoteViewModel(mockRepository, autoLoadInit = false)
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(null, viewModel.quote.value)
        assertFalse(viewModel.isLoading.value)
    }
}