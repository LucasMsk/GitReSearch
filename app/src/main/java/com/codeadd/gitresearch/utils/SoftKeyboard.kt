package com.codeadd.gitresearch.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.search_fragment.view.*

//Use to hide keyboard and loose focus from search edit text
object SoftKeyboard {
    fun hide(activity: Activity, view: View? = null) {
        if(activity.currentFocus != null) {
            val inputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity.currentFocus!!.windowToken,0)
            if(view != null) {
                view.txt_searchBar?.clearFocus()
                view.empty_focusable?.requestFocus()
            }
        }
    }
}