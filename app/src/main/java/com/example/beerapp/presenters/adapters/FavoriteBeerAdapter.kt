package com.example.beerapp.presenters.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.example.beerapp.databinding.FavoriteBeerItemBinding
import com.example.data.database.FavoriteBeer
import com.example.domain.models.DetailBeer
import com.example.domain.models.asDomainDetailBeer

class FavoriteBeerAdapter(val clickListener: FavoriteBeerListener) :
    PagedListAdapter<FavoriteBeer, FavoriteBeerAdapter.FavoriteBeerViewHolder>(
        FavoriteBeerDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteBeerViewHolder {
        return FavoriteBeerViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: FavoriteBeerViewHolder, position: Int) {
        val favBeer = getItem(position)
        holder.bind(favBeer, clickListener)
    }

    class FavoriteBeerViewHolder private constructor(val binding: FavoriteBeerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun from(parent: ViewGroup): FavoriteBeerViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoriteBeerItemBinding.inflate(layoutInflater, parent, false)
                return FavoriteBeerViewHolder(binding)
            }
        }

        fun bind(item: FavoriteBeer?, clickListener: FavoriteBeerListener){
            binding.clickListener = clickListener
            binding.favoriteBeer = item?.asDomainDetailBeer()
            binding.favBeerImage.load(uri = item?.imageUrl)
            binding.favBeerName.text = item?.name
            binding.favBeerTagline.text = item?.tagline
            binding.favBeerDescription.text = item?.description
            binding.abvValueText.text = item?.abv.toString()
            binding.ibuValueText.text = item?.ibu.toString()
            binding.ebcValueText.text = item?.ebc.toString()
//            TODO when add foods in DetailBeer as property
//             binding.foodsText.text = item.foods
        }
    }
}

class FavoriteBeerDiffCallback : DiffUtil.ItemCallback<FavoriteBeer>() {
    override fun areItemsTheSame(oldItem: FavoriteBeer, newItem: FavoriteBeer): Boolean {
        return oldItem.favBeerId.id == newItem.favBeerId.id
    }

    override fun areContentsTheSame(oldItem: FavoriteBeer, newItem: FavoriteBeer): Boolean {
        return oldItem.equals(newItem)
    }
}

class FavoriteBeerListener(val clickListener: (beerId: Long) -> Unit){
    fun onClick(beer: DetailBeer) = clickListener(beer.id)
}