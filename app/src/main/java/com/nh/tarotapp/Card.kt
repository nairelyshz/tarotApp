package com.nh.tarotapp

import android.graphics.drawable.Drawable

class Card( image: Drawable?, id: Int?, name: String?,description: String?){
    var id: Int? = id
        get() = field
        set(value){
            field = value
        }
    var image: Drawable? = image
        get() = field
        set(value){
            field = value
        }
    var name: String? = name
        get() = field
        set(value){
            field = value
        }

    var description: String? = description
        get() = field
        set(value){
            field = value
        }

}