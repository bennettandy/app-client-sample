package biz.filmeroo.premier.detail

import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import biz.filmeroo.premier.R
import biz.filmeroo.premier.api.ApiFilm
import biz.filmeroo.premier.api.FilmService
import biz.filmeroo.premier.base.inflate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.movie_thumbnail.view.*
import javax.inject.Inject

class SimilarFilmAdapter @Inject constructor(private val picasso: Picasso)
    : ListAdapter<ApiFilm, SimilarFilmAdapter.Holder>(diffCallback) {

    init {
        setHasStableIds(true)
    }

    private var onClick: ((Long) -> Unit)? = null

    fun setOnClick(onClick: (Long) -> Unit) {
        this.onClick = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(parent.inflate(R.layout.movie_thumbnail))

    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(getItem(position))

    override fun getItemId(position: Int) = getItem(position).id

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: ApiFilm) {
            picasso.load(FilmService.buildImageUrl(item.posterPath)).into(itemView.film_image)
            itemView.setOnClickListener { onClick?.invoke(item.id) }
        }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<ApiFilm>() {
            override fun areItemsTheSame(@NonNull old: ApiFilm, @NonNull new: ApiFilm) = old.id == new.id
            override fun areContentsTheSame(@NonNull old: ApiFilm, @NonNull new: ApiFilm) = old == new
        }
    }
}