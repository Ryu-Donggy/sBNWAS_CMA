package com.familidata.sbnwas_cma.main.bio

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
import com.familidata.sbnwas_cma.databinding.FragmentBioCommonTopBinding
import com.familidata.sbnwas_cma.databinding.FragmentMainTopBinding
import com.familidata.sbnwas_cma.main.mypage.MyPageActivity
import com.familidata.sbnwas_cma.main.notification.NotificationMainActivity

class BioCommonTopFragment : PFragment() {

    private lateinit var binding: FragmentBioCommonTopBinding
    private var title: String = ""
    private fun setCallback(controller: IController?, title: String) {
        this.controller = controller
        this.title = title
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bio_common_top, container, false)
        binding.controller = controller
        binding.name = title
        return binding.root
    }


    companion object {
        fun with(controller: IController?, title: String): BioCommonTopFragment {
            val frag = BioCommonTopFragment()
            frag.setCallback(controller, title)
            return frag
        }
    }
}