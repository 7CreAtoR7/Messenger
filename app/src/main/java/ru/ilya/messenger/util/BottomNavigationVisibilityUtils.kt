package ru.ilya.messenger.util

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlin.reflect.KClass

class BottomNavigationVisibilityUtils(private val bottomNavigationView: BottomNavigationView) {
    // управляет видимостью BottomNavigationView (только на трёх фрагментах)
    // чтобы не прописывать в каждом фрагменте проверку и ставить ему visibility
    private val visibleFragments = mutableListOf<KClass<out Fragment>>()

    fun addVisibleFragment(fragmentClass: KClass<out Fragment>) {
        visibleFragments.add(fragmentClass)
    }

    fun setBottomNavigationViewVisibility(fragment: Fragment) {
        if (visibleFragments.contains(fragment::class)) {
            bottomNavigationView.visibility = View.VISIBLE
        } else {
            bottomNavigationView.visibility = View.GONE
        }
    }
}
