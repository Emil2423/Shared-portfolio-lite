package com.emil.sharedportfoliolite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.emil.sharedportfoliolite.ui.PortfolioScreen
import com.emil.sharedportfoliolite.ui.theme.SharedPortfolioLiteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SharedPortfolioLiteTheme {
                PortfolioScreen()
            }
        }
    }
}
