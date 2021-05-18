package com.example.hand_calculator.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Calculator_model (


    var name :String = "",
    var sum :String = "",
    var number :String = "",
    var last_play :String = "",
    var id :Int = 0,
    var idd :Int =0

   ): Parcelable