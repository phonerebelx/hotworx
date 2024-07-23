package com.hotworx.helpers

interface DrawableClickListener {
    enum class DrawablePosition {
        TOP, BOTTOM, LEFT, RIGHT
    }

    fun onClick(target: DrawablePosition?)
}