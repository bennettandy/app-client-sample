package biz.filmeroo.premier.detail

import biz.filmeroo.premier.api.ApiFilm
import biz.filmeroo.premier.main.FilmRepository
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test

class FilmDetailPresenterTest {

    private val repository: FilmRepository = mock()
    private val view: FilmDetailPresenter.View = mock()

    private lateinit var presenter: FilmDetailPresenter

    @Before
    fun setUp() {
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        presenter = FilmDetailPresenter(repository)
    }

    @Test
    fun `should fetch movie details and similar movies`() {
        val movie = mock<ApiFilm>()
        whenever(repository.fetchMovie(2)).thenReturn(Single.just(movie))

        presenter.loadMovie(view, 2)

        verify(view).displayMovie(movie)
    }
}