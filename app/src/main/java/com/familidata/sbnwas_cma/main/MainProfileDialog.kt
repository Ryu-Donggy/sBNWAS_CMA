package com.familidata.sbnwas_cma.main

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.databinding.DialogDiaryBinding

class MainProfileDialog(context: Context, private val okCallback: (String) -> Unit) : Dialog(context) { // 뷰를 띄워야하므로 Dialog 클래스는 context를 인자로 받는다.

    private lateinit var binding: DialogDiaryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 만들어놓은 dialog_profile.xml 뷰를 띄운다.
        binding = DialogDiaryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() = with(binding) {
        setCancelable(true)

        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        profileBtn.setOnClickListener {
            if (profileEt.text.isNullOrBlank()) {
                Toast.makeText(context, context.getString(R.string.msg_set_diary), Toast.LENGTH_SHORT).show()
            } else {
                AlertDialog.Builder(context).setTitle("").setMessage(context.getString(R.string.want_send)).setPositiveButton(context.getString(R.string.confrim)) { _, _ ->
                    okCallback(profileEt.text.toString())
                    dismiss()
                }.setNegativeButton(context.getString(R.string.cancel)) { _, _ -> }.create().show()
            }
        }
    }
}