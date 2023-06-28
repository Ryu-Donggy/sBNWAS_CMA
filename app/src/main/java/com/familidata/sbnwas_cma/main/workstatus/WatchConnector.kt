package com.familidata.sbnwas_cma.main.workstatus

import android.content.Context
import com.familidata.base.Log
import com.familidata.sbnwas_cma.R
import com.familidata.sbnwas_cma.util.CommonUtil
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.Wearable
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.util.concurrent.ExecutorService

class WatchConnector(var context: Context, var json: JSONObject, var executor: ExecutorService) {
    private val SBNWAS_MESSAGE_PATH = "/sbnwas"

    var connectNode: Node? = null

    inner class checkConnection : Runnable {
        override fun run() {
            if (connectNode == null) {
                try {
                    checkAvailableNode()
                    Thread.sleep(1000)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                Thread.interrupted()
            }
        }
    }

    private fun checkAvailableNode() {
        val nodes = Wearable.getNodeClient(context).connectedNodes
        nodes.addOnSuccessListener { nodes12: List<Node?> ->
            Log.d("MainActivity.TA", nodes12.toString())
            if (nodes12.isEmpty()) {
                Log.i("MainActivity.TAG", "없다")
                CommonUtil.showToast(context, context.getString(R.string.msg_non_connected_to_watch))
            } else {
                for (temp: Node? in nodes12) {
                    temp?.let {
                        if (it.isNearby) {
                            connectNode = it
                            executor.execute(SendMessage(SBNWAS_MESSAGE_PATH, json.toString()))
                        }
                    }
                }
//                val capabilityInfoTask = Wearable.getCapabilityClient(context).getCapability(SBNWAS_CAPABILITY_NAME, CapabilityClient.FILTER_REACHABLE)
//                capabilityInfoTask.addOnSuccessListener { capabilityInfo: CapabilityInfo ->
//                    val nodes1 = capabilityInfo.nodes
//                    if (nodes1.size == 0) {
//
//                        Log.i("MainActivity.TAG", "없다")
//                    } else {
//                        for (node in nodes1) {
//                            if (node.isNearby) {
//                                connectStatus.connectNode = node
//                                connectNode = connectStatus.connectNode
////                                runOnUiThread(Runnable { Toast.makeText(this@MainActivity, "모바일 기기와 연결 되었습니다.", Toast.LENGTH_SHORT).show() })
//                                Log.i("MainActivity.TAG", "연결됨")
//
//                            } else {
//                                Log.i("MainActivity.TAG", "안연결됨")
//                            }
//                        }
//                    }
//                }
            }
        }
    }

    inner class SendMessage internal constructor(var path: String, var message: String) : Runnable {
        override fun run() {
            Log.d("==========>스레드이름", Thread.currentThread().name)
//            val sendMessageTask: Task<Int>? =
            connectNode?.let { Wearable.getMessageClient(context).sendMessage(it.id, path, message.toByteArray(StandardCharsets.UTF_8)) }

        }
    }
}