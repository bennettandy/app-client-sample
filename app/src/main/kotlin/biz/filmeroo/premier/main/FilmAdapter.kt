package biz.filmeroo.premier.main

import androidx.annotation.NonNull
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import biz.filmeroo.premier.R
import biz.filmeroo.premier.api.ApiFilm
import biz.filmeroo.premier.api.FilmService
import biz.filmeroo.premier.base.inflate
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_film.view.*
import javax.inject.Inject

internal class FilmAdapter @Inject constructor(private val picasso: Picasso)
    : ListAdapter<ApiFilm, FilmAdapter.Holder>(FilmAdapter.diffCallback) {

    init {
        setHasStableIds(true)
    }

    private var onClick: ((Long) -> Unit)? = null

    fun setOnClick(onClick: (Long) -> Unit) {
        this.onClick = onClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = Holder(parent.inflate(R.layout.item_film))

    override fun onBindViewHolder(holder: Holder, position: Int) = holder.bind(getItem(position))

    override fun getItemId(position: Int) = getItem(position).id

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: ApiFilm) {
            itemView.title.text = item.title
            itemView.overview.text = item.overview
            picasso.load(FilmService.buildImageUrl(item.posterPath)).into(itemView.image)
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