package com.example.objectmovementsimulation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.objectmovementsimulation.ui.theme.ObjectMovementSimulationTheme
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ObjectMovementSimulationTheme {
                ObjectMovementSimulation()
            }
        }
    }
}

@Composable
fun ObjectMovementSimulation() {
    var xPosition by remember { mutableStateOf(100f) } // Initial X position
    var yPosition by remember { mutableStateOf(100f) } // Initial Y position
    var isMoving by remember { mutableStateOf(false) } // Track movement state
    var direction by remember { mutableStateOf(1) } // 1 for right, -1 for left
    var movementJob by remember { mutableStateOf<Job?>(null) } // Store coroutine job

    // Function to start movement using coroutines
    fun startMovement() {
        isMoving = true

        // Launch a coroutine to handle movement
        movementJob = CoroutineScope(Dispatchers.Main).launch {
            while (isMoving) {
                // Move in a zigzag pattern
                if (direction == 1) {
                    // Move to the right
                    xPosition += 10f
                    if (xPosition > 500f) { // Set a limit to stop and move down
                        direction = -1 // Change direction to left
                        yPosition += 50f // Move downward after reaching the right
                    }
                } else {
                    // Move to the left
                    xPosition -= 10f
                    if (xPosition < 100f) { // Set a limit to stop and move down
                        direction = 1 // Change direction to right
                        yPosition += 50f // Move downward after reaching the left
                    }
                }

                delay(100L) // Wait 100 milliseconds between updates
            }
        }
    }

    // Function to stop and reset movement
    fun resetMovement() {
        isMoving = false
        movementJob?.cancel() // Cancel the ongoing coroutine
        xPosition = 100f
        yPosition = 100f
        direction = 1 // Start from the right when reset
    }

    // Layout with Box to overlay buttons over the Canvas
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Canvas drawing the moving object
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = Color.Blue,
                radius = 50f,
                center = Offset(xPosition, yPosition) // Dynamic position
            )
        }

        // Column for the buttons, positioned at the bottom
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(onClick = { startMovement() }) {
                Text(text = "Start Movement")
            }

            Button(onClick = { resetMovement() }) {
                Text(text = "Reset")
            }
        }
    }
}
