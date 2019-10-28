package com.example.forecastapplication.local

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

fun <T> Task<T>.toDeferred(): Deferred<T>{
    val deferred = CompletableDeferred<T>()

    this.addOnSuccessListener {value ->
        deferred.complete(value)
    }

    this.addOnFailureListener  {exception ->
        deferred.completeExceptionally(exception)
    }

    return deferred
}


