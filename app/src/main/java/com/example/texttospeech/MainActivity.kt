package com.example.texttospeech

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import java.util.Locale
import com.example.texttospeech.ui.theme.TextToSpeechAppTheme
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TextToSpeechTheme {
                TextToSpeechScreen()
            }
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextToSpeechScreen() {
    val context = LocalContext.current
    var textToSpeak by remember { mutableStateOf("") }
    var tts: TextToSpeech? by remember { mutableStateOf(null) }
    var isInitialized by remember { mutableStateOf(false) }


    DisposableEffect(context) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result=tts?.setLanguage(Locale.US)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "The Language specified is not supported!")
                    isInitialized = false
                } else {
                    Log.d("TTS", "Initialization successful!")
                    isInitialized = true
                }
                }else{
                    Log.e("TTS", "Initialization failed!")
                    isInitialized=false
                }
            }
        onDispose {
            tts?.stop()
            tts?.shutdown()
            isInitialized = false
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Text to Speech", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1C1B20),
                )
            )
        }, containerColor = Color(0xFF2C2B30),
        content = { paddingValues ->
            // Content of the screen goes here, apply paddingValues
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Spacer(modifier = Modifier.height(32.dp))

                OutlinedTextField(
                    value = textToSpeak,
                    onValueChange = { textToSpeak = it }, // Update state on text change.
                    label = { Text("Enter text to speak", color = Color.Gray) }, // Hint text.
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp), // Fill width and add padding.
                    shape = RoundedCornerShape(12.dp), // Rounded corners for the text field.
                    colors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color(0xFF6A1B9A),
                        unfocusedIndicatorColor = Color.Gray.copy(alpha = 0.5f), // Gray border when unfocused.
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color(0xFF6A1B9A), // Purple cursor color.
                        focusedContainerColor = Color(0xFF3C3B40), // Darker background for the text field.
                        unfocusedContainerColor = Color(0xFF3C3B40)
                    ),
                    textStyle = TextStyle(color = Color.White)
                )
                Spacer(modifier = Modifier.height(32.dp)) // Spacing between text field and button.

                // Speak Button.
                Button(
                    onClick = {
                        if (isInitialized && textToSpeak.isNotBlank()) {
                            tts?.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, "")
                        } else if (!isInitialized) {
                            Log.e("TTS", "TextToSpeech engine is not initialized.")
                            // Optionally show a Toast message to the user.
                        } else if (textToSpeak.isBlank()) {
                            Log.d("TTS", "No text entered to speak.")
                            // Optionally show a Toast message to the user.
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth() // Fill width of the parent.
                        .height(64.dp), // Set a fixed height for the button.
                    shape = RoundedCornerShape(20.dp), // Highly rounded corners for the button.
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent), // Make button transparent to show brush.
                    contentPadding = PaddingValues(0.dp) // Remove default content padding to allow brush to fill.
                ) {
                    // Apply a linear gradient background to the button's content.
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.linearGradient(
                                    colors = listOf(Color(0xFF6A1B9A), Color(0xFF4A148C)) // Purple gradient.
                                ),
                                shape = RoundedCornerShape(20.dp)
                            ),
                        contentAlignment = Alignment.Center // Center content within the box.
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp) // Space between icon and text.
                        ) {
                            // Speaker icon inside the button.
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Speak",
                                tint = Color.White,
                                modifier = Modifier.size(28.dp) // Larger icon size.
                            )
                            // Button text.
                            Text(
                                "Speak",
                                color = Color.White,
                                fontSize = 22.sp, // Larger font size for the button text.
                                style = MaterialTheme.typography.titleLarge // Apply Material 3 typography.
                            )
                        }
                    }
                }
            }
        })
}

@Preview(showBackground = true, widthDp = 360)
@Composable
fun TextToSpeechScreenPreview() {
    TextToSpeechAppTheme {
        TextToSpeechScreen()
    }
}

@Composable
fun TextToSpeechTheme(content: @Composable () -> Unit) {
    TextToSpeechAppTheme(content = content)
}