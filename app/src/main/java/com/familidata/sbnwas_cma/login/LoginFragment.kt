package com.familidata.sbnwas_cma.login

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.familidata.base.Log
import com.familidata.base.encryption.Aria
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.api.req.RequestLoginData
import com.familidata.sbnwas_cma.base.view.IController
import com.familidata.sbnwas_cma.base.view.PFragment
import com.familidata.sbnwas_cma.databinding.FragmentLoginBinding

class LoginFragment : PFragment() {

    private lateinit var binding: FragmentLoginBinding
    private var loginId: String? = null
    private fun setCallback(controller: IController?, loginId: String?) {
        this.controller = controller
        this.loginId = loginId
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.controller = controller
        loginId?.let {
            if (loginId != "") {
                binding.userId = it
                binding.etUserId.inputType = InputType.TYPE_NULL
            }
        }
        return binding.root
    }

    fun checkLogin(): RequestLoginData? {
        if (binding.etUserId.text.trim() == "") {
            Toast.makeText(requireContext(), requireContext().getString(R.string.msg_input_id), Toast.LENGTH_SHORT).show()
            return null
        }
        if (binding.etUserPw.text.trim() == "") {
            Toast.makeText(requireContext(), requireContext().getString(R.string.msg_input_pw), Toast.LENGTH_SHORT).show()
            return null
        }
        Log.i("암호화전 ,", binding.etUserId.text.trim().toString(), binding.etUserPw.text.trim().toString())
        return RequestLoginData(binding.etUserId.text.trim().toString(), Aria.withFirst().Encrypt(binding.etUserPw.text.trim().toString()))
    }


    companion object {
        fun with(controller: IController?, loginId: String?): LoginFragment {
            val frag = LoginFragment()
            frag.setCallback(controller, loginId)
            return frag
        }
    }
}