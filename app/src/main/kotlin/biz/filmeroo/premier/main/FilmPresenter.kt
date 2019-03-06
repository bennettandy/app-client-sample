package biz.filmeroo.premier.main

import biz.filmeroo.premier.api.ApiFilm
import biz.filmeroo.premier.base.Presenter
import biz.filmeroo.premier.base.PresenterView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

internal class FilmPresenter @Inject constructor(private val filmRepository: FilmRepository) : Presenter<FilmPresenter.View>() {

    override fun start(view: View) {
        super.start(view)

        addSubscription(filmRepository.fetchTopRated()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { list -> view.displayResults(list) },
                        { view.displayError() }
                ))
    }

    internal interface View : PresenterView {
        fun displayResults(results: List<ApiFilm>)
        fun displayError()
    }
}