package com.flower.basket.orderflower.api

import javax.net.ssl.X509TrustManager
import javax.security.cert.X509Certificate

class CustomTrustManager: X509TrustManager {
    override fun checkClientTrusted(
        p0: Array<out java.security.cert.X509Certificate>?,
        p1: String?
    ) {
        // Implement your custom logic here if needed
    }

    override fun checkServerTrusted(
        chain: Array<out java.security.cert.X509Certificate>?,
        p1: String?
    ) {
        try {
            for (certificate in chain.orEmpty()) {
                // Print certificate details for debugging
                println("Subject: ${certificate.subjectX500Principal}")
                println("Issuer: ${certificate.issuerX500Principal}")
                println("Serial Number: ${certificate.serialNumber}")
                // ... print other certificate details as needed
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getAcceptedIssuers(): Array<java.security.cert.X509Certificate> {
        return emptyArray()
    }
}