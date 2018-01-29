package ua.artjoker.mvvmsamples.viewmodels.bases

import android.databinding.*
import android.support.annotation.CallSuper
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.Disposables
import io.reactivex.observers.DisposableSingleObserver
import ua.artjoker.mvvmsamples.BR
import ua.artjoker.mvvmsamples.R
import ua.artjoker.mvvmsamples.ui.bindViewModel

abstract class ListViewModel<I> : FragmentViewModel() {

    val refreshing = ObservableBoolean()
    val emptyMessageVisible = ObservableBoolean()

    val recyclerViewAdapter: RecyclerView.Adapter<*> by lazy { createRecyclerViewAdapter() }

    val size @Bindable get() = list.size

    open val emptyListText = R.string.empty_list

    private var list = emptyList<I>()

    private var refreshingDisposable = Disposables.disposed()

    init {
        refreshing.addOnPropertyChangedCallback(RefreshingChangedCallback())
    }

    protected abstract fun makeDiffCallback(newList: List<I>): BaseDiffUtilCallback

    protected abstract fun onRefresh()

    protected abstract fun createRecyclerViewAdapter(): BaseAdapter

    override fun onBackPressed(): Boolean {
        release()
        return false
    }

    fun refresh() {
        refreshing.set(true)
    }

    fun isListEmpty() = list.isEmpty()

    protected open fun checkRefreshAvailable() = true

    protected fun setList(list: List<I>) {
        notifyPropertyChanged(BR.size)

        if (list.isEmpty()) {
            this.list = emptyList()
            recyclerViewAdapter.notifyDataSetChanged()
        } else {
            val diffResult = DiffUtil.calculateDiff(makeDiffCallback(list))
            this.list = list
            diffResult.dispatchUpdatesTo(recyclerViewAdapter)
        }
    }

    protected fun clear() {
        setList(emptyList())
    }

    protected fun syncEmptyMessage() {
        emptyMessageVisible.set(list.isEmpty())
    }

    private fun release() {
        refreshing.set(false)
    }

    private inner class RefreshingChangedCallback : Observable.OnPropertyChangedCallback() {

        override fun onPropertyChanged(o: Observable, propertyId: Int) {
            if (refreshing.get()) {
                onRefresh()
            }
        }
    }

    open class BaseViewHolder<I>(v: View) : RecyclerView.ViewHolder(v)

    abstract class ItemViewHolder<I>(v: View) : BaseViewHolder<I>(v) {
        val item = ObservableField<I>()
    }

    private enum class ViewType {
        ITEM, FOOTER
    }

    protected abstract inner class BaseAdapter : RecyclerView.Adapter<BaseViewHolder<I>>() {

        protected abstract val itemLayoutId: Int

        abstract fun onCreateItemViewHolder(v: View): BaseViewHolder<I>

        override fun getItemCount() = 0.takeIf { list.isEmpty() } ?: list.size+1

        override fun getItemViewType(position: Int) =
                when (position) {
                    list.size -> ViewType.FOOTER
                    else -> ViewType.ITEM
                }.ordinal

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<I> =
                when (viewType) {
                    ViewType.ITEM.ordinal -> {
                        val binding = DataBindingUtil.inflate<ViewDataBinding>(
                                LayoutInflater.from(parent.context),
                                itemLayoutId,
                                parent,
                                false)

                        onCreateItemViewHolder(binding.root).also { binding.bindViewModel(it) }
                    }

                    else -> LayoutInflater.from(parent.context)
                            .inflate(android.R.layout.simple_list_item_2, parent, false)
                            .let(::BaseViewHolder)
                }

        @CallSuper
        override fun onBindViewHolder(holder: BaseViewHolder<I>, position: Int) {
            if (holder.itemViewType == ViewType.ITEM.ordinal) {
                (holder as ItemViewHolder).item.set(list[position])
            }
        }
    }

    protected abstract inner class BaseDiffUtilCallback(private val newList: List<I>)
        : DiffUtil.Callback() {

        final override fun getNewListSize() = newList.size

        final override fun getOldListSize() = list.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                areItemsTheSame(list[oldItemPosition], newList[newItemPosition])

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
                areContentsTheSame(list[oldItemPosition], newList[newItemPosition])

        abstract fun areItemsTheSame(item1: I, item2: I): Boolean

        abstract fun areContentsTheSame(oldItem: I, newItem: I): Boolean
    }

    protected open inner class SimpleListSingleObserver : DisposableSingleObserver<List<I>>() {


        @CallSuper
        override fun onStart() {
            refreshingDisposable = this
            refreshing.set(true)
        }

        @CallSuper
        override fun onError(e: Throwable) {
            refreshing.set(false)
            syncEmptyMessage()
        }

        @CallSuper
        override fun onSuccess(t: List<I>) {
            setList(t)
            refreshing.set(false)
            syncEmptyMessage()
        }
    }
}
