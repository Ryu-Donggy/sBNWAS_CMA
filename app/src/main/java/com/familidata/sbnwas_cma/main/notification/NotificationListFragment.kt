package com.familidata.sbnwas_cma.main.notification

import android.content.Context
import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.PViewModel
import com.familidata.sbnwas_cma.base.model.vo.VoBioMonitorDate
import com.familidata.sbnwas_cma.base.model.vo.VoNotification
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.databinding.FragmentNotificationListBinding
import com.familidata.sbnwas_cma.databinding.ItemBioMonitorDateBinding
import com.familidata.sbnwas_cma.databinding.ItemNotificationDataBinding

class NotificationListFragment : PFragment() {

    private lateinit var binding: FragmentNotificationListBinding

    internal var list: List<PBean> = ArrayList()
    private lateinit var mListAdapter: ListAdapter
    private var desc = ""
    private fun setCallback(controller: IController, model: PViewModel?, desc: String) {
        this.controller = controller
        this.model = model
        this.desc = desc
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification_list, container, false)
        binding.controller = controller
        binding.title = desc
        binding.lifecycleOwner = this

        val mLinearLayoutManager = LinearLayoutManager(activity)
        mLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        binding.rv.layoutManager = mLinearLayoutManager
        binding.rv.setHasFixedSize(true)
        binding.rv.itemAnimator = DefaultItemAnimator()


        model?.let {
            it.getList().observe(viewLifecycleOwner) { tempList ->
                list = tempList
                if (list.isNotEmpty()) {
                    list.last().bottomLineVisible = false
                }
                mListAdapter = ListAdapter(requireContext())
                binding.rv.adapter = mListAdapter

            }
        }
        return binding.root
    }


    internal inner class ListAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private val selectedItems: SparseBooleanArray = SparseBooleanArray()

        override fun getItemViewType(position: Int): Int {
            return list[position].viewType
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                PBean.TYPE_DATE -> {
                    val binding = ItemBioMonitorDateBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                    AViewHolder(binding)
                }
                else -> {
                    val binding = ItemNotificationDataBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                    DataViewHolder(binding)
                }
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder) {
                is AViewHolder -> {
                    onAItemViewHolder(holder, position)
                }
                is DataViewHolder -> {
                    onDataItemViewHolder(holder, position)
                }
            }
        }

        private fun onAItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewHolder = holder as AViewHolder
            if (list.isEmpty()) return
            viewHolder.bind(list[position] as VoBioMonitorDate)
        }

        private fun onDataItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewHolder = holder as DataViewHolder
            if (list.isEmpty()) return
            viewHolder.bind(list[position] as VoNotification)
        }


        override fun getItemCount(): Int {
            return list.size
        }

        internal inner class AViewHolder internal constructor(binder: ItemBioMonitorDateBinding) : RecyclerView.ViewHolder(binder.root) {
            private val ibinding: ItemBioMonitorDateBinding = binder
            internal fun bind(vo: VoBioMonitorDate) {
                ibinding.controller = controller
                ibinding.bean = vo
            }
        }

        internal inner class DataViewHolder internal constructor(binder: ItemNotificationDataBinding) : RecyclerView.ViewHolder(binder.root) {
            private val ibinding: ItemNotificationDataBinding = binder
            internal fun bind(vo: VoNotification) {
                ibinding.controller = controller
                ibinding.bean = vo
            }
        }
    }

    companion object {

        fun with(controller: IController, model: PViewModel?, title: String): NotificationListFragment {
            val frag = NotificationListFragment()
            frag.setCallback(controller, model, title)
            return frag
        }
    }
}