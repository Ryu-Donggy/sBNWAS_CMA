package com.familidata.sbnwas_cma.util

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.familidata.sbnwas_cma.R


object BindingUtil {


    @BindingAdapter("loadUrl", "imgWidth", "imgHeight", "placeHolder", requireAll = false)
    @JvmStatic
    fun loadUrl(iv: ImageView, url: String?, imgWidth: Int?, imgHeight: Int?, placeHolder: Drawable?) {
        try {
            if (url != null) {
                if (imgWidth != null && imgHeight != null) {
                    Glide.with(iv).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(false).override(imgWidth, imgHeight).transition(DrawableTransitionOptions.withCrossFade()).into(iv)
                } else {
                    Glide.with(iv).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(false).transition(DrawableTransitionOptions.withCrossFade()).into(iv)
                }
            } else {
                if (placeHolder != null) {
                    iv.setImageDrawable(placeHolder)
                } else {
                    iv.setImageResource(android.R.color.transparent)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @BindingAdapter("visible")
    @JvmStatic
    fun setVisibility(v: View, isVisible: Boolean?) {
        v.visibility = if (isVisible == true) View.VISIBLE else View.GONE
    }


    @BindingAdapter("setBackgroundRes")
    @JvmStatic
    fun setBackgroundRes(v: View, res: Int) {
        v.setBackgroundResource(res)
    }

    @BindingAdapter("setSelectedBackGround")
    @JvmStatic
    fun setSelectedBackGround(v: View, isSelected: Boolean) {
        if (isSelected) {
            v.setBackgroundResource(R.drawable.ract_rounded5_e5e6ea)
        } else {
            v.setBackgroundResource(R.drawable.transparent)
        }
    }

    @BindingAdapter("alphaVisible")
    @JvmStatic
    fun setAlphaVisible(v: View, isSelected: Boolean) {
        if (isSelected) {
            v.alpha = 1f
        } else {
            v.alpha = 0.5f
        }
    }
}