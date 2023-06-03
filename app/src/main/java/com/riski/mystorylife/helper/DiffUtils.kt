package com.riski.mystorylife.helper

import androidx.recyclerview.widget.DiffUtil
import com.riski.mystorylife.data.model.ListUserStory

class DiffUtils(private val oldList: List<ListUserStory>, private val NewList: List<ListUserStory>): DiffUtil.Callback() {
    override fun getOldListSize() = oldList.size
    override fun getNewListSize() = NewList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = oldList[oldItemPosition].id == NewList[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItemStory = oldList[oldItemPosition]
        val newItemStory = NewList[newItemPosition]
        return oldItemStory.id == newItemStory.id
    }
}