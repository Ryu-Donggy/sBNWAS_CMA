package com.familidata.sbnwas_cma.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.databinding.FragmentMainPagerBinding
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.util.CommonUtil
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


class MainPagerFragment : PFragment() {

    lateinit var binding: FragmentMainPagerBinding
    internal var list: List<PBean> = ArrayList()
    private var isThereWorkPlan = false

    private fun setCallback(controller: IController) {
        this.controller = controller
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_pager, container, false)
        isThereWorkPlan = checkWorkPlan()
        binding.controller = controller
        binding.lifecycleOwner = this

        val mLinearLayoutManager = LinearLayoutManager(activity)
        mLinearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        mLinearLayoutManager.scrollToPosition(0)
        val pagerAdapter = ScreenSlidePagerAdapter(requireActivity())
        binding.viewpager2.adapter = pagerAdapter
        binding.viewpager2.orientation = ViewPager2.ORIENTATION_HORIZONTAL // 방향을 가로로
        binding.viewpager2.offscreenPageLimit = 4
        binding.viewpager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                controller?.onPageSelected(binding.viewpager2, position)
            }
        })

        TabLayoutMediator(binding.tabLayout01, binding.viewpager2) { _, _ -> }.attach()
//        var plan = DBManager.withDB().withWorkPlan().getPlanList()
//        if(plan == null || plan.size == 0){
//            binding.viewpager2.isUserInputEnabled = false;
//        }
        return binding.root
    }

    fun setPage(page: String) {
        binding.viewpager2.post {
            binding.viewpager2.setCurrentItem(page.toInt(), true)
        }

    }

//    private val mMainBodyController: MainBodyController by lazy { MainBodyController(requireContext()) }

    private val mMainWorkController: MainWorkController by lazy {
        MainWorkController(requireContext(), progressVisible = {
            if (it == View.VISIBLE) autoCloseProgress()
            binding.clProgress.visibility = it
        })
    }

    private fun checkWorkPlan(): Boolean {
        var plan = DBManager.withDB().withWorkPlan().getUnFinishedWork()
        if (plan == null) {
            plan = DBManager.withDB().withWorkPlan().getTodayPlan()
            if (plan == null) {
                return (false)
            } else {
                return (true)
            }
        } else return (true)
    }

    private val mMainWatchPageController: MainWatchPageController by lazy { MainWatchPageController(requireContext()) }
    private val mMainBioPageController: MainBioPageController by lazy { MainBioPageController(requireContext()) }
    private val mMainCholePageController: MainCholePageController by lazy { MainCholePageController(requireContext()) }
    private val mMainBodyCompostionPageController: MainBodyCompostionPageController by lazy { MainBodyCompostionPageController(requireContext()) }

    private fun autoCloseProgress() {
        GlobalScope.launch {
            launch(Dispatchers.Main) {
                delay(10000)
                if (binding.clProgress.isVisible) {
                    CommonUtil.createNotificationChannel(requireContext(), getString(R.string.msg_watch_non_responding), getString(R.string.msg_check_watch_bluetooth))
                    CommonUtil.showToast(requireContext(), getString(R.string.msg_unable_to_find_watch))
                    binding.clProgress.visibility = View.GONE
                }
            }
        }
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun getItemCount(): Int {
            return if (isThereWorkPlan) 5
            else 4
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> {
                    mMainWatchPageController.asFragCreate()!!
                }

                1 -> {
                    mMainBioPageController.asFragCreate()!!
                }

                2 -> {
                    mMainCholePageController.asFragCreate()!!
                }

                3 -> {
                    mMainBodyCompostionPageController.asFragCreate()!!
                }


                else -> {
                    mMainWorkController.asFragCreate()!!
                }
            }
        }
    }

    companion object {
        fun with(controller: IController): MainPagerFragment {
            val frag = MainPagerFragment()
            frag.setCallback(controller)
            return frag
        }
    }
}