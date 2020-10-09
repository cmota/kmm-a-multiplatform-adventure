package com.cmota.playground.alltogethernow.androidApp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cmota.playground.alltogethernow.androidApp.R
import com.cmota.playground.alltogethernow.androidApp.databinding.ItemConferenceBinding
import com.cmota.playground.alltogethernow.shared.data.entities.Conference

class ConferencesListAdapter(private val action: (Conference) -> Unit): ListAdapter<Conference, ConferencesListAdapter.UsersViewHolder>(DiffCallback()) {

    private lateinit var binding: ItemConferenceBinding

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): UsersViewHolder {
        binding = ItemConferenceBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return UsersViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: UsersViewHolder, position: Int) {
        val conference = getItem(position)
        viewHolder.bind(conference, action)
    }

    private class DiffCallback : DiffUtil.ItemCallback<Conference>() {

        override fun areItemsTheSame(oldItem: Conference, newItem: Conference) =
            oldItem.name == newItem.name

        override fun areContentsTheSame(oldItem: Conference, newItem: Conference) =
            oldItem == newItem
    }

    class UsersViewHolder(private val itemBinding: ItemConferenceBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(conference: Conference, action: (Conference) -> Unit) {
            val theme = itemBinding.tvStatus.context.theme
            val resources = itemBinding.tvStatus.resources

            itemBinding.llContainer.setOnClickListener { action(conference) }

            itemBinding.tvName.text = conference.name

            if (conference.city.isEmpty()) {
                itemBinding.tvLocation.text = conference.country
            } else {
                itemBinding.tvLocation.text = resources.getString(R.string.conference_country, conference.city, conference.country)
            }

            itemBinding.tvDate.text = conference.date

            val padding = itemBinding.tvStatus.paddingTop
            if (conference.isCanceled()) {
                itemBinding.tvStatus.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_conference_canceled, theme)
            } else {
                itemBinding.tvStatus.background = ResourcesCompat.getDrawable(resources, R.drawable.bg_conference_status, theme)
            }

            itemBinding.tvStatus.setPadding(padding, padding, padding, padding)
            itemBinding.tvStatus.text = conference.status

            Glide.with(itemBinding.ivLogo)
                .load(conference.logo)
                .apply(RequestOptions.circleCropTransform())
                .placeholder(R.drawable.ic_android_green)
                .into(itemBinding.ivLogo)
        }
    }
}