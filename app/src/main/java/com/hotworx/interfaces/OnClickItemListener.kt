package com.hotworx.interfaces


interface OnClickItemListener {
    fun <T>onItemClick(data: T, type: String)
}