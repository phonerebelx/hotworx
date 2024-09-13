package com.passio.modulepassio.interfaces


interface OnClickItemListener {
    fun <T>onItemClick(data: T, type: String)
}