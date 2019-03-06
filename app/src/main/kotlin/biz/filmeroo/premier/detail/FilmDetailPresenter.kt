package biz.filmeroo.premier.detail

import biz.filmeroo.premier.api.ApiFilm
import biz.filmeroo.premier.base.Presenter
import biz.filmeroo.premier.base.PresenterView
import biz.filmeroo.premier.main.FilmRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

internal class FilmDetailPresenter @Inject constructor(private val filmRepository: FilmRepository) : Presenter<FilmDetailPresenter.View>() {

    fun loadMovie(view: View, movieId: Long) {
        addSubscription(filmRepository.fetchMovie(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { movie -> view.displayMovie(movie) },
                    { view.displayError() }
                ))
    }

    internal interface View : PresenterView {
        fun displayMovie(movie: ApiFilm)
        fun displayError()
    }
}