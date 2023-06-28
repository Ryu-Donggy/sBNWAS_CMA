package com.familidata.sbnwas_cma.main.bio

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.familidata.base.Log
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.PViewModel
import com.familidata.sbnwas_cma.base.model.vo.VoBarEntry
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.databinding.FragmentCommonBioMonitorBarChartBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener

class BioCommonBarChartFragment : PFragment() {

    private lateinit var binding: FragmentCommonBioMonitorBarChartBinding
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_common_bio_monitor_bar_chart, container, false)
        binding.controller = controller
        binding.lifecycleOwner = this

        val mLinearLayoutManager = LinearLayoutManager(activity)
        mLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        model?.let {

            it.getItem().observe(viewLifecycleOwner) { temp ->
                temp?.let {
                    if (temp is VoBarEntry) {
                        binding.chart.data = generateData(temp.entryList, temp.isInt)
                    }
                }

            }
        }

        binding.chart.getDescription().setEnabled(false)
        binding.chart.setDrawGridBackground(false)
        binding.chart.animateX(50)


        val leftAxis: YAxis = binding.chart.getAxisLeft()
//        leftAxis.axisMaximum = 250f
//        leftAxis.axisMinimum = 0f

        binding.chart.getAxisRight().setEnabled(false)

        val xAxis: XAxis = binding.chart.getXAxis()
        xAxis.isEnabled = false
        binding.chart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onValueSelected(e: Entry?, h: Highlight?) {
//
            }

            override fun onNothingSelected() {
//                T
            }

        })

//        setData(45, 180f)

        return binding.root
    }


    fun generateData(list: List<BarEntry>, isInt: Boolean): BarData {
//        var entry1: Entry = Entry(1f, 100f)
//        var entry2: Entry = Entry(2f, 150f)
////        var entry3: Entry = Entry(3f, 50f)
//        var list: List<Entry> = list

        val sets = java.util.ArrayList<IBarDataSet>()
        val ds1 = BarDataSet(list, "")
        Log.i("isInt",isInt)
        if (isInt) {
            val formatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return "" + value.toInt()
                }
            }
            binding.chart.xAxis.valueFormatter = formatter
            binding.chart.axisLeft.valueFormatter = formatter
            ds1.valueFormatter = formatter
        }else {
            val formatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    Log.i("" + String.format("%.1f", value))
                    return "" + String.format("%.1f", value)
                }
            }
            binding.chart.xAxis.valueFormatter = formatter
            binding.chart.axisLeft.valueFormatter = formatter
            ds1.valueFormatter = formatter

        }
        ds1.color = Color.rgb(255, 184, 64)

        // load DataSets from files in assets folder
        sets.add(ds1)
        val d = BarData(sets)
        return d
    }


    companion object {
        fun with(controller: IController, model: PViewModel?): BioCommonBarChartFragment {
            val frag = BioCommonBarChartFragment()
            frag.setCallback(controller, model)
            return frag
        }
    }
}