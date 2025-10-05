package com.emil.sharedportfoliolite

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.emil.sharedportfoliolite.ui.PortfolioScreen
import com.emil.sharedportfoliolite.ui.theme.SharedPortfolioLiteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SharedPortfolioLiteTheme {
                Scaffold { innerPadding ->
                    PortfolioScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
