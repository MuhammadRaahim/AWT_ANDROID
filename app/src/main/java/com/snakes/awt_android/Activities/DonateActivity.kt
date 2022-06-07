package com.snakes.awt_android.Activities

import android.nfc.NfcAdapter
import android.nfc.tech.IsoDep
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import com.github.devnied.emvnfccard.model.EmvCard
import com.github.devnied.emvnfccard.parser.EmvTemplate
import com.github.devnied.emvnfccard.parser.IProvider
import com.snakes.awt_android.Models.Card
import com.snakes.awt_android.R
import com.snakes.awt_android.Utils.BaseUtils.Companion.showMessage
import com.snakes.awt_android.YourProvider

import com.snakes.awt_android.databinding.ActivityDonateBinding
import java.io.IOException

class DonateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDonateBinding
    private lateinit var isoDep: IsoDep
    private lateinit var card: EmvCard


    private val nfcAdapter: NfcAdapter? by lazy {
        NfcAdapter.getDefaultAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonateBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    override fun onResume() {
        super.onResume()
        reFreshNfc()
        readCard()
    }

    private fun readCard() {
        val options = Bundle()
        options.putInt(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY, 500)
        nfcAdapter!!.enableReaderMode(this,
            { tag ->
                isoDep = IsoDep.get(tag)
                try {
                    isoDep.connect()
                    getCardDetails(isoDep)
                } catch (e: IOException) {
                    Toast.makeText(this,e.message, Toast.LENGTH_SHORT).show()
                }
            },NfcAdapter.FLAG_READER_NFC_A or NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, options)
    }


    private fun reFreshNfc(){
        if (nfcAdapter != null && isNfcEnabled()) {
            Log.d("Nfc","Nfc On")
        } else if (nfcAdapter != null && !isNfcEnabled()){
            Log.d("Nfc","Nfc Off")
            showMessage(binding.root, getString(R.string.str_turn_on_nfc))
        } else {
            showMessage(binding.root, getString(R.string.str_no_nfc_support))
        }
    }

    private fun getCardDetails(isoDep: IsoDep) {
        val provider: IProvider = YourProvider(isoDep)

        val config: EmvTemplate.Config = EmvTemplate.Config()
            .setContactLess(true)
            .setReadAllAids(true)
            .setReadTransactions(true)
            .setReadCplc(false)
            .setRemoveDefaultParsers(false)
            .setReadAt(true)

        val parser = EmvTemplate.Builder()
            .setProvider(provider)
            .setConfig(config)
            .build()

        card = parser.readEmvCard()

        var cardData = Card(card.cardNumber,card.expireDate.toString(), card.applications[0].aid,card.applications[0].applicationLabel,
            card.type.toString(),card.applications[0].leftPinTry.toString(),card.atrDescription.toString(),false)
        binding.etCardNumber.setText(cardData.cardNumber)
        binding.etExpire.setText(cardData.expireDate)
        binding.progressLayout.isVisible = false
    }

    override fun onPause() {
        super.onPause()
        reFreshNfc()
        if (nfcAdapter != null) {
            NfcAdapter.getDefaultAdapter(this).disableReaderMode(this)
        }
    }

    private fun isNfcEnabled(): Boolean {
        if (nfcAdapter != null) {
            return try {
                nfcAdapter!!.isEnabled
            } catch (exp: Exception) {
                try {
                    nfcAdapter!!.isEnabled
                } catch (exp: Exception) {
                    false
                }
            }
        }
        return false
    }
}