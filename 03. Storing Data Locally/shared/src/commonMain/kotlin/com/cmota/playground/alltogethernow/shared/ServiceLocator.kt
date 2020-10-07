package com.cmota.playground.alltogethernow.shared

import com.cmota.playground.alltogethernow.shared.data.ConferencesAPI
import com.cmota.playground.alltogethernow.shared.domain.GetConferences
import com.cmota.playground.alltogethernow.shared.domain.dao.ConferenceDAO
import com.cmota.playground.alltogethernow.shared.presentation.ConferenceListPresenter
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object ServiceLocator {

    private val conferencesAPI by lazy { ConferencesAPI() }

    private val conferenceDao by lazy { ConferenceDAO(PlatformDatabase().createDatabase()) }

    private val getConferences: GetConferences
        get() = GetConferences(conferencesAPI, conferenceDao)

    val getConferencePresenter: ConferenceListPresenter
        get() = ConferenceListPresenter(getConferences)
}