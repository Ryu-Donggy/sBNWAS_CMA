package com.familidata.sbnwas_cma.main.analyze

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
import com.familidata.sbnwas_cma.base.model.vo.VoMedicalCheckUpInfo
import com.familidata.sbnwas_cma.base.model.vo.VoBioMonitorDate
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.databinding.FragmentMedicalCheckUpAnalzeBodyListBinding
import com.familidata.sbnwas_cma.databinding.ItemMcuAnalzeDataBinding
import com.familidata.sbnwas_cma.databinding.ItemMcuAnalzeTopBinding

class MedicalCheckUpBodyListFragment : PFragment() {

    private lateinit var binding: FragmentMedicalCheckUpAnalzeBodyListBinding

    internal var list: List<PBean> = ArrayList()
    private lateinit var mListAdapter: ListAdapter
    private fun setCallback(controller: IController, model: PViewModel?) {
        this.controller = controller
        this.model = model
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_medical_check_up_analze_body_list, container, false)
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
                mListAdapter = ListAdapter(requireContext())
                binding.rv.adapter = mListAdapter
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
                PBean.TYPE_ANALY_TOP -> {
                    val binding = ItemMcuAnalzeTopBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                    AViewHolder(binding)
                }

                else -> {
                    val binding = ItemMcuAnalzeDataBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
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
            viewHolder.bind(list[position] as VoMedicalCheckUpInfo)
        }

        private fun onDataItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewHolder = holder as DataViewHolder
            if (list.isEmpty()) return
            viewHolder.bind(list[position] as VoMedicalCheckUpInfo)
        }


        override fun getItemCount(): Int {
            return list.size
        }

        internal inner class AViewHolder internal constructor(binder: ItemMcuAnalzeTopBinding) : RecyclerView.ViewHolder(binder.root) {
            private val ibinding: ItemMcuAnalzeTopBinding = binder
            internal fun bind(vo: VoMedicalCheckUpInfo) {
                ibinding.controller = controller
                ibinding.bean = vo
            }
        }

        internal inner class DataViewHolder internal constructor(binder: ItemMcuAnalzeDataBinding) : RecyclerView.ViewHolder(binder.root) {
            private val ibinding: ItemMcuAnalzeDataBinding = binder
            internal fun bind(vo: VoMedicalCheckUpInfo) {
                ibinding.controller = controller
                ibinding.bean = vo
            }
        }
    }

    companion object {
        fun with(controller: IController, model: PViewModel?): MedicalCheckUpBodyListFragment {
            val frag = MedicalCheckUpBodyListFragment()
            frag.setCallback(controller, model)
            return frag
        }

    }
}