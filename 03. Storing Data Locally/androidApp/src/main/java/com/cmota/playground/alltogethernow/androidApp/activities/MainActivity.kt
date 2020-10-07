package com.cmota.playground.alltogethernow.androidApp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.cmota.playground.alltogethernow.androidApp.EXTRA_TAB_SELECTED
import com.cmota.playground.alltogethernow.androidApp.EXTRA_TAB_TITLE
import com.cmota.playground.alltogethernow.androidApp.R
import com.cmota.playground.alltogethernow.androidApp.databinding.ActivityMainBinding
import com.cmota.playground.alltogethernow.androidApp.fragments.ConferencesFragment
import com.cmota.playground.alltogethernow.androidApp.fragments.MessagesFragment
import com.cmota.playground.alltogethernow.androidApp.fragments.SettingsFragment


class MainActivity : AppCompatActivity() {

    private var selectedTab = 0
    private val fragments: ArrayList<Fragment> by lazy {
        setup()
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val selectedTabId = savedInstanceState?.getInt(EXTRA_TAB_SELECTED) ?: R.id.navigation_messages

        setupBottomBarActions(selectedTabId)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(EXTRA_TAB_SELECTED, binding.bnvNavigation.selectedItemId)
        super.onSaveInstanceState(outState)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setup(): ArrayList<Fragment> {

        val argMessages = Bundle()
        argMessages.putString(EXTRA_TAB_TITLE, getString(R.string.navigation_droidcon))

        val messages = MessagesFragment()
        messages.arguments = argMessages


        val argConferences= Bundle()
        argConferences.putString(EXTRA_TAB_TITLE, getString(R.string.navigation_conferences))

        val conferences = ConferencesFragment()
        conferences.arguments = argConferences


        val argSettings = Bundle()
        argSettings.putString(EXTRA_TAB_TITLE, getString(R.string.navigation_settings))

        val settings = SettingsFragment()
        settings.arguments = argSettings


        return arrayListOf(messages, conferences, settings)
    }

    private fun setupBottomBarActions(tab: Int) {
        binding.bnvNavigation.setOnNavigationItemSelectedListener { item ->
            val index: Int = when (item.itemId) {
                R.id.navigation_messages    -> 0
                R.id.navigation_conferences -> 1
                R.id.navigation_settings    -> 2
                else                        -> 0
            }

            switchFragment(index)
            selectedTab = index

            return@setOnNavigationItemSelectedListener true
        }

        binding.bnvNavigation.selectedItemId = tab
    }

    private fun switchFragment(index: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        val tag = "${fragments[index].arguments?.get(EXTRA_TAB_TITLE)}"

        // if the fragment has not yet been added to the container, add it first
        if (supportFragmentManager.findFragmentByTag(tag) == null) {
            transaction.add(R.id.fl_container, fragments[index], tag)

        } else {
            if (fragments[index] === supportFragmentManager.findFragmentByTag(tag)) {
                fragments[index].onResume()

            } else {
                transaction.replace(R.id.fl_container, fragments[index], tag)
            }
        }

        transaction.hide(fragments[selectedTab])
        transaction.show(fragments[index])
        transaction.commit()
    }
}
