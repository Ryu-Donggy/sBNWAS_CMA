package com.familidata.sbnwas_cma.main.analyze

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.base.model.PViewModel
import com.familidata.sbnwas_cma.base.model.vo.*
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.databinding.FragmentAnalyzeBodyBinding

class AnalyzeBodyFragment : PFragment() {

    private lateinit var binding: FragmentAnalyzeBodyBinding

    private fun setCallback(controller: IController, model: PViewModel?) {
        this.controller = controller
        this.model = model
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_analyze_body, container, false)
        binding.controller = controller
        binding.lifecycleOwner = this

        model?.let {
            it.getItem().observe(viewLifecycleOwner) { temp ->
                if (temp is VoAnalyze) {
                    binding.date = temp.data.create_date
                    binding.desc = temp.data.answer
                }
            }
        }
        return binding.root
    }


    companion object {
        fun with(controller: IController, model: PViewModel?): AnalyzeBodyFragment {
            val frag = AnalyzeBodyFragment()
            frag.setCallback(controller, model)
            return frag
        }
    }
}