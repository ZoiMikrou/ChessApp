package com.chessapp.knightpaths.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object CoRoutines {

    fun default(work: suspend (() -> Unit)) = run {
        CoroutineScope(Dispatchers.Default).launch {
            work()
        }
    }

    suspend fun transferToMain(work: suspend (() -> Unit)) = run {
        withContext(Main) {
            work()
        }
    }
}