package com.familidata.sbnwas_cma.main.bio

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.PViewModel
import com.familidata.sbnwas_cma.base.model.vo.VoCandleStickEntry
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.databinding.FragmentCommonBioMonitorCandleChartBinding
import com.familidata.sbnwas_cma.databinding.FragmentCommonBioSimpleTextBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.CandleData
import com.github.mikephil.charting.data.CandleDataSet
import com.github.mikephil.charting.data.CandleEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

class BioCommonSimpleTextFragment : PFragment() {

    private lateinit var binding: FragmentCommonBioSimpleTextBinding
    internal var list: List<PBean> = ArrayList()

    private fun setCallback(controller: IController, model: PViewModel?) {
        this.controller = controller
        this.model = model
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_common_bio_simple_text, container, false)
        binding.controller = controller
        binding.lifecycleOwner = this

        return binding.root
    }


    companion object {
        fun with(controller: IController, model: PViewModel?): BioCommonSimpleTextFragment {
            val frag = BioCommonSimpleTextFragment()
            frag.setCallback(controller, model)
            return frag
        }
    }
}