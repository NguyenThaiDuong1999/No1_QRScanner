package com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.utils

import android.app.Activity
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.Window
import android.view.WindowInsetsController
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.google.android.material.card.MaterialCardView
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.google.zxing.BarcodeFormat
import com.google.zxing.BinaryBitmap
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatReader
import com.google.zxing.MultiFormatWriter
import com.google.zxing.RGBLuminanceSource
import com.google.zxing.Result
import com.google.zxing.common.BitMatrix
import com.google.zxing.common.HybridBinarizer
import com.google.zxing.qrcode.QRCodeWriter
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.BuildConfig
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.R
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.feature.main.create.widget.AppWidgetPinnedReceiverNo1
import com.tkhapp.qrcode.scanqr.barcodescanner.qrscanner.qrgenerator.helper.SharePrefHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun AppCompatActivity.launchActivity(
    option: Bundle,
    clazz: Class<*>
) {
    option.putString("last_activity", clazz.name)
    val intent = Intent(this, clazz)
    intent.putExtra("data_bundle", option)
    startActivity(intent)
}
//start activity
fun AppCompatActivity.launchActivity(
    clazz: Class<*>
) {
    val intent = Intent(this, clazz)
    val option = Bundle()
    option.putString("last_activity", clazz.name)
    intent.putExtra("data_bundle", option)
    startActivity(intent)
}
fun scaleBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
    // Calculate the scaling factor
    val width = bitmap.width
    val height = bitmap.height

    val scaleFactor = Math.min(maxWidth.toFloat() / width, maxHeight.toFloat() / height)

    // Create a new scaled bitmap
    val scaledBitmap = Bitmap.createScaledBitmap(bitmap, (width * scaleFactor).toInt(), (height * scaleFactor).toInt(), true)

    return scaledBitmap
}

fun getRoundedCornerBitmap(bitmap: Bitmap, radius: Float): Bitmap {
    val width = bitmap.width
    val height = bitmap.height

    // Create a new bitmap with the same size as the original bitmap
    val output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

    // Create a canvas to draw on the new bitmap
    val canvas = Canvas(output)
    val paint = Paint()

    // Anti-aliasing for smooth corners
    paint.isAntiAlias = true
    paint.shader = android.graphics.BitmapShader(bitmap, android.graphics.Shader.TileMode.CLAMP, android.graphics.Shader.TileMode.CLAMP)

    // Define the rounded rectangle
    val rect = RectF(0f, 0f, width.toFloat(), height.toFloat())
    canvas.drawRoundRect(rect, radius, radius, paint)

    return output
}

fun View.getBitmapFromView(): Bitmap {
    // Tạo một Bitmap mới có kích thước bằng với kích thước của CardView
    val bitmap = Bitmap.createBitmap(this.width, this.height, Bitmap.Config.ARGB_8888)

    // Tạo Canvas để vẽ lên Bitmap
    val canvas = Canvas(bitmap)

    // Vẽ nền của CardView (bạn có thể tùy chỉnh màu nền nếu cần)
    canvas.drawColor(Color.TRANSPARENT)

    // Tạo một Paint để vẽ bo góc
    val paint = Paint()
    paint.isAntiAlias = true
    paint.color = Color.WHITE // Màu nền của CardView (thường là màu trắng)

    // Tạo một RectF cho vùng bo góc
    val rectF = RectF(0f, 0f, this.width.toFloat(), this.height.toFloat())
    val radius = 16f // Chỉnh sửa bán kính bo góc theo yêu cầu của bạn

    // Vẽ bo góc cho vùng RectF
    canvas.drawRoundRect(rectF, radius, radius, paint)

    // Vẽ nội dung CardView vào Canvas
    this.draw(canvas)

    return bitmap
}

fun Activity.changeStatusBarColor(color: Int, lightStatusBar: Boolean = false) {
    this.window?.statusBarColor = resources.getColor(color, theme)
    if (lightStatusBar)
        this.lightStatusBar()

}

fun Activity.lightStatusBar() {
    val decorView: View? = this.window?.decorView
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val wic = decorView?.windowInsetsController
        wic?.setSystemBarsAppearance(
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        )
    } else
        decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
}

fun View.tap(action: (view: View?) -> Unit) {
    setOnClickListener(object : TapListenerNo1() {
        override fun onTap(v: View?) {
            action(v)
        }
    })
}

fun ViewGroup.tap(action: (view: View?) -> Unit) {
    setOnClickListener(object : TapListenerNo1() {
        override fun onTap(v: View?) {
            action(v)
        }
    })
}

fun isValidName(name: String): Boolean {
    val regex = "^[\\p{L} .'-]+$".toRegex()
    return name.isNotEmpty() && regex.matches(name)
}

fun Context.getCurrentHHMM(): String {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val currentDate = Date()
    return timeFormat.format(currentDate)
}

fun Context.getCurrentDate(): String {
    val dateFormat = SimpleDateFormat("dd/MM/yy", Locale.getDefault())
    val currentDate = Date()
    return dateFormat.format(currentDate)
}

fun generateQRCode(content: String, width: Int, height: Int): Bitmap? {
    val qrCodeWriter = QRCodeWriter()
    val hints = mapOf(
        EncodeHintType.CHARACTER_SET to "UTF-8"
    )
    return try {
        val bitmapMatrix: BitMatrix =
            qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints)
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitmapMatrix[x, y]) Color.BLACK else Color.TRANSPARENT)
            }
        }
        bitmap
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun generateGeoQRCode(content: String, width: Int, height: Int): Bitmap? {
    try {
        /*val bitMatrix: BitMatrix = MultiFormatWriter().encode(
            content,
            BarcodeFormat.QR_CODE,
            width,
            height
        )
        val barcodeEncoder = BarcodeEncoder()
        return barcodeEncoder.createBitmap(bitMatrix)*/
        val hints = mapOf(
            EncodeHintType.CHARACTER_SET to "UTF-8"
        )
        val bitMatrix: BitMatrix = MultiFormatWriter().encode(
            content,
            BarcodeFormat.QR_CODE,
            width,
            height,
            hints
        )

        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.TRANSPARENT)
            }
        }
        return bitmap
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}

fun generate128Barcode(content: String, width: Int = 600, height: Int = 300): Bitmap? {
    return try {
        val hints = mapOf(
            EncodeHintType.CHARACTER_SET to "UTF-8"
        )
        val bitMatrix: BitMatrix = MultiFormatWriter().encode(content, BarcodeFormat.CODE_128, width, height, hints)
        val barcodeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        for (x in 0 until width) {
            for (y in 0 until height) {
                barcodeBitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.TRANSPARENT)
            }
        }

        val textSize = 40f
        val padding = 10
        val combinedBitmap = Bitmap.createBitmap(width, height + textSize.toInt() + padding, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(combinedBitmap)

        // Nền trong suốt thay vì Color.WHITE
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)

        canvas.drawBitmap(barcodeBitmap, 0f, 0f, null)

        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.color = Color.BLACK
        paint.textSize = textSize
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textAlign = Paint.Align.CENTER

        // Vị trí của mã số bên dưới hình ảnh mã vạch
        val xPos = width / 2f
        val yPos = height + textSize

        // Vẽ chuỗi văn bản (barcodeText) lên bitmap
        canvas.drawText(content, xPos, yPos, paint)

        combinedBitmap
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun decodeQRCodeFromBitmap(bitmap: Bitmap): Result? {
    val intArray = IntArray(bitmap.width * bitmap.height)
    bitmap.getPixels(intArray, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

    val source = RGBLuminanceSource(bitmap.width, bitmap.height, intArray)
    val binaryBitmap = BinaryBitmap(HybridBinarizer(source))

    return try {
        val reader = MultiFormatReader()
        reader.decode(binaryBitmap)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun scanCodeFromBitmap(bitmap: Bitmap): Result? {
    val intArray = IntArray(bitmap.width * bitmap.height)
    bitmap.getPixels(intArray, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

    val source = RGBLuminanceSource(bitmap.width, bitmap.height, intArray)
    val binaryBitmap = BinaryBitmap(HybridBinarizer(source))

    return try {
        val reader = MultiFormatReader()
        reader.decode(binaryBitmap)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun detectCode(bitmap: Bitmap): String {
    val result = scanCodeFromBitmap(bitmap)

    return if (result != null) {
        when (result.barcodeFormat) {
            BarcodeFormat.QR_CODE -> "QR Code"
            BarcodeFormat.CODE_128 -> "1D Barcode"
            BarcodeFormat.CODE_39 -> "1D Barcode"
            BarcodeFormat.EAN_8 -> "1D Barcode"
            BarcodeFormat.EAN_13 -> "1D Barcode"
            BarcodeFormat.UPC_A -> "1D Barcode"
            BarcodeFormat.UPC_E -> "1D Barcode"
            BarcodeFormat.ITF -> "1D Barcode"
            BarcodeFormat.RSS_14 -> "1D Barcode"
            BarcodeFormat.RSS_EXPANDED -> "1D Barcode"
            else -> "Unknown Format"
        }
    } else {
        "No barcode or QR code found"
    }
}

fun Context.shareCode(path: String) {
    try {
        val uri = FileProvider.getUriForFile(
            applicationContext,
            BuildConfig.APPLICATION_ID + ".provider",
            File(path)
        )
        val intent = Intent(Intent.ACTION_SEND)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.type = "image/png"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        launchActivityIntent(intent)
    } catch (e: Exception) {
        Log.i("share", e.toString())
    }
}

fun Activity.openWebSearch(keyWord: String) {
    val query = URLEncoder.encode(keyWord, "utf-8")
    val url = "http://www.google.com/search?q=$query"
    val intent = Intent(Intent.ACTION_VIEW)
    intent.data = Uri.parse(url)
    launchActivityIntent(intent)
}

fun Activity.openUrl(url: String) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    launchActivityIntent(browserIntent)
}

fun Context.launchActivityIntent(intent: Intent) {
    try {
        startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, "No valid app found", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
    }
}

fun Activity.openWifiSetting() {
    val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
    launchActivityIntent(intent)
}

fun isLGDevice(): Boolean {
    val manufacturer = Build.MANUFACTURER
    val brand = Build.BRAND
    val model = Build.MODEL
    val product = Build.PRODUCT

    return manufacturer.contains("LG", ignoreCase = true) ||
            brand.contains("LG", ignoreCase = true) ||
            model.contains("LG", ignoreCase = true) ||
            product.contains("lg", ignoreCase = true)
}

fun Activity.toast(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun Activity.shareText(msg: String) {
    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, msg)
        type = "text/explain"
    }
    val shareIntent = Intent.createChooser(intent, null)
    launchActivityIntent(shareIntent)
}

fun Activity.sendEmail(address: String, subject: String, body: String) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.setDataAndType(Uri.parse(address), "message/rfc822")
    intent.setPackage("com.google.android.gm")
    if (intent.resolveActivity(packageManager) != null) {
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(address))
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        intent.putExtra(Intent.EXTRA_TEXT, body)
        launchActivityIntent(intent)
    } else {
        Log.i("gmail", "not installed")
    }
}

fun Activity.sendSMS(phoneNumber: String, message: String) {
    val uri = Uri.parse("smsto:$phoneNumber")
    val intent = Intent(Intent.ACTION_SENDTO, uri)
    intent.putExtra("sms_body", message)
    try {
        launchActivityIntent(intent)
    } catch (e: Exception) {
        toast(getString(R.string.cant_open_message_app))
    }
}

fun Activity.callNow(phoneNumber: String) {
    val uri = Uri.parse("tel:$phoneNumber")
    val intent = Intent(Intent.ACTION_DIAL, uri)
    try {
        launchActivityIntent(intent)
    } catch (e: Exception) {
        toast(getString(R.string.cant_open_dial_app))
    }
}

fun Activity.openMap(lat: String, lng: String) {
    val uri = Uri.parse("geo:$lat,$lng")
    val intent = Intent(Intent.ACTION_VIEW, uri)
    intent.setPackage("com.google.android.apps.maps")
    try {
        launchActivityIntent(intent)
    } catch (e: Exception) {
        toast(getString(R.string.cant_open_map))
    }
}

fun Activity.startSaveBitmapAsImageToCache(bitmap: Bitmap?, timeDone: String, onSuccess: (String) -> Unit, onSaved: (String) -> Unit) {
    saveBitmapAsImageToCache(bitmap!!, timeDone,
        onSuccess = { path ->
//            runOnUiThread {
//                Toast.makeText(
//                    this,
//                    "${getString(R.string.image_was_saved)}.${getString(R.string.file_path)}: $path",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
            onSuccess.invoke(path)
        },
        onSaved = { path ->
            onSaved.invoke(path)
        })
}

fun Activity.startSaveBitmapAsImageToStorage(bitmap: Bitmap?, timeDone: String, onSuccess: (String) -> Unit, onSaved: (String) -> Unit) {
    saveBitmapAsImageToStorage(bitmap!!, timeDone,
        onSuccess = { path ->
            runOnUiThread {
                Toast.makeText(
                    this,
                    "${getString(R.string.image_was_saved)}.${getString(R.string.file_path)}: $path",
                    Toast.LENGTH_SHORT
                ).show()
            }
            onSuccess.invoke(path)
        },
        onSaved = { path ->
            Log.i("kkkk", path)
            onSaved.invoke(path)
        })
}

fun Context.saveImagePathWithId(id: Int, path: String) {
    SharePrefHelper(this).saveString("widget_image_path_$id", path)
}

fun Context.getImagePathWithId(id: Int): String {
    return SharePrefHelper(this).getString("widget_image_path_$id").toString()
}

fun Activity.shareCodeIfSavedToCache(timeDone: String): String {
    val directory = File(cacheDir, "scan_qr")
    shareCode(
        "$directory/$timeDone.png"
    )
    return "$directory/$timeDone.png"
}

fun Activity.shareCodeIfSavedToStorage(timeDone: String): String {
    val directory = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        "scan_qr"
    )
    shareCode(
        "$directory/$timeDone.png"
    )
    return "$directory/$timeDone.png"
}


fun Context.saveBitmapAsImageToCache(
    bitmap: Bitmap,
    name: String,
    onSuccess: (String) -> Unit,
    onSaved: (String) -> Unit,
) {
    CoroutineScope(Dispatchers.IO).launch {
        val directory = File(cacheDir, "scan_qr")
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val file = File(directory, "$name.png")
        if (!file.exists()) {
            var fileOutputStream: FileOutputStream? = null
            try {
                fileOutputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                fileOutputStream.flush()
                onSuccess.invoke(file.absolutePath)
                Log.i("saved_success", "on")
            } catch (e: Exception) {
                Log.i("save_bitmap", e.toString())
            } finally {
                fileOutputStream?.close()
            }
        } else {
            Log.i("saved", "on")
            onSaved.invoke(file.absolutePath)
        }
    }
}

fun Context.saveBitmapAsImageToStorage(
    bitmap: Bitmap,
    name: String,
    onSuccess: (String) -> Unit,
    onSaved: (String) -> Unit,
) {
    CoroutineScope(Dispatchers.IO).launch {
        val directory = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "scan_qr"
        )
        if (!directory.exists()) {
            directory.mkdirs()
            Log.i("nam", directory.exists().toString())
        }
        val file = File(directory, "$name.png")
        //if (!file.exists()) {
            var fileOutputStream: FileOutputStream? = null
            try {
                fileOutputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
                fileOutputStream.flush()
                onSuccess.invoke(file.absolutePath)
                Log.i("saved_success", "on")
            } catch (e: Exception) {
                Log.i("save_bitmap", e.toString())
            } finally {
                fileOutputStream?.close()
            }
//        } else {
//            Log.i("saved", "on")
//            onSaved.invoke("${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)}/scan_qr/$name.png")
//        }
    }
}


fun EditText.checkOnEditTextChange(onChange: (Int) -> Unit) {
    addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            onChange.invoke(p0!!.length)
        }

        override fun afterTextChanged(p0: Editable?) {
        }
    })
}

fun detectCodeInImageWithMLKit(bitmap: Bitmap, onBarcodeDetected: (List<Barcode>) -> Unit) {
    val image = InputImage.fromBitmap(bitmap, 0)

    val options = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE,
            Barcode.FORMAT_CODE_128,
            Barcode.FORMAT_CODE_39,
            Barcode.FORMAT_EAN_8,
            Barcode.FORMAT_EAN_13,
            Barcode.FORMAT_UPC_A,
            Barcode.FORMAT_UPC_E,
            Barcode.FORMAT_ITF
        )
        .build()

    val scanner = BarcodeScanning.getClient(options)

    scanner.process(image)
        .addOnSuccessListener { barcodes ->
            onBarcodeDetected(barcodes)
        }
        .addOnFailureListener {
            it.printStackTrace()
        }
}

fun clearCacheDirectory(context: Context, directoryName: String) {
    try {
        val cacheDir = File(context.cacheDir, directoryName)
        if (cacheDir.exists() && cacheDir.isDirectory) {
            val files = cacheDir.listFiles()
            files?.forEach { file ->
                if (file.isDirectory) {
                    deleteDir(file)
                } else {
                    file.delete()
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun deleteDir(dir: File): Boolean {
    if (dir.isDirectory) {
        val children = dir.list()
        if (children != null) {
            for (child in children) {
                val success = deleteDir(File(dir, child))
                if (!success) {
                    return false
                }
            }
        }
    }
    return dir.delete()
}

fun Context.copyText(label: String, content: String) {
    Log.i("mm", content)
    val clipboardManager: ClipboardManager =
        getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip: ClipData = ClipData.newPlainText(label, content)
    clipboardManager.setPrimaryClip(clip)
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        Toast.makeText(this, getString(R.string.copy_to_clipboard), Toast.LENGTH_SHORT).show()
    }
}

fun Context.showError(tvEmpty: TextView, cv: MaterialCardView, errorMsg: String) {
    tvEmpty.visible()
    tvEmpty.text = errorMsg
    cv.strokeColor = ContextCompat.getColor(this, R.color.red_create)
}

fun Context.setOnFocus(tvEmpty: TextView, cv: MaterialCardView) {
    tvEmpty.gone()
    cv.strokeColor = ContextCompat.getColor(this, R.color.white)
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.invisible() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}

fun Window.setKeyboardVisibilityListener(onKeyboardVisibilityChanged: (Boolean) -> Unit) {
    val rootView = this.decorView.rootView
    val rect = Rect()

    rootView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        private var lastKeyboardVisible = false

        override fun onGlobalLayout() {
            rootView.getWindowVisibleDisplayFrame(rect)
            val screenHeight = rootView.height
            val keypadHeight = screenHeight - rect.bottom

            val isKeyboardVisible = keypadHeight > screenHeight * 0.15 // 15% chiều cao màn hình

            if (isKeyboardVisible != lastKeyboardVisible) {
                onKeyboardVisibilityChanged(isKeyboardVisible)
            }

            lastKeyboardVisible = isKeyboardVisible
        }
    })
}

// app widget
fun AppWidgetProviderInfo.pin(context: Context) {
    val callback = PendingIntent.getBroadcast(
        context,
        0,
        Intent(context, AppWidgetPinnedReceiverNo1::class.java),
        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
    )
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        AppWidgetManager.getInstance(context).requestPinAppWidget(provider, null, callback)
    }
}

fun deleteImageFromCache(context: Context, fileName: String) {
    val cacheDir = context.cacheDir

    val fileToDelete = File(cacheDir, fileName)

    if (fileToDelete.exists()) {
        val deleted = fileToDelete.delete()
        if (deleted) {
            Log.i("delete", "File đã được xóa thành công.")
        } else {
            Log.i("delete", "Không thể xóa file.")
        }
    } else {
        Log.i("delete", "File không tồn tại.")
    }
}

val Float.toPx get() = this * Resources.getSystem().displayMetrics.density
