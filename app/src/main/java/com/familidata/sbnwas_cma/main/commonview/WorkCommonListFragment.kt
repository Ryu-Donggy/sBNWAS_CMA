package com.familidata.sbnwas_cma.main.commonview

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
import com.familidata.sbnwas_cma.base.model.vo.VoWork
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.databinding.FragmentCommonWorkListBinding
import com.familidata.sbnwas_cma.databinding.ItemWorkBinding

class WorkCommonListFragment : PFragment() {

    private lateinit var binding: FragmentCommonWorkListBinding

    internal var list: List<PBean> = ArrayList()
    private lateinit var mListAdapter: ListAdapter
    private fun setCallback(controller: IController, model: PViewModel?) {
        this.controller = controller
        this.model = model
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_common_work_list, container, false)
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
        mLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL

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
                    val binding = ItemWorkBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                    AViewHolder(binding)
                }
                else -> {
                    val binding = ItemWorkBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
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
            viewHolder.bind(list[position] as VoWork)
        }


        override fun getItemCount(): Int {
            return list.size
        }

        internal inner class AViewHolder internal constructor(binder: ItemWorkBinding) : RecyclerView.ViewHolder(binder.root) {
            private val ibinding: ItemWorkBinding = binder
            internal fun bind(vo: VoWork) {
                ibinding.controller = controller
                ibinding.bean = vo
            }
        }
    }

    companion object {
        fun with(controller: IController, model: PViewModel?): WorkCommonListFragment {
            val frag = WorkCommonListFragment()
            frag.setCallback(controller, model)
            return frag
        }
    }
}