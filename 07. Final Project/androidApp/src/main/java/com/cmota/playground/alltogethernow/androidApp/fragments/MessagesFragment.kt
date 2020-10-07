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
import com.cmota.playground.alltogethernow.shared.ServiceLocator
import com.cmota.playground.alltogethernow.shared.data.entities.Message
import com.cmota.playground.alltogethernow.shared.getFirebaseInstance
import com.cmota.playground.alltogethernow.shared.presentation.cb.IMessageData

private const val TAG = "MessagesFragment"

class MessagesFragment : Fragment(), IMessageData {

    private lateinit var binding: FragmentMessagesBinding

    private val presenterMessages by lazy { ServiceLocator.getMessagesPresenter }

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
        presenterMessages.subscribe(this)
    }

    private fun sendMessage(content: String) {
        presenterMessages.sendMessage(content)
    }

    //region IMessageData

    override fun onMessagesUpdate(messages: List<Message>) {
        val adapter = binding.rvMessages.adapter as MessagesListAdapter
        adapter.submitList(messages)
        adapter.registerAdapterDataObserver(object : AdapterDataObserver() {

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                binding.rvMessages.scrollToPosition(positionStart)
            }
        })
    }

    override fun onMessageSent(status: Boolean) {
        if (status) {
            Gutenberg.d(TAG, "DocumentSnapshot successfully written!")

        } else {
            Gutenberg.w(TAG, "Error writing document")
        }
    }

    //endregion
}