package com.aimcore.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val refreshHz = windowManager.defaultDisplay.refreshRate.roundToInt()

        setContent {
            AimCoreApp(refreshHz)
        }
    }
}

@Composable
fun AimCoreApp(refreshHz: Int) {

    var fps by remember { mutableIntStateOf(60) }
    var gyro by remember { mutableStateOf(true) }
    var ram by remember { mutableIntStateOf(6) }
    var result by remember {
        mutableStateOf("اضبط بيانات جهازك واضغط «إنشاء الحساسية».")
    }

    MaterialTheme {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {

            Text(
                text = "AimCore",
                style = MaterialTheme.typography.headlineLarge
            )

            Text(
                text = "مولّد حساسية تجريبي حسب مواصفات جهازك"
            )

            Text("معدل تحديث الشاشة: ${refreshHz}Hz")

            Text("اختار FPS المستهدف")

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(30, 40, 60, 90, 120).forEach { value ->

                    FilterChip(
                        selected = fps == value,
                        onClick = { fps = value },
                        label = { Text("$value") }
                    )
                }
            }

            Text("RAM: ${ram}GB")

            Slider(
                value = ram.toFloat(),
                onValueChange = {
                    ram = it.roundToInt().coerceIn(2, 16)
                },
                valueRange = 2f..16f,
                steps = 13
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Gyroscope")

                Switch(
                    checked = gyro,
                    onCheckedChange = { gyro = it }
                )
            }

            Button(
                onClick = {

                    val camera = when {
                        fps >= 90 && ram >= 6 -> 120
                        fps >= 60 -> 105
                        else -> 90
                    }

                    val ads = if (gyro) 125 else 100

                    result = """
                        إعداد مقترح:

                        Camera / No Scope: $camera
                        ADS: $ads
                        Red Dot: ${(camera * 0.82).roundToInt()}
                        2x: ${(camera * 0.68).roundToInt()}
                        4x: ${(camera * 0.52).roundToInt()}

                        Gyroscope: ${if (gyro) "مفعّل" else "غير مفعّل"}

                        هذه قيم بداية تجريبية وليست ضمانًا لأفضل أداء.
                    """.trimIndent()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("إنشاء الحساسية")
            }

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = result,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Text(
                text = "AimCore لا يعدّل ملفات اللعبة ولا يضمن نتائج أو دقة تصويب معينة.",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
