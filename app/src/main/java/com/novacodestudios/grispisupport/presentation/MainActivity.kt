package com.novacodestudios.grispisupport.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.exclude
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.novacodestudios.grispisupport.presentation.navigation.SupportNavHost
import com.novacodestudios.grispisupport.presentation.theme.GrispiSupportTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GrispiSupportTheme {
                Scaffold(
                    modifier = Modifier.Companion.fillMaxSize(),
                    contentWindowInsets = ScaffoldDefaults.contentWindowInsets
                        .exclude(TopAppBarDefaults.windowInsets).exclude(NavigationBarDefaults.windowInsets)
                ) { innerPadding ->
                    SupportNavHost(modifier = Modifier.padding(innerPadding), navController = rememberNavController())
                }
            }
        }
    }
}