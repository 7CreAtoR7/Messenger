package ru.ilya.messenger.presentation.ui.channels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import ru.ilya.messenger.databinding.FragmentChannelsBinding
import ru.ilya.messenger.presentation.ui.MainActivity
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.allStreams.AllStreamsFragment
import ru.ilya.messenger.presentation.ui.channels.streamsFragments.subscribedStreams.SubscribedStreamsFragment

class ChannelsFragment : Fragment() {

    private var _binding: FragmentChannelsBinding? = null
    private val binding: FragmentChannelsBinding
        get() = _binding ?: throw RuntimeException("FragmentChannelsBinding == null")

    private lateinit var pagerAdapter: PagerAdapter
    private lateinit var tabs: List<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.hide()
        _binding = FragmentChannelsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as MainActivity)
            .bottomNavigationVisibilityUtils.setBottomNavigationViewVisibility(this)
        tabs = listOf(TAB1, TAB2)
        pagerAdapter = PagerAdapter(childFragmentManager, lifecycle)
        binding.viewPager.adapter = pagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabs[position]
        }.attach()

        initEditTextChangeListener()
    }

    private fun initEditTextChangeListener() {
        binding.searchEditText.addTextChangedListener {
            val currentFragment = getCurrentFragment()
            if (currentFragment is SubscribedStreamsFragment) {
                currentFragment.updateStreamsList(it.toString())
            } else if (currentFragment is AllStreamsFragment) {
                currentFragment.updateStreamsList(it.toString())
            }
        }
    }

    private fun getCurrentFragment(): Fragment {
        // какой fragment из двух сейчас отображается во viewpager: subscribed/all
        return childFragmentManager.fragments[binding.viewPager.currentItem]
    }

    companion object {

        const val TAB1 = "Subscribed"
        const val TAB2 = "All streams"
        const val KEY_TOPIC_NAME = "TOPIC_DATA"
        const val KEY_STREAM_NAME = "STREAM_TITLE"
        const val KEY_STREAM_ID = "STREAM_ID"

        const val KEY_STREAM_TITLE = "StreamTitle"
        const val EMPTY_STREAM_TITLE = ""
        const val DEFAULT_STREAM_TITLE = "general"
        const val DEFAULT_STREAM_ID = 0
        const val SHARED_PREFERENCES = "MySharedPreferences"

        const val CHAT_SHARED_PREFERENCES = "ChatSharedPreferences"
        const val KEY_LAST_MESSAGE = "LastMessageId"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
