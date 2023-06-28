package com.familidata.sbnwas_cma.main.bio

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.PViewModel
import com.familidata.sbnwas_cma.base.model.vo.VoPieEntry
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.databinding.FragmentCommonBioMonitorPieChartBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate

class BioCommonPieChartFragment : PFragment() {

    private lateinit var binding: FragmentCommonBioMonitorPieChartBinding
    internal var list: List<PBean> = ArrayList()

    private fun setCallback(controller: IController, model: PViewModel?) {
        this.controller = controller
        this.model = model
    }

    private fun setCallback(controller: IController, model: PViewModel?, title: String) {
        this.controller = controller
        this.model = model
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_common_bio_monitor_pie_chart, container, false)
        binding.controller = controller
        binding.lifecycleOwner = this

        val mLinearLayoutManager = LinearLayoutManager(activity)
        mLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        model?.let {

            it.getItem().observe(viewLifecycleOwner) { temp ->
                temp?.let {
                    if (temp is VoPieEntry) {
                        binding.chart.data = generateData(temp.entryList)
                    }
                }

            }
        }

        binding.chart.description.isEnabled = false
        binding.chart.setCenterTextSize(8f)
        binding.chart.centerText = generateCenterText()
        binding.chart.animateX(1000)

        // radius of the center hole in percent of maximum radius
        binding.chart.holeRadius = 45f
        binding.chart.transparentCircleRadius = 50f

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


    private fun generateData(entries1: List<PieEntry>): PieData {
        val ds1 = PieDataSet(entries1, requireContext().getString(R.string.sleep_detail))
        ds1.setColors(*ColorTemplate.MATERIAL_COLORS)
        ds1.sliceSpace = 2f
        ds1.valueTextColor = Color.WHITE
        ds1.valueTextSize = 10f
        return PieData(ds1)
    }

    private fun generateCenterText(): SpannableString? {
        val s = SpannableString(getString(R.string.sleep_detail_2))
        s.setSpan(RelativeSizeSpan(2f), 0, 2, 0)
        s.setSpan(ForegroundColorSpan(Color.GRAY), 0, s.length, 0)
        return s
    }

    companion object {
        fun with(controller: IController, model: PViewModel?): BioCommonPieChartFragment {
            val frag = BioCommonPieChartFragment()
            frag.setCallback(controller, model)
            return frag
        }
    }
}