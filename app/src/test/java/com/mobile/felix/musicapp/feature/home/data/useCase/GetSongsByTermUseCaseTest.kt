package com.mobile.felix.musicapp.feature.home.data.useCase

import com.mobile.felix.musicapp.core.domain.Failure
import com.mobile.felix.musicapp.core.domain.Result
import com.mobile.felix.musicapp.feature.home.domain.repository.HomeRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetSongsByTermUseCaseTest {

    private val repository: HomeRepository = mockk()
    private lateinit var useCase: GetSongsByTermUseCase

    @Before
    fun setUp() {
        useCase = GetSongsByTermUseCase(repository)
    }

    @Test
    fun `invoke should return success when repository saves songs`() = runTest {
        coEvery { repository.getSongsByTerm("rock") } returns Result.Success(true)

        val result = useCase.invoke("rock")

        assertEquals(Result.Success(true), result)
        coVerify(exactly = 1) { repository.getSongsByTerm("rock") }
    }

    @Test
    fun `invoke should return network error when repository fails with network error`() = runTest {
        coEvery { repository.getSongsByTerm("rock") } returns Result.Error(Failure.NetworkError)

        val result = useCase.invoke("rock")

        assertEquals(Result.Error(Failure.NetworkError), result)
    }

    @Test
    fun `invoke should return unknown error when repository fails with unknown error`() = runTest {
        coEvery { repository.getSongsByTerm("rock") } returns Result.Error(Failure.Unknown)

        val result = useCase.invoke("rock")

        assertEquals(Result.Error(Failure.Unknown), result)
    }

    @Test
    fun `invoke should delegate query to repository`() = runTest {
        val query = "linkin park"
        coEvery { repository.getSongsByTerm(query) } returns Result.Success(true)

        useCase.invoke(query)

        coVerify { repository.getSongsByTerm(query) }
    }
}
