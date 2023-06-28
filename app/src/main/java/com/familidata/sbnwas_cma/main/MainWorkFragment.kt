package com.familidata.sbnwas_cma.main

import android.content.Context
import android.os.Bundle
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
import com.familidata.sbnwas_cma.base.model.vo.VoBio
import com.familidata.sbnwas_cma.base.model.vo.VoWork
import com.familidata.sbnwas_cma.base.model.vo.VoWorkCheckList
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.databinding.FragmentMainWorkBinding
import com.familidata.sbnwas_cma.databinding.ItemWorkCheckBinding

class MainWorkFragment : PFragment() {

    private lateinit var binding: FragmentMainWorkBinding
    internal var list: List<PBean> = ArrayList()
    private lateinit var mListAdapter: ListAdapter
    private fun setCallback(controller: IController?, model: PViewModel?) {
        this.controller = controller
        this.model = model
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_work, container, false)
        binding.controller = controller
        val mLinearLayoutManager = LinearLayoutManager(activity)
        mLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        binding.rv.layoutManager = mLinearLayoutManager
        binding.rv.setHasFixedSize(true)
        binding.rv.itemAnimator = DefaultItemAnimator()
        mListAdapter = ListAdapter(requireContext())
        binding.rv.adapter = mListAdapter

        model?.let {
            it.getItem().observe(viewLifecycleOwner) { bean ->
                bean?.let {
                    binding.vo = (bean as VoWork)
                }
            }
            it.mitem2.observe(viewLifecycleOwner) { bean ->
                bean?.let {
                    binding.voCheck = (bean as VoWorkCheckList)
                }
            }
            it.mList.observe(viewLifecycleOwner) { l ->

                l?.let {
                    if (l.isNotEmpty()) {
                        (l[0] as VoWorkCheckList).atFirst = true
                    }
                    list = l
                    mListAdapter = ListAdapter(requireContext())
                    binding.rv.adapter = mListAdapter

                }

            }
        }
        return binding.root
    }

    internal inner class ListAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun getItemViewType(position: Int): Int {
            return list[position].viewType
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                PBean.TYPE_A -> {
                    val binding = ItemWorkCheckBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                    AViewHolder(binding)
                }
                else -> {
                    val binding = ItemWorkCheckBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
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
            viewHolder.bind(list[position] as VoWorkCheckList)
        }


        override fun getItemCount(): Int {
            return list.size
        }

        internal inner class AViewHolder internal constructor(binder: ItemWorkCheckBinding) : RecyclerView.ViewHolder(binder.root) {
            private val ibinding: ItemWorkCheckBinding = binder
            internal fun bind(vo: VoWorkCheckList) {
                ibinding.controller = controller
                ibinding.voCheck = vo
            }
        }
    }

    companion object {
        fun with(controller: IController?, model: PViewModel?): MainWorkFragment {
            val frag = MainWorkFragment()
            frag.setCallback(controller, model)
            return frag
        }
    }
}