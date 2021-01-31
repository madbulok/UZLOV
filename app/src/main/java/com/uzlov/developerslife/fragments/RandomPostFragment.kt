package com.uzlov.developerslife.fragments

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.uzlov.developerslife.R
import com.uzlov.developerslife.repository.LoadableObjects
import com.uzlov.developerslife.repository.PostRepository
import com.uzlov.developerslife.repository.ReConnectable
import com.uzlov.developerslife.repository.model.Post


class RandomPostFragment : Fragment() , LoadableObjects<Post>, ReConnectable {

    private lateinit var mNextBtn: ImageButton
    private lateinit var mPrevBtn: ImageButton
    private lateinit var mProgressBar: ProgressBar
    private lateinit var mGifImageView: ImageView
    private lateinit var mDescriptionTV: TextView
    private lateinit var mAutorName: TextView
    private val cachedHistory = ArrayList<Post>()
    private var cursorPos = -1

    private var repository: PostRepository<Post> ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        repository = PostRepository(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_random_post, container, false)
        mGifImageView =  root.findViewById(R.id.imageView)
        mNextBtn = root.findViewById(R.id.btnNext)
        mPrevBtn = root.findViewById(R.id.btnPrev)
        mProgressBar = root.findViewById(R.id.progressBar)
        mDescriptionTV = root.findViewById(R.id.description)
        mAutorName = root.findViewById(R.id.autor_name)


        repository?.getNewRandomPost()

        mNextBtn.setOnClickListener {
            animateNextButton()
            if (cursorPos == cachedHistory.size-1){
                repository?.getNewRandomPost()
            } else if (cursorPos < cachedHistory.size){
                setupNextCachedPost()
            }
            showPreviousBtn()
        }

        mPrevBtn.setOnClickListener {
            animatePreviousButton()
            setupPrevCachedPost()
        }
        return root
    }

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int): RandomPostFragment {
            return RandomPostFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    private fun animateNextButton(){
        ObjectAnimator.ofFloat(mNextBtn, View.X,  mNextBtn.x, mNextBtn.x+20, mNextBtn.x).start()
    }

    private fun animatePreviousButton(){
        ObjectAnimator.ofFloat(mPrevBtn, View.ROTATION, 0f, -45f, 0f).start()
    }

    private fun setupPrevCachedPost(){
        if (cursorPos > 0) {
            mProgressBar.visibility = View.VISIBLE
            cursorPos--
            loadContent()
        }
        if(cursorPos == 0) {
            hidePreviousBtn()
        }
    }

    private fun setupNextCachedPost(){
        mProgressBar.visibility = View.VISIBLE

        cursorPos++
        loadContent()
    }

    private fun loadContent() {
        Glide.with(this@RandomPostFragment)
            .asGif()
            .load(cachedHistory[cursorPos].gifURL)
            .error(R.drawable.ic_error_link)
            .addListener(object : RequestListener<GifDrawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<GifDrawable>?,
                    isFirstResource: Boolean
                ): Boolean {
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
            .skipMemoryCache(true) // снижаем нагрузку для слабых телефонов
            .diskCacheStrategy(DiskCacheStrategy.NONE) // снижаем нагрузку для слабых телефонов
            .into(mGifImageView)
            .clearOnDetach()

        mDescriptionTV.text = cachedHistory[cursorPos].description
        mAutorName.text = cachedHistory[cursorPos].author
    }

    private fun hidePreviousBtn(){
        mPrevBtn.isEnabled = false
        mPrevBtn.visibility = View.INVISIBLE
    }

    private fun showPreviousBtn(){
        mPrevBtn.isEnabled = true
        mPrevBtn.visibility = View.VISIBLE
    }

    override fun loadStart() {
        mProgressBar.visibility = View.VISIBLE
    }

    override fun loadEnd() {
        mProgressBar.visibility = View.VISIBLE
        if (cursorPos > 0){
            showPreviousBtn()
        }
    }

    override fun loadError(message: String) {
        //show error
    }

    override fun loadObject(objects: Post) {
        cachedHistory.add(objects)
        cursorPos++
        loadContent()
    }

    override fun loadObjects(objects: Collection<Post>) {}

    override fun reconnect() {
        // при первом запуске независимо от состояния сети отработает
        if (repository != null) repository?.getNewRandomPost()
        println("reconnect random fragment")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.apply {
            putInt("POS_CURSOR", cursorPos)
            putParcelableArrayList("CACHE_LIST", cachedHistory)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        println("onViewStateRestored")
        super.onViewStateRestored(savedInstanceState)

        if (savedInstanceState == null) return
        cachedHistory.clear()
        savedInstanceState.let {
            cursorPos = it.getInt("POS_CURSOR")
            cachedHistory.addAll(it.getParcelableArrayList<Post>("CACHE_LIST")?.toList() ?: emptyList())
        }
    }
}