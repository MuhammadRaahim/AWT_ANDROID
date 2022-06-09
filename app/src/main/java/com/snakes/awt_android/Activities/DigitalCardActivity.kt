package com.snakes.awt_android.Activities

import android.content.Intent
import android.nfc.NfcAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.horizam.idbuddy.nfc_utils.NFCUtil
import com.horizam.skbhub.Utils.Constants
import com.snakes.awt_android.R
import com.snakes.awt_android.Utils.BaseUtils.Companion.showMessage
import com.snakes.awt_android.databinding.ActivityDigitalCardBinding

class DigitalCardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDigitalCardBinding
    private var mNfcAdapter: NfcAdapter? = null
    private var nfcUtil: NFCUtil = NFCUtil()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDigitalCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setNfcWriter()
        setClickListeners()

    }

    private fun setClickListeners() {
        binding.civCloseScreen.setOnClickListener{
            finish()
        }
    }

    private fun startNfcWriting() {
        mNfcAdapter?.let {
            nfcUtil.enableNFCInForeground(it, this, javaClass)
        }
    }

    private fun setNfcWriter() {
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)
    }

    override fun onResume() {
        super.onResume()
        startNfcWriting()
    }

    override fun onPause() {
        super.onPause()
        mNfcAdapter?.let {
            nfcUtil.disableNFCInForeground(it, this)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
            val messageWrittenSuccessfully =
                nfcUtil.createNFCMessage(
                    Constants.awtHomeUrl,
                    intent
                )
            if (messageWrittenSuccessfully) {
                showMessage(
                    binding.root,
                    "AWT DCard created successfully")
            } else {
                showMessage(
                    binding.root,
                    "Failed")
            }
    }

}