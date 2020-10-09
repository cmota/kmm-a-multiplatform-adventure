package com.cmota.playground.alltogethernow.androidApp.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.cmota.playground.alltogethernow.androidApp.R
import com.cmota.playground.alltogethernow.androidApp.adapters.ConferencesListAdapter
import com.cmota.playground.alltogethernow.androidApp.databinding.FragmentConferencesBinding
import com.cmota.playground.alltogethernow.shared.Gutenberg
import com.cmota.playground.alltogethernow.shared.ServiceLocator
import com.cmota.playground.alltogethernow.shared.data.entities.Conference
import com.cmota.playground.alltogethernow.shared.presentation.cb.IConferenceData

private const val TAG = "ConferencesFragment"

class ConferencesFragment : Fragment(), IConferenceData {

    private lateinit var binding: FragmentConferencesBinding

    private val presenterConferences by lazy { ServiceLocator.getConferencePresenter }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.toolbar.title = getString(R.string.navigation_conferences)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentConferencesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setup()
    }

    private fun setup() {
        val linearLayoutManager = LinearLayoutManager(context)

        binding.rvConferences.apply {
            setHasFixedSize(true)
            layoutManager = linearLayoutManager
            adapter = ConferencesListAdapter {
                openConferenceUrl(it.website)
            }
        }

        presenterConferences.attachView(this)
    }

    private fun openConferenceUrl(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    //region IConferenceData

    override fun onConferenceDataFetched(conferences: List<Conference>) {
        Gutenberg.d(TAG, "New conference data fetched: $conferences")

        val adapter = binding.rvConferences.adapter as ConferencesListAdapter
        adapter.submitList(conferences)
    }

    override fun onConferenceDataFailed(e: Exception) {
        Gutenberg.e(TAG, "Unable to retrieve conference data. Reason: $e")
    }

    //endregion IConferenceData
}