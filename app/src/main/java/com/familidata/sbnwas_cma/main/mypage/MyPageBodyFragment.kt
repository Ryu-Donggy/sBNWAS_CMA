package com.familidata.sbnwas_cma.main.mypage

import android.content.Context
import android.os.Bundle
import android.util.SparseBooleanArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.PViewModel
import com.familidata.sbnwas_cma.base.model.vo.*
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.databinding.FragmentCommonBioMonitorListBinding
import com.familidata.sbnwas_cma.databinding.FragmentMyPageBodyBinding
import com.familidata.sbnwas_cma.databinding.ItemBioMonitorDataBinding
import com.familidata.sbnwas_cma.databinding.ItemBioMonitorDateBinding

class MyPageBodyFragment : PFragment() {

    private lateinit var binding: FragmentMyPageBodyBinding

    private fun setCallback(controller: IController, model: PViewModel?) {
        this.controller = controller
        this.model = model
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_page_body, container, false)
        binding.controller = controller
        binding.lifecycleOwner = this


        model?.let {
            it.getItem().observe(viewLifecycleOwner) { temp ->
                if (temp is VoMyPage) {
                    binding.tvId.text = temp.data.loginId
                    binding.tvName.text = temp.data.userName
                    binding.tvSex.text = temp.data.gender
                    binding.tvBirTh.text = temp.data.birth
                    binding.tvPhoneNumber.text = temp.data.telNo
                    binding.tvZipCode.text = temp.data.zipCode
                    binding.tvAddr.text = temp.data.address
                    binding.tvEnterDate.text = temp.data.joinDate
                    binding.tvEmpNo.text = temp.data.empId
                    binding.tvHeight.text = temp.data.height
                    binding.tvWeight.text = temp.data.weight
                    binding.tvAge.text = temp.data.age
                }
            }
        }
        return binding.root
    }


    companion object {
        fun with(controller: IController, model: PViewModel?): MyPageBodyFragment {
            val frag = MyPageBodyFragment()
            frag.setCallback(controller, model)
            return frag
        }
    }
}