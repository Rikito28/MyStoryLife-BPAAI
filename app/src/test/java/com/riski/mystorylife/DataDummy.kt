package com.riski.mystorylife

import com.riski.mystorylife.data.model.ListStory

object DataDummy {

    fun generateNewStory(): List<ListStory> {
        val listStory = ArrayList<ListStory>()
        for (i in 1..10) {
            val story = ListStory(
                "riski123",
                "Riski",
                "Test Testing",
                "https://story-api.dicoding.dev/images/stories/photos-1683504030052_WLCnFInY.jpg",
                "2023-05-08T00:00:30.054Z",
                -10.212,
                -16.002
            )
            listStory.add(story)
        }
        return listStory
    }
}