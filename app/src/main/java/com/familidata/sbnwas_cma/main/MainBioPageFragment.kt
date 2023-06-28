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
import com.familidata.sbnwas_cma.base.model.vo.VoBioBas
import com.familidata.sbnwas_cma.base.model.vo.VoBioBdy
import com.familidata.sbnwas_cma.base.model.vo.VoBioBlp
import com.familidata.sbnwas_cma.base.model.vo.VoBioBls
import com.familidata.sbnwas_cma.base.model.vo.VoBioHtr
import com.familidata.sbnwas_cma.base.model.vo.VoBioOxs
import com.familidata.sbnwas_cma.base.model.vo.VoBioTmm
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.databinding.FragmentMainBioPageBinding
import com.familidata.sbnwas_cma.databinding.ItemBdyABinding
import com.familidata.sbnwas_cma.databinding.ItemBdyBBinding
import com.familidata.sbnwas_cma.databinding.ItemBdyCBinding
import com.familidata.sbnwas_cma.databinding.ItemBdyDBinding
import com.familidata.sbnwas_cma.databinding.ItemBioBasHsmBinding
import com.familidata.sbnwas_cma.databinding.ItemBioBasLsmBinding
import com.familidata.sbnwas_cma.databinding.ItemBioBlpBinding
import com.familidata.sbnwas_cma.databinding.ItemBioBlsBinding
import com.familidata.sbnwas_cma.databinding.ItemBioHtrBinding
import com.familidata.sbnwas_cma.databinding.ItemBioOxsBinding
import com.familidata.sbnwas_cma.databinding.ItemBioTmmBinding

class MainBioPageFragment : PFragment() {

    private lateinit var binding: FragmentMainBioPageBinding

    private fun setCallback(controller: IController?, model: PViewModel?) {
        this.controller = controller
        this.model = model
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_bio_page, container, false)
        binding.controller = controller
        model?.getList()
        return binding.root
    }

    private var basList = mutableListOf<VoBio>()
    fun setBas(lms: VoBioBas?, hms: VoBioBas?) {
        lms?.let {
            addCheck(basList as ArrayList<VoBio>, it)
        }
        hms?.let {
            addCheck(basList as ArrayList<VoBio>, it)
        }
        if (basList.size > 0) binding.llBas.visibility = View.VISIBLE
        val mLinearLayoutManager = LinearLayoutManager(activity)
        mLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvSkeeper.layoutManager = mLinearLayoutManager
        binding.rvSkeeper.setHasFixedSize(true)
        binding.rvSkeeper.itemAnimator = DefaultItemAnimator()
        val mListAdapter = ListAdapter(requireContext(), basList as ArrayList<VoBio>)
        binding.rvSkeeper.adapter = mListAdapter
    }


    private var blsList = mutableListOf<VoBio>()
    fun setBls(lms: VoBioBls) {
        addCheck(blsList as ArrayList<VoBio>, lms)
        if (blsList.size > 0) binding.llBls.visibility = View.VISIBLE
        val mLinearLayoutManager = LinearLayoutManager(activity)
        mLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvBls.layoutManager = mLinearLayoutManager
        binding.rvBls.setHasFixedSize(true)
        binding.rvBls.itemAnimator = DefaultItemAnimator()
        val mListAdapter = ListAdapter(requireContext(), blsList as ArrayList<VoBio>)
        binding.rvBls.adapter = mListAdapter
    }

    private var blpList = mutableListOf<VoBio>()
    fun setBlp(lms: VoBioBlp) {
        addCheck(blpList as ArrayList<VoBio>, lms)
        if (blpList.size > 0) binding.llBlp.visibility = View.VISIBLE
        val mLinearLayoutManager = LinearLayoutManager(activity)
        mLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvBlp.layoutManager = mLinearLayoutManager
        binding.rvBlp.setHasFixedSize(true)
        binding.rvBlp.itemAnimator = DefaultItemAnimator()
        val mListAdapter = ListAdapter(requireContext(), blpList as ArrayList<VoBio>)
        binding.rvBlp.adapter = mListAdapter
    }

    private var oxsList = mutableListOf<VoBio>()
    fun setOxs(lms: VoBioOxs) {
        addCheck(oxsList as ArrayList<VoBio>, lms)
        if (oxsList.size > 0) binding.llOxs.visibility = View.VISIBLE
        val mLinearLayoutManager = LinearLayoutManager(activity)
        mLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvOxy.layoutManager = mLinearLayoutManager
        binding.rvOxy.setHasFixedSize(true)
        binding.rvOxy.itemAnimator = DefaultItemAnimator()
        val mListAdapter = ListAdapter(requireContext(), oxsList as ArrayList<VoBio>)
        binding.rvOxy.adapter = mListAdapter
    }

    private var htrList = mutableListOf<VoBio>()
    fun setHtr(lms: VoBioHtr) {
        addCheck(htrList as ArrayList<VoBio>, lms)
        if (htrList.size > 0) binding.llHtr.visibility = View.VISIBLE
        val mLinearLayoutManager = LinearLayoutManager(activity)
        mLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvHtr.layoutManager = mLinearLayoutManager
        binding.rvHtr.setHasFixedSize(true)
        binding.rvHtr.itemAnimator = DefaultItemAnimator()
        val mListAdapter = ListAdapter(requireContext(), htrList as ArrayList<VoBio>)
        binding.rvHtr.adapter = mListAdapter
    }


    private var tmmList = mutableListOf<VoBio>()
    fun setTmm(lms: VoBioTmm) {
        addCheck(tmmList as ArrayList<VoBio>, lms)
        if (tmmList.size > 0) binding.llTmm.visibility = View.VISIBLE
        val mLinearLayoutManager = LinearLayoutManager(activity)
        mLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvTmm.layoutManager = mLinearLayoutManager
        binding.rvTmm.setHasFixedSize(true)
        binding.rvTmm.itemAnimator = DefaultItemAnimator()
        val mListAdapter = ListAdapter(requireContext(), tmmList as ArrayList<VoBio>)
        binding.rvTmm.adapter = mListAdapter
    }

    private fun addCheck(list: ArrayList<VoBio>, bean: VoBio) {
        for (temp: VoBio in list) {
            if (temp.deviceid == bean.deviceid) {
                temp.first = bean.first
                temp.second = bean.second
                temp.date = bean.date
                temp.entity = bean.entity
                return
            }
        }
        list.add(bean)
    }

    internal inner class ListAdapter(val context: Context, val list: ArrayList<VoBio>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        fun update(position: Int) {
            notifyItemChanged(position, list[position])
        }

        fun insert() {
            notifyItemInserted(list.size)
        }

//        fun addCheck(bean: VoBio) {
//            for ((i, temp: VoBio) in list.withIndex()) {
//                if (temp.deviceid == bean.deviceid) {
//                    temp.first = bean.first
//                    temp.second = bean.second
//                    temp.date = bean.date
//                    temp.entity = bean.entity
//                    update(i)
//                    return
//                }
//            }
//            list.add(bean)
//            notifyDataSetChanged()
//        }

        override fun getItemViewType(position: Int): Int {
            return list[position].viewType
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                PBean.TYPE_BLP -> {
                    val binding = ItemBioBlpBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                    BlpViewHolder(binding)
                }

                PBean.TYPE_HTR -> {
                    val binding = ItemBioHtrBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                    HtrViewHolder(binding)
                }

                PBean.TYPE_OXS -> {
                    val binding = ItemBioOxsBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                    OxsViewHolder(binding)
                }

                PBean.TYPE_BLS -> {
                    val binding = ItemBioBlsBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                    BlsViewHolder(binding)
                }

                PBean.TYPE_BAS_HSM -> {
                    val binding = ItemBioBasHsmBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                    BasHsmViewHolder(binding)
                }

                PBean.TYPE_BAS_LSM -> {
                    val binding = ItemBioBasLsmBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                    BasLsmViewHolder(binding)
                }

                PBean.TYPE_BDY_A -> {
                    val binding = ItemBdyABinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                    BdyAViewHolder(binding)
                }

                PBean.TYPE_BDY_B -> {
                    val binding = ItemBdyBBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                    BdyBViewHolder(binding)
                }

                PBean.TYPE_BDY_C -> {
                    val binding = ItemBdyCBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                    BdyCViewHolder(binding)
                }

                PBean.TYPE_BDY_D -> {
                    val binding = ItemBdyDBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                    BdyDViewHolder(binding)
                }

                PBean.TYPE_TMM -> {
                    val binding = ItemBioTmmBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                    TmmViewHolder(binding)
                }

                else -> {
                    val binding = ItemBioBlpBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                    BlpViewHolder(binding)
                }
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder) {
                is BlpViewHolder -> {
                    onBlpItemViewHolder(holder, position)
                }

                is BlsViewHolder -> {
                    onBlsItemViewHolder(holder, position)
                }

                is OxsViewHolder -> {
                    onOxsItemViewHolder(holder, position)
                }

                is HtrViewHolder -> {
                    onHtrItemViewHolder(holder, position)
                }

                is BasHsmViewHolder -> {
                    onBasHsmItemViewHolder(holder, position)
                }

                is BasLsmViewHolder -> {
                    onBasLsmItemViewHolder(holder, position)
                }

                is BdyAViewHolder -> {
                    onBdyAItemViewHolder(holder, position)
                }

                is BdyBViewHolder -> {
                    onBdyBItemViewHolder(holder, position)
                }

                is BdyCViewHolder -> {
                    onBdyCItemViewHolder(holder, position)
                }

                is BdyDViewHolder -> {
                    onBdyDItemViewHolder(holder, position)
                }

                is TmmViewHolder -> {
                    onTmmItemViewHolder(holder, position)
                }
            }
        }

        private fun onBlpItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewHolder = holder as BlpViewHolder
            if (list.isEmpty()) return
            viewHolder.bind(list[position] as VoBioBlp)
        }

        private fun onBlsItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewHolder = holder as BlsViewHolder
            if (list.isEmpty()) return
            viewHolder.bind(list[position] as VoBioBls)
        }

        private fun onOxsItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewHolder = holder as OxsViewHolder
            if (list.isEmpty()) return
            viewHolder.bind(list[position] as VoBioOxs)
        }

        private fun onHtrItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewHolder = holder as HtrViewHolder
            if (list.isEmpty()) return
            viewHolder.bind(list[position] as VoBioHtr)
        }

        private fun onBasHsmItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewHolder = holder as BasHsmViewHolder
            if (list.isEmpty()) return
            viewHolder.bind(list[position] as VoBioBas)
        }

        private fun onBasLsmItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewHolder = holder as BasLsmViewHolder
            if (list.isEmpty()) return
            viewHolder.bind(list[position] as VoBioBas)
        }

        private fun onBdyAItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewHolder = holder as BdyAViewHolder
            if (list.isEmpty()) return
            viewHolder.bind(list[position] as VoBioBdy)
        }

        private fun onBdyBItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewHolder = holder as BdyBViewHolder
            if (list.isEmpty()) return
            viewHolder.bind(list[position] as VoBioBdy)
        }

        private fun onBdyCItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewHolder = holder as BdyCViewHolder
            if (list.isEmpty()) return
            viewHolder.bind(list[position] as VoBioBdy)
        }

        private fun onBdyDItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewHolder = holder as BdyDViewHolder
            if (list.isEmpty()) return
            viewHolder.bind(list[position] as VoBioBdy)
        }

        private fun onTmmItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewHolder = holder as TmmViewHolder
            if (list.isEmpty()) return
            viewHolder.bind(list[position] as VoBioTmm)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        internal inner class BlpViewHolder internal constructor(binder: ItemBioBlpBinding) : RecyclerView.ViewHolder(binder.root) {
            private val ibinding: ItemBioBlpBinding = binder
            internal fun bind(vo: VoBioBlp) {
                ibinding.controller = controller
                ibinding.bean = vo
            }
        }

        internal inner class OxsViewHolder internal constructor(binder: ItemBioOxsBinding) : RecyclerView.ViewHolder(binder.root) {
            private val ibinding: ItemBioOxsBinding = binder
            internal fun bind(vo: VoBioOxs) {
                ibinding.controller = controller
                ibinding.bean = vo
            }
        }

        internal inner class BlsViewHolder internal constructor(binder: ItemBioBlsBinding) : RecyclerView.ViewHolder(binder.root) {
            private val ibinding: ItemBioBlsBinding = binder
            internal fun bind(vo: VoBioBls) {
                ibinding.controller = controller
                ibinding.bean = vo
            }
        }

        internal inner class HtrViewHolder internal constructor(binder: ItemBioHtrBinding) : RecyclerView.ViewHolder(binder.root) {
            private val ibinding: ItemBioHtrBinding = binder
            internal fun bind(vo: VoBioHtr) {
                ibinding.controller = controller
                ibinding.bean = vo
            }
        }

        internal inner class BasHsmViewHolder internal constructor(binder: ItemBioBasHsmBinding) : RecyclerView.ViewHolder(binder.root) {
            private val ibinding: ItemBioBasHsmBinding = binder
            internal fun bind(vo: VoBioBas) {
                ibinding.controller = controller
                ibinding.bean = vo
            }
        }

        internal inner class BasLsmViewHolder internal constructor(binder: ItemBioBasLsmBinding) : RecyclerView.ViewHolder(binder.root) {
            private val ibinding: ItemBioBasLsmBinding = binder
            internal fun bind(vo: VoBioBas) {
                ibinding.controller = controller
                ibinding.bean = vo
            }
        }

        internal inner class BdyAViewHolder internal constructor(binder: ItemBdyABinding) : RecyclerView.ViewHolder(binder.root) {
            private val ibinding: ItemBdyABinding = binder
            internal fun bind(vo: VoBioBdy) {
                ibinding.controller = controller
                ibinding.bean = vo
            }
        }

        internal inner class BdyBViewHolder internal constructor(binder: ItemBdyBBinding) : RecyclerView.ViewHolder(binder.root) {
            private val ibinding: ItemBdyBBinding = binder
            internal fun bind(vo: VoBioBdy) {
                ibinding.controller = controller
                ibinding.bean = vo
            }
        }

        internal inner class BdyCViewHolder internal constructor(binder: ItemBdyCBinding) : RecyclerView.ViewHolder(binder.root) {
            private val ibinding: ItemBdyCBinding = binder
            internal fun bind(vo: VoBioBdy) {
                ibinding.controller = controller
                ibinding.bean = vo
            }
        }

        internal inner class BdyDViewHolder internal constructor(binder: ItemBdyDBinding) : RecyclerView.ViewHolder(binder.root) {
            private val ibinding: ItemBdyDBinding = binder
            internal fun bind(vo: VoBioBdy) {
                ibinding.controller = controller
                ibinding.bean = vo
            }
        }

        internal inner class TmmViewHolder internal constructor(binder: ItemBioTmmBinding) : RecyclerView.ViewHolder(binder.root) {
            private val ibinding: ItemBioTmmBinding = binder
            internal fun bind(vo: VoBioTmm) {
                ibinding.controller = controller
                ibinding.bean = vo
            }
        }
    }

    companion object {
        fun with(controller: IController?, model: PViewModel?): MainBioPageFragment {
            val frag = MainBioPageFragment()
            frag.setCallback(controller, model)
            return frag
        }
    }
}