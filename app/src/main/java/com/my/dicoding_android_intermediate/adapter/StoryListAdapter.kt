package com.my.dicoding_android_intermediate.adapter


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.my.dicoding_android_intermediate.data.remote.response.StoryResponseItem
import com.my.dicoding_android_intermediate.databinding.StoryItemBinding
import com.my.dicoding_android_intermediate.ui.detail.DetailActivity
import com.my.dicoding_android_intermediate.utils.Utils
import com.my.dicoding_android_intermediate.utils.setImageFromUrl
import com.my.dicoding_android_intermediate.utils.setLocalDateFormat
import java.util.ArrayList

class StoryListAdapter(private val listUserStory: ArrayList<StoryResponseItem>) : RecyclerView.Adapter<StoryListAdapter.ViewHolder>() {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    class ViewHolder(private val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, story: StoryResponseItem) {
            binding.apply {
                storyUsername.text = story.name
                tvStoryDescription.text = story.description
                storyImage.setImageFromUrl(context, story.photoUrl)
                storyDate.setLocalDateFormat(story.createdAt)

                // On item clicked
                root.setOnClickListener {
                    // Set ActivityOptionsCompat for SharedElement
                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            root.context as Activity,
                            Pair(storyImage, "story_image"),
                            Pair(storyUsername, "username"),
                            Pair(storyDate, "date"),
                            Pair(tvStoryDescription, "description")
                        )


                    Intent(context, DetailActivity::class.java).also { intent ->
                        intent.putExtra(Utils.DETAIL, story)
                        context.startActivity(intent, optionsCompat.toBundle())
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val stories = listUserStory[position]
        holder.bind(holder.itemView.context, stories)
    }

    override fun getItemCount(): Int = listUserStory.size

    interface OnItemClickCallback {
        fun onItemClicked(data: StoryResponseItem)
    }
}