package biz.filmeroo.premier.main

import biz.filmeroo.premier.support.Fixtures
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test

class FilmPresenterTest {

    private val repository: FilmRepository = mock()
    private val view: FilmPresenter.View = mock()

    private lateinit var presenter: FilmPresenter

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        presenter = FilmPresenter(repository)
    }

    @Test
    fun `binding fetches and displays list`() {
        val results = Fixtures.filmList()
        whenever(repository.fetchTopRated()).thenReturn(Single.just(results))

        presenter.start(view)

        verify(view).displayResults(results)
    }

    @Test
    fun `errors fetching results are displayed`() {
        whenever(repository.fetchTopRated()).thenReturn(Single.error(Exception()))

        presenter.start(view)

        verify(view).displayError()
    }
}