package com.riski.mystorylife.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.riski.mystorylife.data.model.ListStory
import com.riski.mystorylife.databinding.ItemRowStoryBinding
import com.riski.mystorylife.helper.formatDate
import com.riski.mystorylife.ui.detail.DetailStoryActivity
import java.util.TimeZone

class StoryAdapter: PagingDataAdapter<ListStory, StoryAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    class ViewHolder(private var binding: ItemRowStoryBinding): RecyclerView.ViewHolder(binding.root) {
    fun bind(app: ListStory) {
        with(binding) {
            tvItemName.text = app.name
            timeRow.text = formatDate(app.createdAt, TimeZone.getDefault().id)
            Glide.with(itemView)
                .load(app.photoUrl)
                .centerCrop()
                .into(ivItemPhoto)

            ivItemPhoto.setOnClickListener {
                val intent = Intent(it.context, DetailStoryActivity::class.java)
                intent.putExtra(DetailStoryActivity.EXTRA_STORY, app)
                val compat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    it.context as Activity,
                        Pair(ivItemPhoto, "story"),
                        Pair(tvItemName, "name"),
                        Pair(timeRow, "posted_time")
                    )
                it.context.startActivity(intent, compat.toBundle())
            }
        }
    }}

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStory>() {
            override fun areItemsTheSame(oldItem: ListStory, newItem: ListStory): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStory, newItem: ListStory): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}