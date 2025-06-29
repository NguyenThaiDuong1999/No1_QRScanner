package com.scanqr.qrscanner.qrgenerator.base

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.scanqr.qrscanner.qrgenerator.utils.SystemUtilNo1
import com.scanqr.qrscanner.qrgenerator.utils.SystemUtilNo1.setLocale

abstract class BaseNo1Fragment<VB : ViewBinding> : Fragment() {
    var TAG = "BaseFragment"

    lateinit var binding: VB

    private var mIsAttached: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        SystemUtilNo1.setLocale(requireContext())
        super.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(setLocale(context))
        TAG = javaClass.simpleName
        mIsAttached = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getViewBinding()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI(view)
        initView()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupUI(view: View) {
        if (view !is EditText) {
            view.setOnTouchListener { _, _ ->
                hideSoftKeyboard()
                false
            }
        }
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                val innerView = view.getChildAt(i)
                setupUI(innerView)
            }
        }
    }

    private fun hideSoftKeyboard() {
        activity?.currentFocus?.let {
            val inputMethodManager =
                activity?.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    abstract fun getViewBinding(): VB

    abstract fun initView()

    fun showActivity(act: Class<*>, bundle: Bundle?) {
        val intent = Intent(activity, act)
        intent.putExtras(bundle ?: Bundle())
        startActivity(intent)
    }

    override fun onDetach() {
        super.onDetach()
        mIsAttached = false
    }
}