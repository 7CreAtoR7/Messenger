package ru.ilya.messenger.presentation.ui.channels

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.allStreams.AllStreamsFragment
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.subscribedStreams.SubscribedStreamsFragment

class PagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            SubscribedStreamsFragment()
        } else {
            AllStreamsFragment()
        }
    }

}
