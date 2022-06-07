package com.snakes.awt_android

import android.nfc.tech.IsoDep
import com.github.devnied.emvnfccard.exception.CommunicationException
import com.github.devnied.emvnfccard.parser.IProvider
import java.io.IOException


class YourProvider(var mTagCom: IsoDep?) : IProvider {

    override fun transceive(pCommand: ByteArray?): ByteArray {
        return try {
            mTagCom!!.transceive(pCommand)
        } catch (e: IOException) {
            throw CommunicationException(e.message)
        }
    }
    
    override fun getAt(): ByteArray {
        return mTagCom!!.historicalBytes;
    }

}
