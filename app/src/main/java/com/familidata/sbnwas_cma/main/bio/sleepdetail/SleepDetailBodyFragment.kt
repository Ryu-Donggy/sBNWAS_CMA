package com.familidata.sbnwas_cma.main.bio.sleepdetail

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
import com.familidata.sbnwas_cma.base.model.vo.*
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.databinding.FragmentCommonBioMonitorListBinding
import com.familidata.sbnwas_cma.databinding.FragmentSleepDetailBodyBinding
import com.familidata.sbnwas_cma.databinding.ItemBioMonitorSleepDataBinding
import com.familidata.sbnwas_cma.databinding.ItemBioMonitorDateBinding

class SleepDetailBodyFragment : PFragment() {

    private lateinit var binding: FragmentSleepDetailBodyBinding

    internal var list: List<PBean> = ArrayList()
    private lateinit var mListAdapter: ListAdapter
    private fun setCallback(controller: IController, model: PViewModel?) {
        this.controller = controller
        this.model = model
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sleep_detail_body, container, false)
        binding.controller = controller
        binding.lifecycleOwner = this

        val mLinearLayoutManager = LinearLayoutManager(activity)
        mLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        binding.rv.layoutManager = mLinearLayoutManager
        binding.rv.setHasFixedSize(true)
        binding.rv.itemAnimator = DefaultItemAnimator()


        model?.let {
            it.getList().observe(viewLifecycleOwner) { tempList ->
                list = tempList
                if (list.isNotEmpty())
                    list.last().bottomLineVisible = false
                mListAdapter = ListAdapter(requireContext())
                binding.rv.adapter = mListAdapter
                
            }
            it.getItem().observe(viewLifecycleOwner) { one ->
                one?.let { it2 ->
                    if (it2 is VoBioSleepHeader) {
                        binding.bean = it2
                    }
                }
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
                    val binding = ItemBioMonitorSleepDataBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
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
            viewHolder.bind(list[position] as VoBioMonitorSleepData)
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

        internal inner class DataViewHolder internal constructor(binder: ItemBioMonitorSleepDataBinding) : RecyclerView.ViewHolder(binder.root) {
            private val ibinding: ItemBioMonitorSleepDataBinding = binder
            internal fun bind(vo: VoBioMonitorSleepData) {
                ibinding.controller = controller
                ibinding.bean = vo
            }
        }
    }

    companion object {
        fun with(controller: IController, model: PViewModel?): SleepDetailBodyFragment {
            val frag = SleepDetailBodyFragment()
            frag.setCallback(controller, model)
            return frag
        }
    }
}