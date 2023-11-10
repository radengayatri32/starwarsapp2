package com.example.appstarwars

import android.widget.TextView
import androidx.databinding.BindingAdapter


@BindingAdapter ("app:heightWithUnit")

fun setHeightWithUnit(view: TextView, height: String?) {
    if (!height.isNullOrEmpty()) {
        val heightWithUnit = "$height cm"
        view.text = heightWithUnit
    }
}