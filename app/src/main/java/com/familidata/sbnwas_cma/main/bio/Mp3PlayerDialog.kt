package com.familidata.sbnwas_cma.main.bio

import android.app.Dialog
import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.widget.SeekBar
import androidx.databinding.DataBindingUtil
import com.familidata.base.Log
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.api.ApiService
import com.familidata.sbnwas_cma.databinding.DialogMp3PlayerBinding
import com.familidata.sbnwas_cma.room.entity.BioBas
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException

class Mp3PlayerDialog(private val context: Context, private val bas: BioBas) : Dialog(context) {

    private lateinit var binding: DialogMp3PlayerBinding
    private var mediaPlayer: MediaPlayer? = null
    private var isActive: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.dialog_mp3_player, null, false)
        setContentView(binding.root)
        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    Log.i(progress)
                    mediaPlayer?.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })

        if (bas.DATA_TYPE == "H") {
            binding.tvType.text = context.getString(R.string.hsm_title)
        } else {
            binding.tvType.text = context.getString(R.string.lsm)
        }


        var date = bas.CREATE_DTTM
        if (date != null) {
            if (date.length > 19) date = date.substring(0, 19)
            binding.tvDate.text = date
        }

        binding.tvClose.setOnClickListener {
            onStop()
            dismiss()
        }



        mediaPlayer = MediaPlayer()
        try {
            Log.i(ApiService.URL + bas.FTPPATH)
            mediaPlayer?.apply {
                setAudioAttributes(
                    AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC).setUsage(AudioAttributes.USAGE_MEDIA).build()
                )
            }

            mediaPlayer?.setDataSource(ApiService.URL + bas.FTPPATH)
            mediaPlayer?.prepareAsync()
            mediaPlayer?.setOnPreparedListener { mp ->
                binding.seekBar.max = mp.duration
                mp.start()
                CoroutineScope(context = Dispatchers.Main).launch {
                    while (isActive) {
                        binding.seekBar.progress = mp.currentPosition
                        delay(100)
                    }
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    override fun onStop() {
        super.onStop()
        try {
            mediaPlayer?.stop()
            mediaPlayer?.reset()
            mediaPlayer?.release()
            isActive = false
        } catch (e: Exception) {
        }
    }

    override fun dismiss() {
        onStop()
        super.dismiss()
    }
}