package com.music.bitchord.ui.components

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.PriorityHigh
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect

/**
 * The app's toast: a black capsule that springs out of the top edge. Callers
 * use [show] the way they used `Toast.makeText(...).show()`. With no
 * [IslandToastHost] on screen (a service, or the app in the background) it
 * falls back to the system toast so a notice is never lost.
 */
object IslandToast {
    enum class Kind { Info, Error }

    internal data class Event(val text: String, val kind: Kind)

    internal val events = MutableSharedFlow<Event>(extraBufferCapacity = 8)

    @Volatile
    internal var hostCount = 0

    fun show(context: Context, text: CharSequence, kind: Kind = Kind.Info) {
        if (hostCount > 0 && events.tryEmit(Event(text.toString(), kind))) return
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }

    fun show(context: Context, @StringRes res: Int, kind: Kind = Kind.Info) =
        show(context, context.getString(res), kind)
}

@Composable
fun IslandToastHost(modifier: Modifier = Modifier) {
    var current by remember { mutableStateOf<IslandToast.Event?>(null) }
    var visible by remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        IslandToast.hostCount++
        onDispose { IslandToast.hostCount-- }
    }
    LaunchedEffect(Unit) {
        IslandToast.events.collect { event ->
            // A newer notice replaces the one on screen rather than queueing behind it.
            current = event
            visible = true
        }
    }
    LaunchedEffect(current) {
        if (current != null) {
            delay(2_800)
            visible = false
        }
    }

    Box(modifier.fillMaxSize().statusBarsPadding().padding(top = 8.dp), contentAlignment = Alignment.TopCenter) {
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(spring(dampingRatio = 0.55f, stiffness = Spring.StiffnessMediumLow)) { -it * 2 } +
                scaleIn(initialScale = 0.6f) + fadeIn(),
            exit = slideOutVertically { -it * 2 } + scaleOut(targetScale = 0.7f) + fadeOut(),
        ) {
            val event = current
            if (event != null) {
                IslandToastPill(event.text, event.kind)
            }
        }
    }
}

@Composable
internal fun IslandToastPill(text: String, kind: IslandToast.Kind) {
    val error = kind == IslandToast.Kind.Error
    Row(
        modifier = Modifier
            .widthIn(max = 340.dp)
            .clip(CircleShape)
            .background(Color.Black)
            .padding(start = 8.dp, end = 16.dp, top = 8.dp, bottom = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            Modifier
                .size(26.dp)
                .clip(CircleShape)
                .background(if (error) Color(0xFFD8453D) else Color(0xFF1F9D6B)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                if (error) Icons.Rounded.PriorityHigh else Icons.Rounded.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp),
            )
        }
        Text(
            text = text,
            modifier = Modifier.padding(start = 10.dp),
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
