package com.uzlov.developerslife.ui.main

import android.graphics.Typeface.BOLD
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.uzlov.developerslife.R
import com.uzlov.developerslife.repository.model.Post

class PostAdapter : RecyclerView.Adapter<PostHolder>(){
    private val listPosts = mutableListOf<Post>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder =
        PostHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false))

    override fun onBindViewHolder(holder: PostHolder, position: Int) = holder.onBind(listPosts[position])

    override fun getItemCount(): Int = listPosts.size

    fun setData(objects: Collection<Post>) {
        listPosts.clear()
        listPosts.addAll(objects)
        notifyDataSetChanged()
    }

    fun addItem(post: Post){
        listPosts.add(post)
        notifyItemInserted(listPosts.size-1)
    }
}

class PostHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    private var mProgressBar: ProgressBar = view.findViewById(R.id.progressBar2)
    private var profileNameTV:TextView = view.findViewById(R.id.tvProfineName)
    private var datePostTV:TextView = view.findViewById(R.id.tvDatePost)
    private var gifContainerIV:ImageView = view.findViewById(R.id.gifContainerIV)
    private var likesCountTV:TextView = view.findViewById(R.id.tvLikes)
    private var descriptionTV:TextView = view.findViewById(R.id.tvDescription)
    private var s :SpannableString ? = null

    fun onBind(post : Post){
        mProgressBar.visibility = View.VISIBLE
        profileNameTV.text = post.author
        datePostTV.text = post.date

        Glide.with(view.context)
            .asGif()
            .load(post.gifURL)
            .error(R.drawable.ic_error_link)
            .skipMemoryCache(true) // снижаем нагрузку для слабых телефонов
            .diskCacheStrategy(DiskCacheStrategy.NONE) // снижаем нагрузку для слабых телефонов
            .addListener(object : RequestListener<GifDrawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.e("123", e?.message.toString())
                    mProgressBar.visibility = View.INVISIBLE
                    return false
                }

                override fun onResourceReady(
                    resource: GifDrawable?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    mProgressBar.visibility = View.INVISIBLE
                    return false

                }
            })
            .centerCrop()
            .into(gifContainerIV)
            .clearOnDetach()

        likesCountTV.text = "Нравится: ${post.votes}"

        s = SpannableString("${post.author} ${post.description}")
        s?.setSpan(StyleSpan(BOLD), 0, post.author.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        descriptionTV.text = s
    }

}

