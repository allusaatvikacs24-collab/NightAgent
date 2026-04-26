package com.example.nightagent.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.nightagent.ui.theme.NightagentTheme
import com.example.nightagent.ui.theme.PurpleEnd
import com.example.nightagent.ui.theme.PurpleStart
import android.content.Context
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.WindowManager
import kotlinx.coroutines.delay

sealed class CallState {
    object Incoming : CallState()
    object Active : CallState()
}

class FakeCallActivity : ComponentActivity() {
    private lateinit var ringtone: Ringtone
    private lateinit var vibrator: Vibrator
    private val vibrationPattern = longArrayOf(0L, 1000L, 500L, 1000L) // Wait, vibrate, wait, vibrate

    private fun stopRingtone() {
        if (::ringtone.isInitialized && ringtone.isPlaying) {
            ringtone.stop()
        }
        try {
            vibrator.cancel()
        } catch (e: Exception) {
            // Ignore
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        ringtone = RingtoneManager.getRingtone(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE))
        ringtone.play()
        
        vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            (getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager).defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(vibrationPattern, -1))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(vibrationPattern, 0)
        }
        
        // Fullscreen flags
        WindowCompat.setDecorFitsSystemWindows(window, false)
        enableEdgeToEdge()
        WindowCompat.getInsetsController(window, window.decorView)?.let { controller ->
            controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            controller.hide(WindowInsetsCompat.Type.systemBars())
        }
        
        // Keep screen on during call
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        
        setContent {
            NightagentTheme {
                FakeCallContent(
                    onDecline = {
                        stopRingtone()
                        finish()
                    },
                    onStopRingtone = ::stopRingtone,
                    onFinishActivity = { finish() }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            vibrator.cancel()
        } catch (e: Exception) {
            // Ignore
        }
        stopRingtone()
    }

    companion object {
        fun startFakeCall(context: Context) {
            val intent = android.content.Intent(context, FakeCallActivity::class.java).apply {
                flags = android.content.Intent.FLAG_ACTIVITY_NEW_TASK or android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
            }
            context.startActivity(intent)
        }

        fun startFakeCallDelayed(context: Context, delayMs: Long) {
            Handler(Looper.getMainLooper()).postDelayed({
                startFakeCall(context)
            }, delayMs)
        }
    }
}

@Composable
fun FakeCallContent(
    onDecline: () -> Unit,
    onStopRingtone: () -> Unit,
    onFinishActivity: () -> Unit
) {
    var callState by remember { mutableStateOf<CallState>(CallState.Incoming) }
    
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    // Accept handler
    val onAccept: () -> Unit = { 
        callState = CallState.Active 
    }
    
    // Reject handler
    val onReject: () -> Unit = { 
        onStopRingtone()
        onFinishActivity() 
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        PurpleStart,
                        PurpleEnd,
                        Color(0xFF1A1A2E),
                        Color.Black
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Stop ringtone when accepted
            LaunchedEffect(callState) {
                if (callState is CallState.Active) {
                    onStopRingtone()
                }
            }
            
            AnimatedVisibility(
                visible = callState is CallState.Incoming,
                enter = fadeIn(animationSpec = tween(800)) + scaleIn(initialScale = 0.8f, animationSpec = tween(800)),
                exit = fadeOut(animationSpec = tween(800)) + scaleOut(animationSpec = tween(800))
            ) {
                IncomingCallUI(
                    pulseScale, 
                    onAccept = onAccept,
                    onReject = onReject
                )
            }
            
            AnimatedVisibility(
                visible = callState is CallState.Active,
                enter = fadeIn(animationSpec = tween(800)) + scaleIn(initialScale = 0.8f, animationSpec = tween(800)),
                exit = fadeOut(animationSpec = tween(800)) + scaleOut(animationSpec = tween(800))
            ) {
                OngoingCallUI(pulseScale, onEndCall = {
                    onStopRingtone()
                    onFinishActivity()
                })
            }
        }
    }
}

@Composable
fun IncomingCallUI(
    pulseScale: Float,
    onAccept: () -> Unit,
    onReject: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Caller avatar
        Icon(
            Icons.Default.Person,
            contentDescription = "Caller Avatar",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.3f)),
            tint = Color.White
        )
        Spacer(modifier = Modifier.height(24.dp))
        
        // Pulsing call icon
        Icon(
            imageVector = Icons.Default.Call,
            contentDescription = "Incoming Call",
            modifier = Modifier
                .size(120.dp)
                .scale(pulseScale)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.2f)),
            tint = Color.White,
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Caller name
        Text(
            text = "MOM",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Incoming Call...",
            fontSize = 20.sp,
            color = Color.White.copy(alpha = 0.8f),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Action buttons
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Accept button (Green)
            Button(
                onClick = onAccept,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                )
            ) {
                Icon(
                    Icons.Default.Call,
                    contentDescription = "Accept",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
            
            // Decline button (Red)
            Button(
                onClick = onReject,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF44336)
                )
            ) {
                Icon(
                    Icons.Default.CallEnd,
                    contentDescription = "Decline",
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Composable
fun OngoingCallUI(pulseScale: Float, onEndCall: () -> Unit) {
    val startTime = remember { System.currentTimeMillis() }
    val elapsedSeconds = ((System.currentTimeMillis() - startTime) / 1000) % 3600
    val minutes = elapsedSeconds / 60
    val seconds = elapsedSeconds % 60
    val timeString = "%d:%02d".format(minutes, seconds)
    
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Large pulsing call background
        Icon(
            imageVector = Icons.Default.Call,
            contentDescription = "Ongoing Call",
            modifier = Modifier
                .size(200.dp)
                .scale(pulseScale * 1.2f)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.15f)),
            tint = Color.White.copy(alpha = 0.8f)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "ONGOING CALL",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "MOM",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = timeString,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White.copy(alpha = 0.9f),
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        // End call button
        Button(
            onClick = onEndCall,
            modifier = Modifier
                .size(90.dp)
                .clip(CircleShape),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFF44336)
            )
        ) {
            Icon(
                Icons.Default.CallEnd,
                contentDescription = "End Call",
                tint = Color.White,
                modifier = Modifier.size(36.dp)
            )
        }
    }
}
