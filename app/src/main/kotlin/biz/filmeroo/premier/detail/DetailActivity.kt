package biz.filmeroo.premier.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import biz.filmeroo.premier.R
import biz.filmeroo.premier.api.ApiFilm
import biz.filmeroo.premier.api.FilmService
import com.squareup.picasso.Picasso
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_detail.*
import javax.inject.Inject
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DetailActivity : AppCompatActivity(), FilmDetailPresenter.View {

    @Inject internal lateinit var presenter: FilmDetailPresenter
    @Inject internal lateinit var picasso: Picasso

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        presenter.start(this)
        presenter.loadMovie(this, intent.extras?.getLong(EXTRA_ID, -1) ?: -1)
    }

    override fun displayMovie(movie: ApiFilm) {
        picasso.load(FilmService.buildImageUrl(movie.posterPath)).into(film_image)
        film_title.text = movie.title
        film_tagline.text = movie.tagline

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        try {
            val convertedDate = dateFormat.parse(movie.releaseDate)
            film_date.text = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(convertedDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
    }

    override fun displayError() {
        Toast.makeText(this, R.string.connection_error, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        presenter.stop()
        super.onDestroy()
    }

    companion object {
        const val EXTRA_ID = "extra_id"

        fun start(origin: Activity, id: Long) {
            origin.startActivity(Intent(origin, DetailActivity::class.java).apply {
                putExtra(EXTRA_ID, id)
            })
        }
    }

}
