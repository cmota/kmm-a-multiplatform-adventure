package com.cmota.playground.alltogethernow.shared.domain.dao

import com.cmota.playground.alltogethernow.shared.data.entities.Conference
import data.ConferenceDb

class ConferenceDAO(database: ConferenceDb) {

    private val db = database.conferenceModelQueries

    internal fun insertOrReplace(conference: Conference) {
        db.insertOrReplaceConference(
            id = "${conference.name}-${conference.country}-${conference.date}",
            name = conference.name,
            city = conference.city,
            country = conference.country,
            date = conference.date,
            logo = conference.logo,
            website = conference.website,
            status = conference.status)
    }

    internal fun getAllConferences(): List<Conference> {
        val data = db.selectAllConferences().executeAsList()

        val conferences = mutableListOf<Conference>()
        for (item in data) {
            conferences += Conference(
                item.name,
                item.city,
                item.country,
                item.date,
                item.logo,
                item.website,
                item.status)
        }

        return conferences
    }
}