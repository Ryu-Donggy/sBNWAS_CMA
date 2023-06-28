package com.familidata.sbnwas_cma.main

import android.content.Context
import android.os.Bundle
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
import com.familidata.sbnwas_cma.databinding.*
import com.familidata.sbnwas_cma.rx.RxBusObj
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainWatchPageFragment : PFragment() {

    private lateinit var binding: FragmentMainWatchPageBinding

    private fun setCallback(controller: IController?, model: PViewModel?) {
        this.controller = controller
        this.model = model
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_watch_page, container, false)
        binding.controller = controller
        model?.let {
            it.getList()
        }

        binding.ecgDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd (E)"))

        return binding.root
    }

    fun hideWatch() {
//        binding.clWatch.visibility = View.GONE
    }

    fun setWatchBind(vo: PBean) {
        when (vo) {
            is VoBioBlp -> {
                binding.watchBlp = vo
            }

            is VoBioHtr -> {
                binding.watchHrt = vo
            }

            is VoBioOxs -> {
                binding.watchOxs = vo
            }

            is VoBioEcgHeader -> {
                binding.ecgDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd (E)"))
                binding.ivEcg.visibility = View.VISIBLE
                binding.clWatchEcg.visibility = View.VISIBLE
            }

            is VoBioSleepHeader -> {
                binding.watchSleep = vo
                binding.clWatchSleep.visibility = View.VISIBLE
                binding.tvSt.visibility = View.VISIBLE
                binding.tvSm.visibility = View.VISIBLE
            }

            is VoBioCalorie -> {
                binding.watchCalorie = vo
            }

            is VoBioStep -> {
                binding.watchStep = vo
            }

            is VoBioDistance -> {
                binding.watchDistance = vo
            }

            is VoBioActivity -> {
                binding.watchActivity = vo
            }
        }
    }

    fun setBdy(vo1: VoBioBdy?, vo2: VoBioBdy?, vo3: VoBioBdy?, vo4: VoBioBdy?, vo5: VoBioBdy?, vo6: VoBioBdy?) {
        binding.clAllBMI.visibility = View.VISIBLE
        vo1?.let {
            binding.bmiA = vo1
        }
        vo2?.let {
            binding.bmiB = vo2
        }
        vo3?.let {
            binding.bmiC = vo3
        }
        vo4?.let {
            binding.bmiD = vo4
        }
        vo5?.let {
            binding.bmiF = vo5
        }
        vo6?.let {
            binding.bmiG = vo6
        }
    }

    fun hideWatchData(busKey: String) {
        when (busKey) {
            RxBusObj.BIO_WATCH_SLEEP -> {
//                binding.clWatchSleep.visibility = View.GONE
                binding.tvSt.visibility = View.GONE
                binding.tvSm.visibility = View.GONE
            }
//            RxBusObj.BIO_WATCH_ECG -> {
//                binding.clWatchEcg.visibility = View.GONE
//            }
//            RxBusObj.BIO_WATCH_BLP -> {
//                binding.clWatchBlp.visibility = View.GONE
//            }
//            RxBusObj.BIO_WATCH_HTR -> {
//                binding.clWatchHtr.visibility = View.GONE
//            }
//            RxBusObj.BIO_WATCH_OXY -> {
//                binding.clWatchOxs.visibility = View.GONE
//            }
        }
    }


    companion object {
        fun with(controller: IController?, model: PViewModel?): MainWatchPageFragment {
            val frag = MainWatchPageFragment()
            frag.setCallback(controller, model)
            return frag
        }
    }
}