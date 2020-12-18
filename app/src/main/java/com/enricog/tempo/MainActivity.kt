package com.enricog.tempo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.enricog.timer.TimerFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.rootContainer, TimerFragment.newInstance(1))
                setReorderingAllowed(true)
                commit()
            }
        }

    }
}