package com.familidata.sbnwas_cma.main.bio

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
import com.familidata.sbnwas_cma.databinding.ItemBioMonitorDataBinding
import com.familidata.sbnwas_cma.databinding.ItemBioMonitorDateBinding

class BioCommonListFragment : PFragment() {

    private lateinit var binding: FragmentCommonBioMonitorListBinding

    internal var list: List<PBean> = ArrayList()
    private lateinit var mListAdapter: ListAdapter
    private var title: String = ""
    private fun setCallback(controller: IController, model: PViewModel?) {
        this.controller = controller
        this.model = model
    }

    private fun setCallback(controller: IController, model: PViewModel?, title: String) {
        this.controller = controller
        this.model = model
        this.title = title
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_common_bio_monitor_list, container, false)
        binding.controller = controller
        binding.title = title
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
            it.getItem().observe(viewLifecycleOwner) { one ->
                one?.let { it2 ->
                    if (it2 is VoBioBlp) {
                        binding.blp.root.visibility = View.VISIBLE
                        binding.blp.bean = it2
                    } else if (it2 is VoBioBls) {
                        binding.bls.root.visibility = View.VISIBLE
                        binding.bls.bean = it2
                    } else if (it2 is VoBioBas) {
                        if (it2.viewType == PBean.TYPE_BAS_HSM) {
                            binding.basHsm.root.visibility = View.VISIBLE
                            binding.basHsm.bean = it2
                            binding.basHsm.controller = controller
                        } else {
                            binding.basLsm.root.visibility = View.VISIBLE
                            binding.basLsm.bean = it2
                            binding.basLsm.controller = controller
                        }
                    } else if (it2 is VoBioBdy) {
                        when (it2.viewType) {
                            PBean.TYPE_BDY_A -> {
                                binding.bdyA.root.visibility = View.VISIBLE
                                binding.bdyA.bean = it2
                            }

                            PBean.TYPE_BDY_B -> {
                                binding.bdyB.root.visibility = View.VISIBLE
                                binding.bdyB.bean = it2
                            }

                            PBean.TYPE_BDY_C -> {
                                binding.bdyC.root.visibility = View.VISIBLE
                                binding.bdyC.bean = it2
                            }

                            PBean.TYPE_BDY_D -> {
                                binding.bdyD.root.visibility = View.VISIBLE
                                binding.bdyD.bean = it2
                            }

                            PBean.TYPE_BDY_F -> {
                                binding.bdyF.root.visibility = View.VISIBLE
                                binding.bdyF.bean = it2
                            }

                            PBean.TYPE_BDY_G -> {
                                binding.bdyG.root.visibility = View.VISIBLE
                                binding.bdyG.bean = it2
                            }
                        }
                    } else if (it2 is VoBioEcgHeader) {
                        binding.ecg.root.visibility = View.VISIBLE
                        binding.ecg.bean = it2
                    } else if (it2 is VoBioHtr) {
                        binding.htr.root.visibility = View.VISIBLE
                        binding.htr.bean = it2
                    } else if (it2 is VoBioOxs) {
                        binding.oxs.root.visibility = View.VISIBLE
                        binding.oxs.bean = it2
                    } else if (it2 is VoBioSleepHeader) {
                        binding.sleep.root.visibility = View.VISIBLE
                        binding.sleep.bean = it2
                        binding.sleep.controller = controller
                    } else if (it2 is VoBioTmm) {
                        binding.tmm.root.visibility = View.VISIBLE
                        binding.tmm.bean = it2
                        binding.tmm.controller = controller
                    } else if (it2 is VoBioCalorie) {
                        binding.calorie.root.visibility = View.VISIBLE
                        binding.calorie.bean = it2
                        binding.calorie.controller = controller
                    } else if (it2 is VoBioStep) {
                        binding.step.root.visibility = View.VISIBLE
                        binding.step.bean = it2
                        binding.step.controller = controller
                    } else if (it2 is VoBioDistance) {
                        binding.distance.root.visibility = View.VISIBLE
                        binding.distance.bean = it2
                        binding.distance.controller = controller
                    } else if (it2 is VoBioActivity) {
                        binding.activity.root.visibility = View.VISIBLE
                        binding.activity.bean = it2
                        binding.activity.controller = controller
                    } else if (it2 is VoBioCho) {
                        when (it2.viewType) {
                            PBean.TYPE_CHO_TC -> {
                                binding.choTc.root.visibility = View.VISIBLE
                                binding.choTc.bean = it2
                            }

                            PBean.TYPE_CHO_TG -> {
                                binding.choTg.root.visibility = View.VISIBLE
                                binding.choTg.bean = it2
                            }

                            PBean.TYPE_CHO_HDL -> {
                                binding.choHdl.root.visibility = View.VISIBLE
                                binding.choHdl.bean = it2
                            }

                            PBean.TYPE_CHO_LDL -> {
                                binding.choLdl.root.visibility = View.VISIBLE
                                binding.choLdl.bean = it2
                            }
                        }
                    }
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
                PBean.TYPE_DATE -> {
                    val binding = ItemBioMonitorDateBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                    AViewHolder(binding)
                }

                else -> {
                    val binding = ItemBioMonitorDataBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
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
            viewHolder.bind(list[position] as VoBioMonitorData)
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

        internal inner class DataViewHolder internal constructor(binder: ItemBioMonitorDataBinding) : RecyclerView.ViewHolder(binder.root) {
            private val ibinding: ItemBioMonitorDataBinding = binder
            internal fun bind(vo: VoBioMonitorData) {
                ibinding.controller = controller
                ibinding.bean = vo
            }
        }
    }

    companion object {
        fun with(controller: IController, model: PViewModel?): BioCommonListFragment {
            val frag = BioCommonListFragment()
            frag.setCallback(controller, model)
            return frag
        }

        fun with(controller: IController, model: PViewModel?, title: String): BioCommonListFragment {
            val frag = BioCommonListFragment()
            frag.setCallback(controller, model, title)
            return frag
        }
    }
}