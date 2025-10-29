package com.example.hackmatefrontendfolder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.hackmatefrontendfolder.navigation.NavGraph
import com.example.hackmatefrontendfolder.ui.theme.HackmateFrontendFolderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HackmateFrontendFolderTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)


            }
        }
    }
}
