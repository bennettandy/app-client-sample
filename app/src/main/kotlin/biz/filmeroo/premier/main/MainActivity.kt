package biz.filmeroo.premier.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.widget.Toast
import biz.filmeroo.premier.R
import biz.filmeroo.premier.api.ApiFilm
import biz.filmeroo.premier.detail.DetailActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity(), FilmPresenter.View {

    @Inject internal lateinit var presenter: FilmPresenter
    @Inject internal lateinit var adapter: FilmAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setupView()
        presenter.start(this)
    }

    private fun setupView() {
        setContentView(R.layout.activity_main)
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
        adapter.setOnClick { id -> DetailActivity.start(this, id) }
    }

    override fun onDestroy() {
        presenter.stop()
        super.onDestroy()
    }

    override fun displayResults(results: List<ApiFilm>) {
        adapter.submitList(results)
    }

    override fun displayError() {
        Toast.makeText(this, R.string.connection_error, Toast.LENGTH_SHORT).show()
    }
}
