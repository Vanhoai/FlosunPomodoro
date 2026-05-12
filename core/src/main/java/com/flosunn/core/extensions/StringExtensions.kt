package com.flosunn.core.extensions

import android.util.Patterns

fun String.upperFirstCharAndLowerRest(): String {
    if (this.isEmpty()) return this
    return this[0].uppercaseChar() + this.substring(1).lowercase()
}

fun String.isValidEmail(): Boolean {
    return Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

inline fun <reified T : Enum<T>> String?.toEnum(defaultValue: T): T {
    return if (this == null) defaultValue
    else try {
        enumValueOf(this)
    } catch (exception: IllegalArgumentException) {
        exception.printStackTrace()
        defaultValue
    }
}
