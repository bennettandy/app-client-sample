package biz.filmeroo.premier.support

import biz.filmeroo.premier.api.ApiFilm
import biz.filmeroo.premier.api.ApiFilmListResponse

object Fixtures {

    fun film(id: Long): ApiFilm {
        return ApiFilm(id, "Waterworld", "Kevin Costner on a boat", "Tag-line", "/123.jpg", releaseDate = "16 Apr")
    }

    fun filmList(): List<ApiFilm> {
        return listOf(film(123), film(456))
    }

    fun filmResponse(): ApiFilmListResponse {
        return ApiFilmListResponse(filmList())
    }
}