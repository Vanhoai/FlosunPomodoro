package com.flosunn.core.extensions

fun String.upperFirstCharAndLowerRest(): String {
    if (this.isEmpty()) return this
    return this[0].uppercaseChar() + this.substring(1).lowercase()
}