package biz.filmeroo.premier.detail

import biz.filmeroo.premier.api.ApiFilm
import biz.filmeroo.premier.main.FilmRepository
import biz.filmeroo.premier.support.Fixtures
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.IOException

class FilmDetailPresenterTest {

    private val repository: FilmRepository = mock()
    private val view: FilmDetailPresenter.View = mock()

    private lateinit var presenter: FilmDetailPresenter

    @Before
    fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }

        presenter = FilmDetailPresenter(repository)
    }

    @After
    fun tearDown() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }

    @Test
    fun `should show error message when movie Api fails`() {
        val movie = mock<ApiFilm>()
        val filmList: List<ApiFilm>  = Fixtures.filmList();
        val filmListB: List<ApiFilm> = Fixtures.filmListB();
        whenever(movie.id).thenReturn(2)
        whenever(repository.fetchMovie(2)).thenReturn(Single.error(IOException("server on fire")))
        whenever(repository.fetchSimilar(2)).thenReturn(Single.just(filmList))
        whenever(repository.fetchTopRated()).thenReturn(Single.just(filmListB))

        presenter.loadMovie(view, 2)

        // not up to speed on this library with Mockito, would expect to test call counts and verify arguments
        verify(view, never()).displayMovie(movie)
        verify(view, never()).displaySimilar(filmList)
        verify(view, times(1)).displayError()
    }

    @Test
    fun `should fetch movie details and similar movies`() {
        val movie = mock<ApiFilm>()
        val filmList: List<ApiFilm>  = Fixtures.filmList();
        val filmListB: List<ApiFilm> = Fixtures.filmListB();
        whenever(movie.id).thenReturn(2)
        whenever(repository.fetchMovie(2)).thenReturn(Single.just(movie))
        whenever(repository.fetchSimilar(2)).thenReturn(Single.just(filmList))
        whenever(repository.fetchTopRated()).thenReturn(Single.just(filmListB))

        presenter.loadMovie(view, 2)

        // not up to speed on this library with Mockito, would expect to test call counts and verify arguments
        verify(view, times(1)).displayMovie(movie)
        verify(view, times(1)).displaySimilar(filmList)
        verify(view, never()).displayError()
    }

    @Test
    fun `should fetch movie details and similar from fallback if no similar movies exist`() {
        val movie = mock<ApiFilm>()
        val filmListB: List<ApiFilm> = Fixtures.filmListB();
        whenever(movie.id).thenReturn(2)
        whenever(repository.fetchMovie(2)).thenReturn(Single.just(movie))
        whenever(repository.fetchSimilar(2)).thenReturn(Single.just(emptyList()))
        whenever(repository.fetchTopRated()).thenReturn(Single.just(filmListB))

        presenter.loadMovie(view, 2)

        // not up to speed on this library with Mockito, would expect to test call counts and verify arguments
        verify(view, times(1)).displayMovie(movie)
        verify(view, times(1)).displaySimilar(filmListB)
        verify(view, never()).displayError()
    }

    @Test
    fun `should fetch movie details and similar returns empty list in event of API failure from similar`() {
        val movie = mock<ApiFilm>()
        val filmList: List<ApiFilm> = emptyList()
        val filmListB: List<ApiFilm> = Fixtures.filmListB();
        whenever(movie.id).thenReturn(2)
        whenever(repository.fetchMovie(2)).thenReturn(Single.just(movie))
        whenever(repository.fetchSimilar(2)).thenReturn(Single.error(IOException("something failed")))
        whenever(repository.fetchTopRated()).thenReturn(Single.just(filmListB))

        presenter.loadMovie(view, 2)

        // not up to speed on this library with Mockito, would expect to test call counts and verify arguments
        verify(view, times(1)).displayMovie(movie)
        verify(view, times(1)).displaySimilar(filmList)
        verify(view, never()).displayError()
    }

}