package ru.ilya.messenger.presentation.ui


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.ilya.messenger.R
import ru.ilya.messenger.databinding.ActivityMainBinding
import ru.ilya.messenger.domain.repository.MessengerRepository
import ru.ilya.messenger.presentation.ui.channels.ChannelsFragment
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.allStreams.AllStreamsFragment
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.subscribedStreams.SubscribedStreamsFragment
import ru.ilya.messenger.presentation.ui.people.PeopleFragment
import ru.ilya.messenger.presentation.ui.profile.ProfileFragment
import ru.ilya.messenger.util.BottomNavigationVisibilityUtils
import javax.inject.Inject

class MainActivity : AppCompatActivity(),
    SubscribedStreamsFragment.OnTopicClickListenerToOpenChatFromSubscribedStreams,
    AllStreamsFragment.OnTopicClickListenerToOpenChatFromAllStreams {

    @Inject
    lateinit var repository: MessengerRepository

    private lateinit var binding: ActivityMainBinding
    lateinit var bottomNavigationVisibilityUtils: BottomNavigationVisibilityUtils

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_fragments_container, ChannelsFragment())
                .commit()
        }
        setupBottomNavigationView()
    }


    private fun setupBottomNavigationView() {
        //  классы фрагментов, на которых должен отображаться BottomNavigationView
        bottomNavigationVisibilityUtils =
            BottomNavigationVisibilityUtils(binding.bottomNavigationView)
        bottomNavigationVisibilityUtils.addVisibleFragment(ChannelsFragment::class)
        bottomNavigationVisibilityUtils.addVisibleFragment(PeopleFragment::class)
        bottomNavigationVisibilityUtils.addVisibleFragment(ProfileFragment::class)

        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_channelsFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragments_container, ChannelsFragment())
                        .commit()
                    true
                }
                R.id.navigation_peopleFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragments_container, PeopleFragment())
                        .commit()
                    true
                }
                R.id.navigation_profileFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_fragments_container, ProfileFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
    }

    override fun onTopicClickedFromSubscribedStreams(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_fragments_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onTopicClickedFromAllStreams(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_fragments_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}