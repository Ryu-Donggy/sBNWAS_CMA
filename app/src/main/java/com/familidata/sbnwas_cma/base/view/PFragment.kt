package com.familidata.sbnwas_cma.base.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.familidata.sbnwas_cma.base.model.PViewModel
import com.familidata.sbnwas_cma.rx.DisposeBag
import com.familidata.sbnwas_cma.rx.RxBusObj

open class PFragment : Fragment() {
    var controller: IController? = null
    var model: PViewModel? = null

    val disposeBag = DisposeBag()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        controller?.setBusStation()
    }


    override fun onDestroy() {
        super.onDestroy()
        disposeBag.dispose()
    }

}