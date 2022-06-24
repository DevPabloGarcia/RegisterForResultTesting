package com.pablogarcia.registerforresult

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SecondActivity: AppCompatActivity(R.layout.activity_second) {

    private var button: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        button = findViewById(R.id.button_second)
        button?.setOnClickListener {
            setResult(
                Activity.RESULT_OK, Intent().putExtra(
                "EXTRA_IMAGE_NAME", true
            ))
            finish()
        }
    }

}
