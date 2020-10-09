package com.cmota.playground.alltogethernow.androidApp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
import com.cmota.playground.alltogethernow.androidApp.R
import com.cmota.playground.alltogethernow.androidApp.adapters.MessagesListAdapter
import com.cmota.playground.alltogethernow.androidApp.databinding.FragmentMessagesBinding
import com.cmota.playground.alltogethernow.shared.Gutenberg
import com.cmota.playground.alltogethernow.shared.PlatformSettings.settingsRepository
import com.cmota.playground.alltogethernow.shared.ServiceLocator
import com.cmota.playground.alltogethernow.shared.data.entities.Message
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private const val TAG = "MessagesFragment"

class MessagesFragment : Fragment() {

    private lateinit var binding: FragmentMessagesBinding

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.toolbar.title = getString(R.string.navigation_droidcon)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMessagesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

        setup()
        loadMessages()
    }

    private fun setup() {
        val linearLayoutManager = LinearLayoutManager(context)
        linearLayoutManager.stackFromEnd = true

        binding.rvMessages.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = MessagesListAdapter()
        }

        binding.btnSend.setOnClickListener {
            val message = binding.etContent.text.toString()
            sendMessage(message)

            binding.etContent.text.clear()
        }
    }

    private fun loadMessages() {
        val docs = Firebase.firestore.collection("dcEMEA").orderBy("timestamp")
        docs.addSnapshotListener { snapshot, e ->
            if (e != null || snapshot == null) {
                Gutenberg.w(TAG, "Unable to retrieve data. Error=$e, snapshot=$snapshot")
                return@addSnapshotListener
            }

            Gutenberg.d(TAG, "New data retrieved:${snapshot.documents.size}")

            val messages = mutableListOf<Message>()
            for (document in snapshot.documents) {
                val message = Message(
                    document.id,
                    "${document.data?.get("username")}",
                    "${document.data?.get("content")}",
                    "${document.data?.get("timestamp")}",
                    document.data?.get("username") == settingsRepository.getUsername()
                )

                messages += message
            }

            for (message in messages) {
                Gutenberg.d(TAG, message.toString())
            }

            val adapter = binding.rvMessages.adapter as MessagesListAdapter

            adapter.submitList(messages)
            adapter.registerAdapterDataObserver(object : AdapterDataObserver() {

                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                    super.onItemRangeInserted(positionStart, itemCount)
                    binding.rvMessages.scrollToPosition(positionStart)
                }
            })

        }
    }

    private fun sendMessage(content: String) {
        ServiceLocator.getMessagesPresenter.sendMessage(content)
    }
}