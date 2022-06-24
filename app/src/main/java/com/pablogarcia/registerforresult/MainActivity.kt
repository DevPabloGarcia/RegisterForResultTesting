package com.pablogarcia.registerforresult

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.net.toFile
import java.io.File

class MainActivity : AppCompatActivity() {

    private var button: Button? = null
    private var image: ImageView? = null

    private val capturedImage by lazy {
        Uri.fromFile(File(applicationContext.cacheDir, "imageName.jpg"))
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { }

    private val getContent =
        registerForActivityResult(SelectImageContract()) { result ->
            if (result) {
                image?.setImageURI(capturedImage)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindViews()
        setupButton()
        requestPermissionLauncher.launch(Manifest.permission.CAMERA)
    }

    private fun bindViews() {
        button = findViewById(R.id.button_camera)
        image = findViewById(R.id.image_view)
    }

    private fun setupButton() {
        button?.setOnClickListener {
            getContent.launch(null)
        }
    }

    inner class SelectImageContract: ActivityResultContract<Void, Boolean>() {

        private val IMAGE_TYPE = "image/*"

        override fun createIntent(context: Context, input: Void?): Intent {
            val imageUri = FileProvider.getUriForFile(
                context,
                context.applicationContext.applicationInfo.packageName,
                capturedImage.toFile()
            )
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                .putExtra(MediaStore.EXTRA_OUTPUT, imageUri)

            val contentIntent = Intent(Intent.ACTION_GET_CONTENT)
                .addCategory(Intent.CATEGORY_OPENABLE)
                .setType(IMAGE_TYPE)

            val chooser =  Intent.createChooser(contentIntent, "Selecciona")
            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(cameraIntent))

            return chooser
        }

        override fun parseResult(resultCode: Int, intent: Intent?): Boolean {
            if (resultCode == Activity.RESULT_OK) {
                intent?.data?.let {
                    println(it)
                }
                return true
            }
            return false
        }
    }

}

class MyContractPicture: ActivityResultContract<Int, Uri?>() {

    override fun createIntent(context: Context, input: Int?): Intent {
        return Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, input)
        }
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return intent?.data
    }
}

class MyContract: ActivityResultContract<String, Boolean?>() {

    override fun createIntent(context: Context, input: String?): Intent {
        return Intent(context, SecondActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Boolean? {
        if (resultCode != Activity.RESULT_OK) {
            return null
        }
        return intent?.getBooleanExtra("EXTRA_IMAGE_NAME", false)
    }

}


