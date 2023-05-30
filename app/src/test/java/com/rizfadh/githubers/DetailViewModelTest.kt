package com.rizfadh.githubers

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rizfadh.githubers.data.local.entity.FavoriteEntity
import com.rizfadh.githubers.data.repository.UserRepository
import com.rizfadh.githubers.ui.detail.DetailViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`

class DetailViewModelTest {

    private lateinit var repository: UserRepository
    private val userDummy = FavoriteEntity("a", "b")

    @ExperimentalCoroutinesApi
    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @Before
    fun before() {
        repository = mock(UserRepository::class.java)
        Dispatchers.setMain(testDispatcher)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun isUserFavoriteExist() = runTest {
        `when`(repository.isFavorite("")).thenReturn(userDummy)

        val viewModel = DetailViewModel(repository)
        viewModel.isFavorite("")
        testDispatcher.scheduler.advanceUntilIdle()
        Mockito.verify(repository).isFavorite("")
        val result = viewModel.isUserFavorite.getOrAwaitValue()

        Assert.assertEquals(userDummy, result)
    }

    @ExperimentalCoroutinesApi
    @After
    fun after() {
        Dispatchers.resetMain()
    }
}