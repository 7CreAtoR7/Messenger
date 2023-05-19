package ru.ilya.messenger.presentation.ui.people

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import ru.ilya.messenger.databinding.FragmentPeopleBinding
import ru.ilya.messenger.di.getAppComponent
import ru.ilya.messenger.presentation.ui.people.elm.StoreFactory
import ru.ilya.messenger.presentation.ui.people.elm.models.Effect
import ru.ilya.messenger.presentation.ui.people.elm.models.Event
import ru.ilya.messenger.presentation.ui.people.elm.models.State
import ru.ilya.messenger.presentation.ui.people.peopleAdapter.PeopleAdapter
import vivid.money.elmslie.android.base.ElmFragment
import vivid.money.elmslie.core.store.Store
import javax.inject.Inject

class PeopleFragment : ElmFragment<Event, Effect, State>() {

    @Inject
    lateinit var peopleStoreFactory: StoreFactory

    private var _binding: FragmentPeopleBinding? = null
    private val binding: FragmentPeopleBinding
        get() = _binding ?: throw RuntimeException("FragmentPeopleBinding == null")

    private val peopleAdapter: PeopleAdapter by lazy { PeopleAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        requireActivity().getAppComponent().inject(this)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.hide()
        _binding = FragmentPeopleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        with(binding.recyclerViewPeople) {
            layoutManager = LinearLayoutManager(activity)
            adapter = peopleAdapter
        }
    }

    override val initEvent: Event = Event.Ui.LoadUsers

    override fun createStore(): Store<Event, Effect, State> {
        return peopleStoreFactory.provide()
    }

    override fun render(state: State) {
        with(binding) {
            if (state.isLoading) {
                shimmerLayout.startShimmer()
                shimmerLayout.visibility = View.VISIBLE
            }
            if (!state.isLoading) {
                shimmerLayout.stopShimmer()
                shimmerLayout.visibility = View.GONE
            }
            if (state.users.isNotEmpty()) {
                peopleAdapter.submitList(state.users)
            }

            state.error?.let {
                showSnackBarError("Ошибка: ${state.error.localizedMessage}")
            }
        }
    }

    override fun handleEffect(effect: Effect) {
        when (effect) {
            is Effect.UsersLoadError -> {
                Snackbar.make(
                    requireView(),
                    effect.error.message!!,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            is Effect.UsersStatusLoadError -> {
                showSnackBarError("Ошибка при загрузке статуса пользователя: ${effect.error.message!!}")
            }
        }
    }

    private fun showSnackBarError(message: String) {
        // прячем клавиатуру
        val inputMethodManager =
            context?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)

        val snack = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
        val view: View = snack.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.CENTER_VERTICAL
        view.layoutParams = params
        snack.show()
    }

}