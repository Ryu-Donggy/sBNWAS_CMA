package com.familidata.sbnwas_cma.main

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
import com.familidata.sbnwas_cma.base.model.vo.VoDevice
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.databinding.FragmentMainDeviceListBinding
import com.familidata.sbnwas_cma.databinding.ItemDeviceBinding

class MainDeviceListFragment : PFragment() {

    private lateinit var binding: FragmentMainDeviceListBinding

    internal var list: List<PBean> = ArrayList()
    private lateinit var mListAdapter: ListAdapter
    private fun setCallback(controller: IController, model: PViewModel?) {
        this.controller = controller
        this.model = model
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_device_list, container, false)
        binding.controller = controller
        binding.lifecycleOwner = this

        model?.let {
            it.getList().observe(viewLifecycleOwner) { tempList ->
                list = tempList
                mListAdapter = ListAdapter(requireContext())
                binding.rv.adapter = mListAdapter
            }
        }
        val mLinearLayoutManager = LinearLayoutManager(activity)
        mLinearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        binding.rv.layoutManager = mLinearLayoutManager
        binding.rv.setHasFixedSize(true)
        binding.rv.itemAnimator = DefaultItemAnimator()
        return binding.root
    }


    internal inner class ListAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private val selectedItems: SparseBooleanArray = SparseBooleanArray()

        override fun getItemViewType(position: Int): Int {
            return list[position].viewType
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                PBean.TYPE_A -> {
                    val binding = ItemDeviceBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                    AViewHolder(binding)
                }
                else -> {
                    val binding = ItemDeviceBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                    AViewHolder(binding)
                }
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder) {
                is AViewHolder -> {
                    onAItemViewHolder(holder, position)
                }
            }
        }

        private fun onAItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewHolder = holder as AViewHolder
            if (list.isEmpty()) return
            viewHolder.bind(list[position] as VoDevice)
        }


        override fun getItemCount(): Int {
            return list.size
        }

        internal inner class AViewHolder internal constructor(binder: ItemDeviceBinding) : RecyclerView.ViewHolder(binder.root) {
            private val ibinding: ItemDeviceBinding = binder
            internal fun bind(vo: VoDevice) {
                ibinding.controller = controller
                ibinding.bean = vo
            }
        }
    }

    companion object {
        fun with(controller: IController, model: PViewModel?): MainDeviceListFragment {
            val frag = MainDeviceListFragment()
            frag.setCallback(controller, model)
            return frag
        }
    }
}