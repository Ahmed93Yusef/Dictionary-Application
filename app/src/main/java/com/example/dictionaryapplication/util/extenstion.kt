package com.example.dictionaryapplication.util

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.transition.Slide
import androidx.transition.TransitionManager


/**
 * Make slide animation on change view visibility
 * @author     Ahmed Yusef
 * @see        <a href="https://stackoverflow.com/a/67856091/16375959">StackOverFlow Answer</a>
 * @return     Unit
 * */
fun View.slideVisibility(visibility: Boolean, duration: Long = 350, gravity: Int = Gravity.BOTTOM) {
    val transition = Slide(gravity)
    transition.apply {
        this.duration = duration
        addTarget(this@slideVisibility)
    }
    TransitionManager.beginDelayedTransition(parent as ViewGroup, transition)
    isVisible = visibility
}
