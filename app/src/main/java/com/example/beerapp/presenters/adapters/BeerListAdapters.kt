package com.example.beerapp.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import coil.request.LoadRequestBuilder
import coil.transform.CircleCropTransformation
import com.example.beerapp.R
import com.example.beerapp.databinding.BeerListItemBinding
import com.example.data.database.DatabaseBeer
import com.example.domain.models.BaseBeer
import com.example.domain.models.asDomainBaseModel

class BeerListAdapter(val clickListener: DetailBeerListener) :
    PagedListAdapter<DatabaseBeer, BeerListAdapter.ViewHolder>(BeerDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    class ViewHolder private constructor(val binding: BeerListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = BeerListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }

        fun bind(
            item: DatabaseBeer?,
            clickListener: DetailBeerListener
        ) {
            binding.beerName.text = item?.name
            binding.beerTagline.text = item?.tagline
            binding.beerImage.load(uri = item?.imageUrl)
            binding.beer = item?.asDomainBaseModel()
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }
}

class BeerDiffCallback : DiffUtil.ItemCallback<DatabaseBeer>() {
    override fun areItemsTheSame(oldItem: DatabaseBeer, newItem: DatabaseBeer): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DatabaseBeer, newItem: DatabaseBeer): Boolean {
        return oldItem.equals(newItem)
    }
}

class DetailBeerListener(val clickListener: (beerId: Long) -> Unit) {
    fun onClick(beer: BaseBeer) = clickListener(beer.id)
}

@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String?) {
    if(imageUrl.isNullOrEmpty()){
        view.load(R.drawable.beer_bottle)
    } else {
        view.load(imageUrl)
    }
}
