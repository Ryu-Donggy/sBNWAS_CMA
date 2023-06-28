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
import com.familidata.sbnwas_cma.databinding.FragmentCommonBioMonitorCombinedLineBarChartBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

class BioCommonCombinedLineBarChartFragment : PFragment() {

    private lateinit var binding: FragmentCommonBioMonitorCombinedLineBarChartBinding
    internal var list: List<PBean> = ArrayList()

    private fun setCallback(controller: IController, model: PViewModel?) {
        this.controller = controller
        this.model = model
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_common_bio_monitor_combined_line_bar_chart, container, false)
        binding.controller = controller
        binding.lifecycleOwner = this

        val mLinearLayoutManager = LinearLayoutManager(activity)
        mLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        model?.let {

            it.getItem().observe(viewLifecycleOwner) { temp ->
                temp?.let {
                    val data = CombinedData()
                    if (temp is VoCandleStickEntry) {
//                        binding.chart.data = generateData(temp.entryList)
                    }

                    binding.chart.data = data
                }

            }
        }
        binding.chart.description.isEnabled = false
        binding.chart.setDrawGridBackground(false)
        binding.chart.animateX(50)

        val l: Legend = binding.chart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)

        binding.chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
//                TODO("Not yet implemented")
            }

            override fun onNothingSelected() {
//                TODO("Not yet implemented")
            }
        })
        return binding.root
    }


    private fun generateData(entries: List<CandleEntry>): CandleData {
        val d = CandleData()
//        var index = 0
//        while (index < entries.size) {
//            entries.add(CandleEntry(index + 1f, 90f, 70f, 85f, 75f))
//            index += 2
//        }
        val set = CandleDataSet(entries, "혈압")
        set.decreasingColor = Color.rgb(142, 150, 175)
        set.shadowColor = Color.DKGRAY
        set.barSpace = 0.3f
        set.valueTextSize = 10f
        set.setDrawValues(false)
        d.addDataSet(set)
        return d
    }

    companion object {
        fun with(controller: IController, model: PViewModel?): BioCommonCombinedLineBarChartFragment {
            val frag = BioCommonCombinedLineBarChartFragment()
            frag.setCallback(controller, model)
            return frag
        }
    }
}