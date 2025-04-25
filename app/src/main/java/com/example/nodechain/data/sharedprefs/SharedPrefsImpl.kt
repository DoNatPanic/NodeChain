package com.example.nodechain.data.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import com.example.nodechain.domain.model.Node

const val CHAIN = "chain"
const val ROOT_NODE = "root_node"

class SharedPrefsImpl(
    private val context: Context

) : SharedPrefs {

    override fun saveNodeState(json: String) {
        val sharedPrefs = context.getSharedPreferences(CHAIN, Context.MODE_PRIVATE)
        sharedPrefs.edit()
            .putString(ROOT_NODE, json)
            .apply()
    }

    override fun loadNodeState(): String? {
        return context
            .getSharedPreferences(CHAIN, Context.MODE_PRIVATE).getString(ROOT_NODE, null)
    }

}