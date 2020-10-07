package com.cmota.playground.alltogethernow.shared.presentation

import com.cmota.playground.alltogethernow.shared.domain.GetConferences
import com.cmota.playground.alltogethernow.shared.domain.defaultDispatcher
import com.cmota.playground.alltogethernow.shared.presentation.cb.IConferenceData
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ConferenceListPresenter(private val conferences: GetConferences,
                              private val coroutineContext: CoroutineContext = defaultDispatcher
) {

    private var view: IConferenceData? = null
    private lateinit var scope: PresenterCoroutineScope

    fun attachView(currView: IConferenceData) {
        view = currView
        scope = PresenterCoroutineScope(coroutineContext)
        fetchConferenceList()
    }

    fun detachView() {
        if (view == null) {
            return
        }

        view = null
        scope.viewDetached()
    }

    private fun fetchConferenceList() {
        scope.launch {
            //4
            conferences(
                onSuccess = { view?.onConferenceDataFetched(it) },
                onFailure = { view?.onConferenceDataFailed(it) }
            )
        }
    }
}