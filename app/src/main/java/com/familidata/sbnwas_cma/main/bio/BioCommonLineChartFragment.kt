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
import com.familidata.sbnwas_cma.base.model.vo.VoEntry
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.databinding.FragmentCommonBioMonitorLineChartBinding
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ViewPortHandler

class BioCommonLineChartFragment : PFragment() {

    private lateinit var binding: FragmentCommonBioMonitorLineChartBinding
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
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_common_bio_monitor_line_chart, container, false)
        binding.controller = controller
        binding.lifecycleOwner = this

        val mLinearLayoutManager = LinearLayoutManager(activity)
        mLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        model?.let {

            it.getItem().observe(viewLifecycleOwner) { temp ->
                temp?.let {
                    if (temp is VoEntry) {
                        binding.chart.data = generateLineData(temp.entryList, temp.isInt)
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
                Log.i("onValueSelected")
            }

            override fun onNothingSelected() {
                Log.i("onNothingSelected")
            }

        })

//        setData(45, 180f)

        return binding.root
    }


    fun generateLineData(list: List<Entry>, isInt: Boolean): LineData {
//        var entry1: Entry = Entry(1f, 100f)
//        var entry2: Entry = Entry(2f, 150f)
////        var entry3: Entry = Entry(3f, 50f)
//        var list: List<Entry> = list
        val sets = java.util.ArrayList<ILineDataSet>()
        val ds1 = LineDataSet(list, "")
        ds1.lineWidth = 2f

//        Log.i("isInt", isInt)
        if (isInt) {
            val formatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    Log.i("" + value.toInt())
                    return "" + value.toInt()
                }
            }
            binding.chart.xAxis.valueFormatter = formatter
            binding.chart.axisLeft.valueFormatter = formatter
            ds1.valueFormatter = formatter
        } else {
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
        ds1.setDrawCircles(false)
        ds1.color = Color.rgb(255, 184, 64)

        // load DataSets from files in assets folder
        sets.add(ds1)
        val d = LineData(sets)
        return d
    }


    companion object {
        fun with(controller: IController, model: PViewModel?): BioCommonLineChartFragment {
            val frag = BioCommonLineChartFragment()
            frag.setCallback(controller, model)
            return frag
        }
    }
}