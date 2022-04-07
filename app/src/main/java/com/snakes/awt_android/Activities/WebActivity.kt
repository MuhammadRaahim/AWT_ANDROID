package com.snakes.awt_android.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.horizam.skbhub.Utils.Constants.Companion.urlWebView
import com.snakes.awt_android.R
import com.snakes.awt_android.databinding.ActivityWebBinding

class WebActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWebBinding
    private lateinit var url: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        setWebView()
    }

    private fun initViews() {
       url = intent.getStringExtra(urlWebView).toString()
    }

    private fun setWebView() {
        binding.webView.loadUrl(url);
    }
}