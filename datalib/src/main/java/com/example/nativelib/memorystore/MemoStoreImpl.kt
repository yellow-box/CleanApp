package com.example.nativelib.memorystore

import android.content.Context
import android.os.Looper
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

import androidx.datastore.preferences.preferencesDataStore
import com.example.domain.ApiService
import com.example.domain.device.IGlobalContextProvider
import com.example.domain.memostore.InMemoDataCallback

import com.example.domain.memostore.InMemoStore
import com.example.domain.socket.Executor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MemoStoreImpl : InMemoStore {
    private val Context.myDataStore by preferencesDataStore("clean_app_store")
    private val c = ApiService[IGlobalContextProvider::class.java] as Context
    override fun initSetting() {

    }

    override fun save(key: String, value: String) {
        CoroutineScope(Dispatchers.IO).launch {
            c.myDataStore.edit {
                it[stringPreferencesKey(key)] = value
            }
        }
    }

    override fun save(key: String, value: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            c.myDataStore.edit {
                it[intPreferencesKey(key)] = value
            }
        }
    }

    override fun loadString(key: String, callback: InMemoDataCallback<String?>) {
        val isInMain = Looper.myLooper() == Looper.getMainLooper()
        CoroutineScope(Dispatchers.IO).launch {
            c.myDataStore.edit {
                val s = it[stringPreferencesKey(key)]
                val executor = ApiService[Executor::class.java]
                if (isInMain) {
                    executor.runInMain({ callback.onLoadSuccess(s) }, 0)
                } else {
                    executor.runInChild {
                        callback.onLoadSuccess(s)
                    }
                }
            }
        }
    }

    override fun loadInt(key: String, callback: InMemoDataCallback<Int?>) {
        val isInMain = Looper.myLooper() == Looper.getMainLooper()
        CoroutineScope(Dispatchers.IO).launch {
            c.myDataStore.edit {
                val s = it[intPreferencesKey(key)]
                val executor = ApiService[Executor::class.java]
                if (isInMain) {
                    executor.runInMain({ callback.onLoadSuccess(s) }, 0)
                } else {
                    executor.runInChild {
                        callback.onLoadSuccess(s)
                    }
                }
            }
        }
    }

}

