package com.cmota.playground.alltogethernow.shared

import com.cmota.playground.alltogethernow.shared.data.ConferencesAPI
import com.cmota.playground.alltogethernow.shared.domain.GetConferences
import com.cmota.playground.alltogethernow.shared.domain.GetMessages
import com.cmota.playground.alltogethernow.shared.domain.SendMessages
import com.cmota.playground.alltogethernow.shared.domain.dao.ConferenceDAO
import com.cmota.playground.alltogethernow.shared.presentation.ConferenceListPresenter
import com.cmota.playground.alltogethernow.shared.presentation.MessageListPresenter
import kotlin.native.concurrent.ThreadLocal

@ThreadLocal
object ServiceLocator {

    private val conferencesAPI by lazy { ConferencesAPI() }

    private val conferenceDao by lazy { ConferenceDAO(PlatformDatabase().createDatabase()) }

    private val getConferences: GetConferences
        get() = GetConferences(conferencesAPI, conferenceDao)

    private val getMessages: GetMessages
        get() = GetMessages()

    private val sendMessages: SendMessages
        get() = SendMessages()

    val getConferencePresenter: ConferenceListPresenter
        get() = ConferenceListPresenter(getConferences)

    val getMessagesPresenter: MessageListPresenter
        get() = MessageListPresenter(getMessages, sendMessages)
}