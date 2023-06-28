package com.familidata.sbnwas_cma.base.view


import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.familidata.sbnwas_cma.R


abstract class BaseActivity : AppCompatActivity() {

    private val REQUEST_PERMISSIONS = 1
    private val BACK_REQUEST_PERMISSIONS = 2
    private val mFragMan: FragmentManager by lazy { supportFragmentManager }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if(this.javaClass.simpleName)
        requestPermission()
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            return
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), REQUEST_PERMISSIONS)
            permissionDialog(this)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, getString(R.string.msg_permission_comfirmed), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.msg_comfrim_from_setting), Toast.LENGTH_SHORT).show()
                finish()
            }
        } else if (requestCode == BACK_REQUEST_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, getString(R.string.msg_permission_comfirmed), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, getString(R.string.msg_comfrim_from_setting), Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }


    private fun permissionDialog(context: Context) {
        var builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.msg_always_permission_cofrimed))
        builder.setCancelable(false)

        var listener = DialogInterface.OnClickListener { _, p1 ->
            when (p1) {
                DialogInterface.BUTTON_POSITIVE -> backgroundPermission()
            }
        }
        var reject = DialogInterface.OnClickListener { _, p1 ->
            when (p1) {
                DialogInterface.BUTTON_NEGATIVE -> {
                    Toast.makeText(this, getString(R.string.msg_comfrim_permission), Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }
        builder.setPositiveButton(getString(R.string.yes), listener)
        builder.setNegativeButton(getString(R.string.no), reject)
        builder.show()
    }

    private fun backgroundPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION), BACK_REQUEST_PERMISSIONS)
    }

    fun pFragReplace(resId: Int, frag: Fragment?) {
        frag?.let {
            val fragTrans = mFragMan.beginTransaction()
//            fragTrans.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

            fragTrans.replace(resId, frag, "")
            fragTrans.commit()
        }
    }


    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}