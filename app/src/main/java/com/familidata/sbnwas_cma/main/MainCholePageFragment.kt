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
import com.familidata.sbnwas_cma.base.model.vo.VoBioCho
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.databinding.FragmentMainCholePageBinding
import com.familidata.sbnwas_cma.databinding.ItemBioChoHdlBinding
import com.familidata.sbnwas_cma.databinding.ItemBioChoLdlBinding
import com.familidata.sbnwas_cma.databinding.ItemBioChoTcBinding
import com.familidata.sbnwas_cma.databinding.ItemBioChoTgBinding

class MainCholePageFragment : PFragment() {

    private lateinit var binding: FragmentMainCholePageBinding

    private fun setCallback(controller: IController?, model: PViewModel?) {
        this.controller = controller
        this.model = model
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_chole_page, container, false)
        binding.controller = controller
        model?.getList()
        return binding.root
    }


    private var tcList = mutableListOf<VoBio>()
    fun setTc(lms: VoBioCho) {
        addCheck(tcList as ArrayList<VoBio>, lms)
        if (tcList.size > 0) binding.llChoTc.visibility = View.VISIBLE
        val mLinearLayoutManager = LinearLayoutManager(activity)
        mLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvChoTc.layoutManager = mLinearLayoutManager
        binding.rvChoTc.setHasFixedSize(true)
        binding.rvChoTc.itemAnimator = DefaultItemAnimator()
        val mListAdapter = ListAdapter(requireContext(), tcList as ArrayList<VoBio>)
        binding.rvChoTc.adapter = mListAdapter
    }

    private var tgList = mutableListOf<VoBio>()
    fun setTg(lms: VoBioCho) {
        addCheck(tgList as ArrayList<VoBio>, lms)
        if (tgList.size > 0) binding.llChoTg.visibility = View.VISIBLE
        val mLinearLayoutManager = LinearLayoutManager(activity)
        mLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvChoTg.layoutManager = mLinearLayoutManager
        binding.rvChoTg.setHasFixedSize(true)
        binding.rvChoTg.itemAnimator = DefaultItemAnimator()
        val mListAdapter = ListAdapter(requireContext(), tgList as ArrayList<VoBio>)
        binding.rvChoTg.adapter = mListAdapter
    }

    private var hdlList = mutableListOf<VoBio>()
    fun setHdl(lms: VoBioCho) {
        addCheck(hdlList as ArrayList<VoBio>, lms)
        if (hdlList.size > 0) binding.llChoHdl.visibility = View.VISIBLE
        val mLinearLayoutManager = LinearLayoutManager(activity)
        mLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvHdl.layoutManager = mLinearLayoutManager
        binding.rvHdl.setHasFixedSize(true)
        binding.rvHdl.itemAnimator = DefaultItemAnimator()
        val mListAdapter = ListAdapter(requireContext(), hdlList as ArrayList<VoBio>)
        binding.rvHdl.adapter = mListAdapter
    }

    private var ldlList = mutableListOf<VoBio>()
    fun setLdl(lms: VoBioCho) {
        addCheck(ldlList as ArrayList<VoBio>, lms)
        if (ldlList.size > 0) binding.llChoLdl.visibility = View.VISIBLE
        val mLinearLayoutManager = LinearLayoutManager(activity)
        mLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.rvChoLdl.layoutManager = mLinearLayoutManager
        binding.rvChoLdl.setHasFixedSize(true)
        binding.rvChoLdl.itemAnimator = DefaultItemAnimator()
        val mListAdapter = ListAdapter(requireContext(), ldlList as ArrayList<VoBio>)
        binding.rvChoLdl.adapter = mListAdapter
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


        override fun getItemViewType(position: Int): Int {
            return list[position].viewType
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                PBean.TYPE_CHO_TC -> {
                    val binding = ItemBioChoTcBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                    TCViewHolder(binding)
                }

                PBean.TYPE_CHO_TG -> {
                    val binding = ItemBioChoTgBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                    TGViewHolder(binding)
                }

                PBean.TYPE_CHO_HDL -> {
                    val binding = ItemBioChoHdlBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                    HdlViewHolder(binding)
                }

                else -> {
                    val binding = ItemBioChoLdlBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                    LdlViewHolder(binding)
                }
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder) {
                is TCViewHolder -> {
                    onTcItemViewHolder(holder, position)
                }

                is TGViewHolder -> {
                    onTgItemViewHolder(holder, position)
                }

                is HdlViewHolder -> {
                    onHdlItemViewHolder(holder, position)
                }

                is LdlViewHolder -> {
                    onLdlItemViewHolder(holder, position)
                }
            }
        }

        private fun onTcItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewHolder = holder as TCViewHolder
            if (list.isEmpty()) return
            viewHolder.bind(list[position] as VoBioCho)
        }

        private fun onTgItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewHolder = holder as TGViewHolder
            if (list.isEmpty()) return
            viewHolder.bind(list[position] as VoBioCho)
        }

        private fun onHdlItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewHolder = holder as HdlViewHolder
            if (list.isEmpty()) return
            viewHolder.bind(list[position] as VoBioCho)
        }

        private fun onLdlItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewHolder = holder as LdlViewHolder
            if (list.isEmpty()) return
            viewHolder.bind(list[position] as VoBioCho)
        }

        override fun getItemCount(): Int {
            return list.size
        }

        internal inner class TCViewHolder internal constructor(binder: ItemBioChoTcBinding) : RecyclerView.ViewHolder(binder.root) {
            private val ibinding: ItemBioChoTcBinding = binder
            internal fun bind(vo: VoBioCho) {
                ibinding.controller = controller
                ibinding.bean = vo
            }
        }

        internal inner class TGViewHolder internal constructor(binder: ItemBioChoTgBinding) : RecyclerView.ViewHolder(binder.root) {
            private val ibinding: ItemBioChoTgBinding = binder
            internal fun bind(vo: VoBioCho) {
                ibinding.controller = controller
                ibinding.bean = vo
            }
        }

        internal inner class HdlViewHolder internal constructor(binder: ItemBioChoHdlBinding) : RecyclerView.ViewHolder(binder.root) {
            private val ibinding: ItemBioChoHdlBinding = binder
            internal fun bind(vo: VoBioCho) {
                ibinding.controller = controller
                ibinding.bean = vo
            }
        }

        internal inner class LdlViewHolder internal constructor(binder: ItemBioChoLdlBinding) : RecyclerView.ViewHolder(binder.root) {
            private val ibinding: ItemBioChoLdlBinding = binder
            internal fun bind(vo: VoBioCho) {
                ibinding.controller = controller
                ibinding.bean = vo
            }
        }
    }

    companion object {
        fun with(controller: IController?, model: PViewModel?): MainCholePageFragment {
            val frag = MainCholePageFragment()
            frag.setCallback(controller, model)
            return frag
        }
    }
}