package biz.filmeroo.premier.detail

import android.util.Log
import biz.filmeroo.premier.api.ApiFilm
import biz.filmeroo.premier.base.Presenter
import biz.filmeroo.premier.base.PresenterView
import biz.filmeroo.premier.main.FilmRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

internal class FilmDetailPresenter @Inject constructor(private val filmRepository: FilmRepository) : Presenter<FilmDetailPresenter.View>() {

    fun loadMovie(view: View, movieId: Long) {
        addSubscription(filmRepository.fetchMovie(movieId)
                .zipWith(filmRepository.fetchSimilar(movieId)

                        // fallback if we have no similar movies
                        // todo: switchIfEmpty only available on Observable which implies there is probably a better way to replace empty List
                        .flatMap { films: List<ApiFilm> -> Observable.just(films)
                                .filter { it.isNotEmpty() }
                                // fetch top-rated as fallback if list is empty
                                .switchIfEmpty(filmRepository.fetchTopRated().toObservable())
                                .firstOrError()
                        }

                        .doOnError { t -> Log.e("FilmDetailPresenter", "Failed fetch movie", t) }

                        .onErrorReturn { emptyList() } // fallback to empty list if fetchSimilar or fetchTopRated fails

                        , BiFunction{ film: ApiFilm, similar: List<ApiFilm> -> Pair(film, similar)  })

                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { filmAndSimilarPair -> displayMovieResults(view, filmAndSimilarPair) },
                        { view.displayError() }
                ))
    }

    private fun displayMovieResults(view: View, moviewResults: Pair<ApiFilm, List<ApiFilm>>) =
            view.apply {
                displayMovie(moviewResults.first)
                displaySimilar(moviewResults.second)
            }

    internal interface View : PresenterView {
        fun displayMovie(movie: ApiFilm)
        fun displaySimilar(similarMovies: List<ApiFilm>)
        fun displayError()
    }
}