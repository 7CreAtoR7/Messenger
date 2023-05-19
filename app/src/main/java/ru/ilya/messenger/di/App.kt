package ru.ilya.messenger.di

import android.app.Application
import android.content.Context


class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        instance = this
        appComponent = DaggerAppComponent.factory().create(this)
    }

    companion object {
        lateinit var instance: App
    }
}

fun Context.getAppComponent(): AppComponent {
    return (this.applicationContext as App).appComponent
}