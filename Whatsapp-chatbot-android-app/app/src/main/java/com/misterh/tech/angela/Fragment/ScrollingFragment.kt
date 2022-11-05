package com.misterh.tech.angela.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import com.misterh.tech.angela.R
import com.misterh.tech.angela.RecyclerViewAdapter
import com.misterh.tech.angela.Tag

class ScrollingFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tagList: ArrayList<Tag>
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_scrolling, container, false)
    }


    override fun onStart() {
        super.onStart()
        recyclerView = requireView().findViewById(R.id.recyclerV)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.setHasFixedSize(true)

        tagList = arrayListOf()

        recyclerViewAdapter = RecyclerViewAdapter(tagList)
        recyclerView.adapter = recyclerViewAdapter

        EventChangeListener()

    }

    private fun EventChangeListener() {

        db = FirebaseFirestore.getInstance()
        db.collection("public").addSnapshotListener(object: EventListener<QuerySnapshot>{
            override fun onEvent(p0: QuerySnapshot?, p1: FirebaseFirestoreException?) {
                if(p1!=null) {
                     Log.e("FireStore Error", p1.message.toString())
                    return
                }

                for(dc : DocumentChange in p0?.documentChanges!!) {
                    if(dc.type == DocumentChange.Type.ADDED) {
                        tagList.add(dc.document.toObject(Tag::class.java ))
                    }

                }
                recyclerViewAdapter.notifyDataSetChanged()
            }

        })

    }
}