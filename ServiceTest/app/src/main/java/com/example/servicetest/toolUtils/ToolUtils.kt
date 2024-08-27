package com.example.servicetest.toolUtils

import android.content.res.Resources
import android.util.TypedValue
import androidx.annotation.InspectableProperty.ValueType

val Float.px
    get() = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics)
