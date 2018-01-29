package ua.artjoker.mvvmsamples.core

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfilesInteractor @Inject constructor() {

    private var flag = false

    fun getProfiles(): Single<List<Profile>> {
        flag = !flag
        return Observable.interval(1, TimeUnit.SECONDS)
                .take(1)
                .flatMap {
                    Observable.just(

                            Profile("1",
                                    "John".takeIf { flag } ?: "Joahn",
                                    "Morrison",
                                    "https://images.pexels.com/photos/91227/pexels-photo-91227.jpeg?h=350&auto=compress&cs=tinysrgb"),
                            Profile("2", "John", "Aiken"),
                            Profile("3", "Nataly", "Borisova", "https://images.pexels.com/photos/415829/pexels-photo-415829.jpeg?w=1260&h=750&auto=compress&cs=tinysrgb"),
                            Profile("4",
                                    "Quan".takeIf { flag } ?: "Ang",
                                    "Shiu",
                                    "https://images.pexels.com/photos/428341/pexels-photo-428341.jpeg?w=1260&h=750&auto=compress&cs=tinysrgb")
                    )
                }
                .filter { flag || it.id != "3" }
                .toSortedList(compareBy(Profile::firstName, Profile::lastName))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}
