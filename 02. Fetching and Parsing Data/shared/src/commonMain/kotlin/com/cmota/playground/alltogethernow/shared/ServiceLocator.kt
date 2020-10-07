package com.cmota.playground.alltogethernow.shared

import com.cmota.playground.alltogethernow.shared.data.ConferencesAPI
import com.cmota.playground.alltogethernow.shared.domain.GetConferences
import com.cmota.playground.alltogethernow.shared.presentation.ConferenceListPresenter
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object ServiceLocator {

    private val conferencesAPI by lazy { ConferencesAPI() }

    private val getConferences: GetConferences
        get() = GetConferences(conferencesAPI)

    val getConferencePresenter: ConferenceListPresenter
        get() = ConferenceListPresenter(getConferences)
}