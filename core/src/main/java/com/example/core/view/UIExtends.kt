package com.example.core.view

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import kotlinx.coroutines.flow.SharedFlow


// Observes this SharedFlow as a LiveData and calls the given action if its latest value is true.
fun <T> SharedFlow<T>.observeTrueEvent(lifecycleOwner: LifecycleOwner, action: () -> Unit) {
    this.asLiveData().observe(lifecycleOwner) {
        if (it is Boolean && it) action()
    }
}

fun <T> LiveData<T>.observeTrueEvent(lifecycleOwner: LifecycleOwner, action: () -> Unit) {
    this.observe(lifecycleOwner) {
        if (it is Boolean && it) action()
    }
}

// Observes a LiveData and calls the given actionTrue or actionFalse depending the latest value.
fun <T> SharedFlow<T>.observeTrueFalseEvent(
    lifecycleOwner: LifecycleOwner,
    actionTrue: () -> Unit,
    actionFalse: () -> Unit
) = asLiveData().observe(lifecycleOwner) {
    if (it is Boolean && it) actionTrue() else actionFalse()
}

// Observes a LiveData and calls the given actionTrue or actionFalse depending the latest value.
fun <T> LiveData<T>.observeTrueFalseEvent(
    lifecycleOwner: LifecycleOwner,
    actionTrue: () -> Unit,
    actionFalse: () -> Unit
) = observe(lifecycleOwner) {
    if (it is Boolean && it) actionTrue() else actionFalse()
}