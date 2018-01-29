package ua.artjoker.mvvmsamples.viewmodels

import android.view.View
import ua.artjoker.mvvmsamples.R
import ua.artjoker.mvvmsamples.core.Profile
import ua.artjoker.mvvmsamples.core.ProfilesInteractor
import ua.artjoker.mvvmsamples.viewmodels.bases.ListViewModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfilesListViewModel @Inject constructor(
        private val interactor: ProfilesInteractor) : ListViewModel<Profile>() {

    override fun onResume() {
        super.onResume()
        refresh()
    }

    override fun makeDiffCallback(newList: List<Profile>): BaseDiffUtilCallback =
            DiffUtilCallbackImpl(newList)

    override fun onRefresh() {
        interactor.getProfiles().subscribe(SimpleListSingleObserver())
    }

    override fun createRecyclerViewAdapter(): BaseAdapter = AdapterImpl()

    inner class ProfileViewHolder(v: View) : ItemViewHolder<Profile>(v) {

        fun onClick() {
            appFacade.showToast(item.get().fullName)
        }
    }

    private inner class AdapterImpl : BaseAdapter() {

        override val itemLayoutId get() = R.layout.list_item_profile

        override fun onCreateItemViewHolder(v: View) = ProfileViewHolder(v)
    }

    private inner class DiffUtilCallbackImpl(newList: List<Profile>)
        : BaseDiffUtilCallback(newList) {

        override fun areItemsTheSame(item1: Profile, item2: Profile): Boolean =
                item1.id == item2.id

        override fun areContentsTheSame(oldItem: Profile, newItem: Profile): Boolean =
                oldItem.firstName == newItem.firstName &&
                        oldItem.lastName == newItem.lastName
    }
}
