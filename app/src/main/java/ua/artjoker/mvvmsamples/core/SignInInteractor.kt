package ua.artjoker.mvvmsamples.core

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SignInInteractor @Inject constructor() {

    fun auth(email: String, password: String): Single<Profile> =
            Observable.interval(2, TimeUnit.SECONDS)
                    .firstOrError()
                    .flatMap {
                        when {
                            email != "serg@example.com" -> Single.error(UnknownEmailException)
                            password != "123123" -> Single.error(WrongPasswordException)
                            else -> Single.just(Profile("123", "Sergey", "Esenin"))
                        }
                    }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())

    object UnknownEmailException : Exception()
    object WrongPasswordException : Exception()
}
