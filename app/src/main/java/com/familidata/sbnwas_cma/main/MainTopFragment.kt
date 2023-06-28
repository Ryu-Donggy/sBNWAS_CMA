package com.familidata.sbnwas_cma.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import com.familidata.base.Log
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.base.model.PViewModel
import com.familidata.sbnwas_cma.base.model.vo.VoUserInfo
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.databinding.FragmentMainTopBinding
import com.familidata.sbnwas_cma.main.mypage.MyPageActivity
import com.familidata.sbnwas_cma.main.notification.NotificationMainActivity

class MainTopFragment : PFragment() {

    private lateinit var binding: FragmentMainTopBinding
    private fun setCallback(controller: IController?, model: PViewModel?) {
        this.controller = controller
        this.model = model
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_top, container, false)
        binding.controller = controller

        model?.let {
            it.getItem().observe(viewLifecycleOwner) { bean ->
                binding.name = (bean as VoUserInfo).name
            }
        }

        setListener()

//
        if (requireActivity() !is MainActivity) {
            binding.ivUserInfo.visibility = View.GONE
            binding.ivNotice.visibility = View.GONE
            binding.sp.visibility = View.GONE
        }
//            binding.ivUserInfo.visibility = View.GONE
//        } else if (requireActivity() is NotificationMainActivity) {
//            binding.ivNotice.visibility = View.GONE
//        }
        return binding.root
    }


    fun setListener() {
        val options = listOf(requireContext().getString(R.string.device_list), requireContext().getString(R.string.setting_monitor))
        binding.options = options
        var listener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, p: Int, id: Long) {
                Log.i(options[p])
                controller?.let {
                    it.spinnerSelectedStr(options[p])
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
        binding.sp.setOnItemSelectedEvenIfUnchangedListener(listener)
    }

    companion object {
        fun with(controller: IController?, model: PViewModel?): MainTopFragment {
            val frag = MainTopFragment()
            frag.setCallback(controller, model)
            return frag
        }
    }
}