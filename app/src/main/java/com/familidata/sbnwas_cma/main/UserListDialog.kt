package com.familidata.sbnwas_cma.main

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.familidata.base.Log
import com.familidata.base.encryption.Aria
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.api.ApiService
import com.familidata.sbnwas_cma.api.req.RequestUserID
import com.familidata.sbnwas_cma.api.res.ResMember
import com.familidata.sbnwas_cma.api.res.ResponseMemberData
import com.familidata.sbnwas_cma.base.model.PBean
import com.familidata.sbnwas_cma.base.model.vo.VoUsers
import com.familidata.sbnwas_cma.databinding.DialogUserListBinding
import com.familidata.sbnwas_cma.databinding.ItemUserBinding
import com.familidata.sbnwas_cma.room.DBManager
import com.familidata.sbnwas_cma.room.PropertyObj
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserListDialog(context: Context?, var callback2Act: (VoUsers) -> Unit) : Dialog(context!!, R.style.Theme_Dialog) {

    internal var list: MutableList<PBean> = ArrayList()
    private var mListAdapter: ListAdapter
    private val binding: DialogUserListBinding

    init {
        window?.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.dialog_user_list, null, false)
        binding.controller = this
        val mLinearLayoutManager = LinearLayoutManager(context)
        mLinearLayoutManager.orientation = LinearLayoutManager.VERTICAL

        binding.rv.layoutManager = mLinearLayoutManager
        binding.rv.setHasFixedSize(true)
        binding.rv.itemAnimator = DefaultItemAnimator()

        mListAdapter = ListAdapter(context!!)
        binding.rv.adapter = mListAdapter


        mListAdapter.insertItem(
            VoUsers(
                userid = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
                userName = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_NAME),
                userEmail = DBManager.withDB().withProperty().getProperty(PropertyObj.LOGIN_ID),
                age = DBManager.withDB().withProperty().getProperty(PropertyObj.AGE),
                gender = DBManager.withDB().withProperty().getProperty(PropertyObj.GENDER),
                height = DBManager.withDB().withProperty().getProperty(PropertyObj.HEIGHT),
                weight = DBManager.withDB().withProperty().getProperty(PropertyObj.WEIGHT),
                telNo = ""
            )
        )

        setContentView(binding.root)
        getMembers {}
    }

    fun onClicking(v: View) {
        if (v.id == R.id.tvNegative) {
            dismiss()
        }
    }

    fun onClicking(v: View, vo: PBean) {
        if (vo is VoUsers) {
            callback2Act(vo)
            dismiss()
        }
    }

    fun getMembers(paramFunc: (Boolean) -> Unit) {
        ApiService.getMembers(
            RequestUserID(
                userId = DBManager.withDB().withProperty().getProperty(PropertyObj.USER_ID),
            )
        ).enqueue(object : Callback<ResponseMemberData> {
            override fun onResponse(call: Call<ResponseMemberData>, response: Response<ResponseMemberData>) {
                try {
                    Log.d(response)
                    Log.d("${response.code()},${response.message()},${response.body()}")
                    if (response.code() == 200) {
                        val rsp = response.body()
                        rsp?.let {
                            it.error?.let { it2 ->
                                Log.i(it2.code, it2.message)
                                paramFunc(false)
                                return
                            }
                            it.datas?.let { it2 ->
                                Log.i("members : " + it2)
                                for (temp: ResMember in it2) {
//                                    mListAdapter.insertItem()
                                    list.add(VoUsers(
                                        userid = temp.userId,
                                        userName = temp.userName,
                                        userEmail = temp.loginId,
                                        age = if (temp.age != "") Aria.withFirst().Decrypt(temp.age) else "",
                                        gender = if (temp.gender != "") Aria.withFirst().Decrypt(temp.gender) else "",
                                        height = if (temp.height != "") Aria.withFirst().Decrypt(temp.height) else "",
                                        weight = if (temp.weight != "") Aria.withFirst().Decrypt(temp.weight) else "",
                                        telNo = if (temp.weight != "") Aria.withFirst().Decrypt(temp.weight) else "",
                                    ))
                                }
                                mListAdapter.notifyDataSetChanged()
                                paramFunc(true)
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.i(e.printStackTrace())
                }
            }

            override fun onFailure(call: Call<ResponseMemberData>, t: Throwable) {
                Log.d("${t.message}")
                paramFunc(false)
            }
        })
    }

    internal inner class ListAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        fun insertItem(vo: PBean) {
            list.add(vo)
            notifyItemChanged(list.size)
        }

        override fun getItemViewType(position: Int): Int {
            return list[position].viewType
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                PBean.TYPE_A -> {
                    val binding = ItemUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                    AViewHolder(binding)
                }
                else -> {
                    val binding = ItemUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
                    AViewHolder(binding)
                }
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            when (holder) {
                is AViewHolder -> {
                    onAItemViewHolder(holder, position)
                }
            }
        }

        private fun onAItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewHolder = holder as AViewHolder
            if (list.isEmpty()) return
            viewHolder.bind(list[position] as VoUsers)
        }


        override fun getItemCount(): Int {
            return list.size
        }

        internal inner class AViewHolder internal constructor(binder: ItemUserBinding) : RecyclerView.ViewHolder(binder.root) {
            private val ibinding: ItemUserBinding = binder
            internal fun bind(vo: VoUsers) {
                ibinding.controller = binding.controller
                ibinding.bean = vo
            }
        }
    }

    interface DialogListener {
        fun onPositiveButtonClick()
        fun onNegativeButtonClick()
    }


}