package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun <T> CoroutineScope.executeAsync(
    onPreExecute: suspend () -> Unit,
    doInBackground: suspend () -> T,
    onPostExecute: (T) -> Unit
) = launch {
    onPreExecute()
    val result = withContext(Dispatchers.IO) {
        doInBackground()
    }
    onPostExecute(result)
}

fun haveNetworkConnection(context: Context): Boolean {
    var haveConnectedWifi = false
    var haveConnectedMobile = false
    val cm =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val netInfo = cm.allNetworkInfo
    for (ni in netInfo) {
        if (ni.typeName.equals("WIFI", ignoreCase = true))
            if (ni.isConnected) haveConnectedWifi = true
        if (ni.typeName.equals("MOBILE", ignoreCase = true))
            if (ni.isConnected) haveConnectedMobile = true
    }
    return haveConnectedWifi || haveConnectedMobile
}

fun logI(tag: String, msg: String) {
    Log.i(tag, msg)
}

fun isYouTubeUrl(url: String): Boolean {
    return (url.startsWith("https://youtube.com") || url.startsWith("https://youtu.be") || url.startsWith("https://www.youtube.com/"))
}

fun isFacebookUrl(url: String): Boolean {
    return (url.startsWith("https://www.facebook.com/") || url.startsWith("https://fb") || url.startsWith("https://m.me") || url.startsWith("https://business.facebook.com"))
}

fun isWhatsappUrl(url: String): Boolean {
    return (url.startsWith("https://www.whatsapp.com/") || url.startsWith("https://faq.whatsapp.com/") || url.startsWith("https://blog.whatsapp.com/") || url.startsWith(
        "https://wa.me"
    ) || url.contains("whatsapp.com"))
}

fun isTwitterUrl(url: String): Boolean {
    return (url.startsWith("https://x.com/"))
}

fun isInstagramUrl(url: String): Boolean {
    return (url.startsWith("https://www.instagram.com"))
}