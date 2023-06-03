package com.riski.mystorylife.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.riski.mystorylife.R
import com.riski.mystorylife.data.model.ListStory
import com.riski.mystorylife.data.model.ListUserStory
import com.riski.mystorylife.databinding.ActivityDetailStoryBinding
import com.riski.mystorylife.helper.formatDate
import java.util.TimeZone

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.apply {
            title = getString(R.string.detail_app)
            setDisplayHomeAsUpEnabled(true)
        }

        val detail = intent.getParcelableExtra<ListUserStory>(EXTRA_STORY) as ListStory

        binding.nameDetail.text = detail.name
        binding.deskDetail.text = detail.description
        binding.postedDetail.text = formatDate(detail.createdAt, TimeZone.getDefault().id)
        Glide.with(this)
            .load(detail.photoUrl)
            .into(binding.imgDetail)

    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        const val EXTRA_STORY = "story"
    }
}