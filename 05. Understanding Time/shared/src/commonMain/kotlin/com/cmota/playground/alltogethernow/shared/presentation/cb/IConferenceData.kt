package com.cmota.playground.alltogethernow.shared.presentation.cb

import com.cmota.playground.alltogethernow.shared.data.entities.Conference

interface IConferenceData {

    fun onConferenceDataFetched(conferences: List<Conference>)

    fun onConferenceDataFailed(e: Exception)
}