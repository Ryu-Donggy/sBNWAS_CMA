package com.familidata.sbnwas_cma.main

import android.content.Context
import android.os.Bundle
import android.util.SparseBooleanArray
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.marginTop
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.familidata.base.Log
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.PViewModel
import com.familidata.sbnwas_cma.base.model.vo.*
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.databinding.*
import com.familidata.sbnwas_cma.room.entity.BioBdy
import com.familidata.sbnwas_cma.rx.RxBusObj
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainBodyCompostionPageFragment : PFragment() {

    private lateinit var binding: FragmentMainBodyCompostionPageBinding

    internal var list: List<PBean> = ArrayList()
    private lateinit var mListAdapter: ListAdapter
    private fun setCallback(controller: IController?, model: PViewModel?) {
        this.controller = controller
        this.model = model
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_body_compostion_page, container, false)
        binding.controller = controller
        model?.let {
            it.getList().observe(viewLifecycleOwner) { tempList ->
                list = tempList
                mListAdapter = ListAdapter(requireContext())
                binding.rv.adapter = mListAdapter
                if (list.isEmpty() || list.size <= 1) {
                    binding.rv.visibility = View.GONE
                    binding.x.margin(top = 0f)
                } else {
                    binding.rv.visibility = View.VISIBLE
                    binding.x.margin(top = -12f)
                }
            }
        }
        val mLinearLayoutManager = LinearLayoutManager(activity)
        mLinearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        binding.rv.layoutManager = mLinearLayoutManager
        binding.rv.setHasFixedSize(true)
        binding.rv.itemAnimator = DefaultItemAnimator()

        return binding.root
    }

    fun deviceSelect(vo: VoDevice) {
        binding.selected = vo.model.DEVICE_COMPANY + " - " + vo.model.DEVICE_MODEL
        for ((i, temp: PBean) in list.withIndex()) {
            if (temp is VoDevice) {
                temp.isSelected = temp == vo
                mListAdapter.update(i)
            }
        }
    }

    fun setDeviceSelectByID(deviceId: String) {
        for ((i, temp: PBean) in list.withIndex()) {
            if (temp is VoDevice) {
                if (temp.model.DEVICE_ID == deviceId) {
                    temp.isSelected = true
                    binding.selected = temp.model.DEVICE_COMPANY + " - " + temp.model.DEVICE_MODEL
                } else {
                    temp.isSelected = false
                }
                mListAdapter.update(i)
            }
        }
    }

    fun setBdy(vo1: VoBioBdy?, vo2: VoBioBdy?, vo3: VoBioBdy?, vo4: VoBioBdy?, vo5: VoBioBdy?, vo6: VoBioBdy?, vo7: VoBioBdy?) {

        vo1?.let {
            binding.bmiA = vo1
        } ?: run {
            binding.tvWeightSecond.text = "-"
        }
        vo2?.let {
            binding.bmiB = vo2
        } ?: run {
            binding.tvFirstBFM.text = "-"
        }
        vo3?.let {
            binding.bmiC = vo3
        } ?: run {
            binding.tvBoSecond.text = "-"
        }
        vo4?.let {
            binding.bmiD = vo4
        } ?: run {
            binding.tvFirstBMI.text = "-"
        }
        vo5?.let {
            binding.bmiE = vo5
            if (vo5.first == "") {
                binding.tvDesc.visibility = View.GONE
            } else {
                binding.tvDesc.visibility = View.VISIBLE
            }
        } ?: run {
            binding.tvDesc.visibility = View.GONE
        }
        vo6?.let {
            binding.bmiF = vo6
        } ?: run {
            binding.tvBodyWater.text = "-"
        }
        vo7?.let {
            binding.bmiG = vo7
        } ?: run {
            binding.tvFirstBFMPropotion.text = "-"
        }
    }


    fun View.margin(left: Float? = null, top: Float? = null, right: Float? = null, bottom: Float? = null) {
        layoutParams<ViewGroup.MarginLayoutParams> {
            left?.run { leftMargin = dpToPx(this) }
            top?.run { topMargin = dpToPx(this) }
            right?.run { rightMargin = dpToPx(this) }
            bottom?.run { bottomMargin = dpToPx(this) }
        }
    }

    inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) {
        if (layoutParams is T) block(layoutParams as T)
    }

    fun View.dpToPx(dp: Float): Int = context.dpToPx(dp)
    fun Context.dpToPx(dp: Float): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics).toInt()

    internal inner class ListAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private val selectedItems: SparseBooleanArray = SparseBooleanArray()

        fun update(position: Int) {
            notifyItemChanged(position, list[position])
        }

        override fun getItemViewType(position: Int): Int {
            return list[position].viewType
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                PBean.TYPE_A -> {
                    val binding = ItemDeviceSimpleBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                    AViewHolder(binding)
                }

                else -> {
                    val binding = ItemDeviceSimpleBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
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

        internal inner class AViewHolder internal constructor(binder: ItemDeviceSimpleBinding) : RecyclerView.ViewHolder(binder.root) {
            private val ibinding: ItemDeviceSimpleBinding = binder
            internal fun bind(vo: VoDevice) {
                ibinding.controller = controller
                ibinding.bean = vo
            }
        }
    }

    companion object {
        fun with(controller: IController?, model: PViewModel?): MainBodyCompostionPageFragment {
            val frag = MainBodyCompostionPageFragment()
            frag.setCallback(controller, model)
            return frag
        }
    }
}