package com.uzlov.developerslife.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.uzlov.developerslife.R
import com.uzlov.developerslife.repository.LoadableObjects
import com.uzlov.developerslife.repository.PostRepository
import com.uzlov.developerslife.repository.ReConnectable
import com.uzlov.developerslife.repository.model.Post
import com.uzlov.developerslife.ui.main.PostAdapter

public class LatestPostFragment  : Fragment(), LoadableObjects<Post>, ReConnectable{
    private lateinit var adapterPost: PostAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var pgBar: ProgressBar
    private var repository: PostRepository<Post> ?= null

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"

        @JvmStatic
        fun newInstance(sectionNumber: Int): LatestPostFragment {
            return LatestPostFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        repository = PostRepository(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.content_adapter_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.rvList)
        pgBar = view.findViewById(R.id.rvProgressLoader)

        adapterPost = PostAdapter()

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adapterPost
        }
        repository?.getLastPost()
    }

    override fun loadStart() {
        pgBar.visibility = View.VISIBLE
    }

    override fun loadEnd() {
        pgBar.visibility = View.INVISIBLE
    }

    override fun loadError(message: String) {
//        Toast.makeText(context, getString(R.string.error_response_api), Toast.LENGTH_SHORT).show()
    }

    override fun loadObjects(objects: Collection<Post>) {
        if (objects.isEmpty()){
            Toast.makeText(context, getString(R.string.empty_result_from_api), Toast.LENGTH_SHORT).show()
        } else {
            adapterPost.setData(objects)
        }
    }

    override fun loadObject(objects: Post) { }

    override fun reconnect() {
        // при первом запуске независимо от состояния сети отработает
        if (repository != null) repository?.getLastPost()
        println("reconnect latest fragment")
    }
}