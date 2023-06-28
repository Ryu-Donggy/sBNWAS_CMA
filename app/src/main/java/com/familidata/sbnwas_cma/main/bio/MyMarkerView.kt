//package com.familidata.sbnwas_cma.main.bio
//
//import android.annotation.SuppressLint
//import android.content.Context
//import android.widget.TextView
//import com.familidata.sbnwas_cma.R
//import com.github.mikephil.charting.components.MarkerView
//import com.github.mikephil.charting.data.CandleEntry
//import com.github.mikephil.charting.data.Entry
//import com.github.mikephil.charting.highlight.Highlight
//import com.github.mikephil.charting.utils.MPPointF
//import com.github.mikephil.charting.utils.Utils
//
///**
// * Custom implementation of the MarkerView.
// *
// * @author Philipp Jahoda
// */
//@SuppressLint("ViewConstructor")
//class MyMarkerView(context: Context?, layoutResource: Int) : MarkerView(context, layoutResource) {
//    private val tvContent: TextView
//
//    init {
//        tvContent = findViewById(R.id.tvContent)
//    }
//
//    // runs every time the MarkerView is redrawn, can be used to update the
//    // content (user-interface)
//    override fun refreshContent(e: Entry, highlight: Highlight) {
//        if (e is CandleEntry) {
//            tvContent.text = Utils.formatNumber(e.high, 0, true)
//        } else {
//            tvContent.text = Utils.formatNumber(e.y, 0, true)
//        }
//        super.refreshContent(e, highlight)
//    }
//
//    override fun getOffset(): MPPointF {
//        return MPPointF(-(width / 2).toFloat(), -height.toFloat())
//    }
//}



//<?xml version="1.0" encoding="utf-8"?>
//<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
//xmlns:tools="http://schemas.android.com/tools"
//android:layout_width="wrap_content"
//android:layout_height="40dp"
//android:background="@drawable/marker2"
//tools:ignore="Overdraw">
//
//<TextView
//android:id="@+id/tvContent"
//android:layout_width="wrap_content"
//android:layout_height="wrap_content"
//android:layout_centerHorizontal="true"
//android:layout_marginTop="7dp"
//android:layout_marginLeft="5dp"
//android:layout_marginRight="5dp"
//android:text=""
//android:textSize="12sp"
//android:textColor="@android:color/white"
//android:ellipsize="end"
//android:singleLine="true"
//android:textAppearance="?android:attr/textAppearanceSmall" />
//
//</RelativeLayout>
