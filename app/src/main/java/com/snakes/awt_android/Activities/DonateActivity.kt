package com.snakes.awt_android.Activities

import android.nfc.NfcAdapter
import android.nfc.tech.IsoDep
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import com.github.devnied.emvnfccard.model.EmvCard
import com.github.devnied.emvnfccard.parser.EmvTemplate
import com.github.devnied.emvnfccard.parser.IProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.horizam.skbhub.Utils.Constants
import com.snakes.awt_android.Models.Card
import com.snakes.awt_android.Models.Donation
import com.snakes.awt_android.R
import com.snakes.awt_android.Utils.BaseUtils.Companion.hideKeyboard
import com.snakes.awt_android.Utils.BaseUtils.Companion.showMessage
import com.snakes.awt_android.YourProvider

import com.snakes.awt_android.databinding.ActivityDonateBinding
import java.io.IOException

class DonateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDonateBinding
    private lateinit var isoDep: IsoDep
    private lateinit var card: EmvCard
    private lateinit var currentFirebaseUser: FirebaseUser
    private lateinit var donationReference: CollectionReference
    private lateinit var db: FirebaseFirestore
    var serviceName: String? = null


    private val nfcAdapter: NfcAdapter? by lazy {
        NfcAdapter.getDefaultAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        setSpinnerListeners()
        setOnCLickListeners()

    }

    private fun setOnCLickListeners() {
        binding.apply {
            btnAddPaymentMethod.setOnClickListener {
                hideKeyboard()
                if (etName.text.isEmpty() || etName.text.length < 3) {
                    etName.error = getString(R.string.str_enter_valid_name)
                } else if (etCardNumber.text.isEmpty() || etCardNumber.text.length < 16) {
                    etCardNumber.error = "Enter a valid Card Number"
                } else if (etExpire.text.isEmpty()) {
                    etExpire.error = "Enter a valid Expire date"
                } else if (etCvv.text.isEmpty() || etCvv.text.length < 3) {
                    etExpire.error = "Enter a valid CVC"
                } else {
                    hideKeyboard()
                    addDonation()
                }

            }
        }
    }

    private fun addDonation() {
        binding.progressLayout.visibility = View.VISIBLE
        val donation = Donation(serviceName,binding.etAmount.text.toString())
        val ref = donationReference.document("Donation")
        ref.collection(serviceName!!).document().set(donation).addOnSuccessListener {
            binding.progressLayout.visibility = View.GONE
            showMessage(findViewById(android.R.id.content),"Donate successfully")
            finish()
        }.addOnFailureListener{
            binding.progressLayout.visibility = View.GONE
            showMessage(findViewById(android.R.id.content),it.message.toString())
        }
    }
    private fun initViews() {
        currentFirebaseUser = FirebaseAuth.getInstance().currentUser!!
        db = Firebase.firestore
        donationReference = db.collection(Constants.DONATION_DATABASE_ROOT)
        setProfileDropDown()
    }

    private fun setSpinnerListeners() {
        binding.spinnerType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    if (p2 != 0) {
                        when (p2) {
                            1 -> {
                                serviceName = Constants.SERVICE_OBJECT
                            }
                            2 -> {
                                serviceName = Constants.SCHOOLKHANA_OBJECT
                            }
                            else -> {
                                serviceName =Constants.DASTERKHAWAN_OBJECT
                            }
                        }
                    }
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
    }

    private fun setProfileDropDown() {
        ArrayAdapter.createFromResource(
            this,
            R.array.profile_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerType.adapter = adapter
        }
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