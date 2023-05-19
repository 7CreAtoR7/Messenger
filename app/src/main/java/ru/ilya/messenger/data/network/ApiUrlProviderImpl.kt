package ru.ilya.messenger.data.network

class ApiUrlProviderImpl : ApiUrlProvider {

    override fun getApiUrlMethod(): String {
        return "https://tinkoff-android-spring-2023.zulipchat.com/api/v1/"
    }
}