package com.riski.mystorylife.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.riski.mystorylife.data.model.ListUserStory

class DetailViewModel: ViewModel() {
    private val _detailStory: MutableLiveData<ListUserStory> = MutableLiveData()
    val detailStory: LiveData<ListUserStory> get() = _detailStory

    fun setDetail(story: ListUserStory) {
        _detailStory.value = story
    }
}