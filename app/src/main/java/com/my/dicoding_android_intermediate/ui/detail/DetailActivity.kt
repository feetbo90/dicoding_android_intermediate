package com.my.dicoding_android_intermediate.ui.detail

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.my.dicoding_android_intermediate.R
import com.my.dicoding_android_intermediate.data.entities.Story
import com.my.dicoding_android_intermediate.databinding.ActivityDetailBinding
import com.my.dicoding_android_intermediate.utils.Utils
import com.my.dicoding_android_intermediate.utils.setLocalFormat

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportPostponeEnterTransition()

        val story = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(Utils.DETAIL, Story::class.java)
        } else {
            intent.getParcelableExtra<Story>(Utils.DETAIL)
        }
        parseStoriesData(story)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    @SuppressLint("SetTextI18n")
    private fun parseStoriesData(stories: Story?) {
        if (stories != null) {
            binding.apply {
                tvStoryUsername.text = stories.name
                tvStoryDescription.text = stories.description
                toolbar.title = getString(R.string.detailToolbarTittle, stories.name)
                tvStoryDate.setLocalFormat(stories.createdAt)
                if (stories.lat != null) {
                    itemStoryLocation.text = "${stories.lat}, ${stories.lon}"
                } else itemStoryLocation.visibility = View.GONE

                Glide
                    .with(this@DetailActivity)
                    .load(stories.photoUrl)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            // Continue enter animation after image loaded
                            supportStartPostponedEnterTransition()
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            supportStartPostponedEnterTransition()
                            return false
                        }
                    })
                    .placeholder(R.drawable.image_loading)
                    .error(R.drawable.image_loading_error)
                    .into(ivStoryImage)
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}