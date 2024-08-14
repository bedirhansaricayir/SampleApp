package com.eterationcase.app.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.eterationcase.app.navigation.AppNavHost
import com.eterationcase.app.ui.theme.EterationCaseTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by bedirhansaricayir on 13.08.2024
 */

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EterationCaseTheme {
                AppNavHost()
            }
        }
    }
}