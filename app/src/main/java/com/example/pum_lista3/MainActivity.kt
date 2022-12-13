package com.example.pum_lista3

import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
  }

  override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
    val v: View? = currentFocus
    if (v is EditText) {
      val outRect = Rect()
      v.getGlobalVisibleRect(outRect)
      if (!outRect.contains(event!!.rawX.toInt(), event.rawY.toInt())) {
        v.clearFocus()
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
      }
    }
    return super.dispatchTouchEvent(event)
  }
}