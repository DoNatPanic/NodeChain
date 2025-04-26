package com.example.nodechain.data.sharedprefs

interface SharedPrefs {
    fun saveNodeState(json: String)
    fun loadNodeState(): String?
}