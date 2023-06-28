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
import com.familidata.sbnwas_cma.base.model.vo.*
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.databinding.*
import com.familidata.sbnwas_cma.rx.RxBusObj
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
@Deprecated("NO LONGER USEFUL", level = DeprecationLevel.WARNING)
class MainBodyFragment : PFragment() {

    private lateinit var binding: FragmentMainBodyBinding
    internal var list = mutableListOf<VoBio>()
    private lateinit var mListAdapter: ListAdapter

    private fun setCallback(controller: IController?, model: PViewModel?) {
        this.controller = controller
        this.model = model
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_body, container, false)
        binding.controller = controller

        val mLinearLayoutManager = LinearLayoutManager(activity)
        mLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        binding.rv.layoutManager = mLinearLayoutManager
        binding.rv.setHasFixedSize(true)
        binding.rv.itemAnimator = DefaultItemAnimator()
        mListAdapter = ListAdapter(requireContext())
        binding.rv.adapter = mListAdapter

        binding.ecgDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd (E)"))

        model?.let {
//            it.mitem.observe(viewLifecycleOwner) { bean ->
//                if (bean != null) mListAdapter.addCheck(bean = bean as VoBio)
//            }
            it.getList()

        }
        return binding.root
    }

    fun setItem(bean: PBean?) {
        if (bean != null) mListAdapter.addCheck(bean = bean as VoBio)
    }

    fun setBdy(vo1: VoBioBdy?, vo2: VoBioBdy?, vo3: VoBioBdy?, vo4: VoBioBdy?, vo5: VoBioBdy?) {
        binding.clAllBMI.visibility = View.VISIBLE
        vo1?.let {
            binding.bmiA = vo1
        }
        vo2?.let {
            binding.bmiB = vo2
        }
        vo3?.let {
            binding.bmiC = vo3
        }
        vo4?.let {
            binding.bmiD = vo4
        }
        vo5?.let {
            binding.bmiE = vo5
        }
    }


    fun setWatchBind(vo: PBean) {
        when (vo) {
            is VoBioBlp -> {
                binding.watchBlp = vo
            }

            is VoBioHtr -> {
                binding.watchHrt = vo
            }

            is VoBioOxs -> {
                binding.watchOxs = vo
            }

            is VoBioEcgHeader -> {
                binding.ecgDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd (E)"))
                binding.ivEcg.visibility = View.VISIBLE
                binding.clWatchEcg.visibility = View.VISIBLE
            }

            is VoBioSleepHeader -> {
                binding.watchSleep = vo
                binding.clWatchSleep.visibility = View.VISIBLE
                binding.tvSt.visibility = View.VISIBLE
                binding.tvSm.visibility = View.VISIBLE
            }

            is VoBioCalorie -> {
                binding.watchCalorie = vo
            }

            is VoBioStep -> {
                binding.watchStep = vo
            }

            is VoBioDistance -> {
                binding.watchDistance = vo
            }

            is VoBioActivity -> {
                binding.watchActivity = vo
            }
        }
    }

    fun hideWatchData(busKey: String) {
        when (busKey) {
            RxBusObj.BIO_WATCH_SLEEP -> {
//                binding.clWatchSleep.visibility = View.GONE
                binding.tvSt.visibility = View.GONE
                binding.tvSm.visibility = View.GONE
            }
//            RxBusObj.BIO_WATCH_ECG -> {
//                binding.clWatchEcg.visibility = View.GONE
//            }
//            RxBusObj.BIO_WATCH_BLP -> {
//                binding.clWatchBlp.visibility = View.GONE
//            }
//            RxBusObj.BIO_WATCH_HTR -> {
//                binding.clWatchHtr.visibility = View.GONE
//            }
//            RxBusObj.BIO_WATCH_OXY -> {
//                binding.clWatchOxs.visibility = View.GONE
//            }
        }
    }

    fun hideWatch() {
//        binding.clWatch.visibility = View.GONE
    }

    internal inner class ListAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        fun update(position: Int) {
            notifyItemChanged(position, list[position])
        }

        fun insert() {
            notifyItemInserted(list.size)
        }

        fun addCheck(bean: VoBio) {
            for ((i, temp: VoBio) in list.withIndex()) {
                if (temp.deviceid == bean.deviceid) {
                    temp.first = bean.first
                    temp.second = bean.second
                    temp.date = bean.date
                    temp.entity = bean.entity
                    update(i)
                    return
                }
            }
            list.add(bean)
            notifyDataSetChanged()
        }

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
        fun with(controller: IController?, model: PViewModel?): MainBodyFragment {
            val frag = MainBodyFragment()
            frag.setCallback(controller, model)
            return frag
        }
    }
}