package com.familidata.sbnwas_cma.main.work

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.base.model.PViewModel
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.databinding.FragmentWorkPageListBinding
import com.familidata.sbnwas_cma.main.MainBodyController
import com.familidata.sbnwas_cma.main.MainWorkController

class WorkPageFragment : PFragment() {

    private lateinit var binding: FragmentWorkPageListBinding
    private fun setCallback(controller: IController) {
        this.controller = controller
    }

    private val mWorkPlanController: WorkPlanController by lazy { WorkPlanController(requireContext()) }
    private val mWorkHistoryController: WorkHistoryController by lazy { WorkHistoryController(requireContext()) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_work_page_list, container, false)
        binding.controller = controller
        binding.lifecycleOwner = this

        val mLinearLayoutManager = LinearLayoutManager(activity)
        mLinearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        mLinearLayoutManager.scrollToPosition(0)
        val pagerAdapter = ScreenSlidePagerAdapter(requireActivity())
        binding.viewpager2.adapter = pagerAdapter
        binding.viewpager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        binding.viewpager2.offscreenPageLimit = 2

        binding.viewpager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                controller?.onPageSelected(binding.viewpager2, position)
                setPage(position)
            }
        })
        return binding.root
    }

    fun setPage(page: Int) {
        if (page == 0) {
            binding.viewpager2.currentItem = 0
            binding.tvPlanSelected.visibility = View.VISIBLE
            binding.vPlanSelected.visibility = View.VISIBLE
            binding.tvPlanNonSelected.visibility = View.GONE
            binding.tvHistorySelected.visibility = View.GONE
            binding.tvHistoryNonSelected.visibility = View.VISIBLE
            binding.vHistorySelected.visibility = View.GONE

        } else {
            binding.viewpager2.currentItem = 1
            binding.tvPlanSelected.visibility = View.GONE
            binding.vPlanSelected.visibility = View.GONE
            binding.tvPlanNonSelected.visibility = View.VISIBLE
            binding.tvHistorySelected.visibility = View.VISIBLE
            binding.tvHistoryNonSelected.visibility = View.GONE
            binding.vHistorySelected.visibility = View.VISIBLE
        }

    }


    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = 2
        override fun createFragment(position: Int): Fragment {
            return if (position == 0) {
                mWorkPlanController.asFragCreate()!!
            } else {
                mWorkHistoryController.asFragCreate()!!
            }
        }
    }

    companion object {
        fun with(controller: IController): WorkPageFragment {
            val frag = WorkPageFragment()
            frag.setCallback(controller)
            return frag
        }
    }
}