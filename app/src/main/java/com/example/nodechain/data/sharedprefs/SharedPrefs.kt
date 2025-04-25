package com.example.nodechain.data.sharedprefs

import android.content.SharedPreferences
import com.example.nodechain.domain.model.Node

interface SharedPrefs {
    fun saveNodeState(json: String)
    fun loadNodeState(): String?
}