package com.music.bitchord.ui.preview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.music.bitchord.ui.components.IslandToast
import com.music.bitchord.ui.components.IslandToastPill
import com.music.bitchord.ui.theme.BitChordTheme

/** The toast capsule at rest: info, error, and a long line that has to truncate. */
@Preview(name = "Island toast", widthDp = 400, heightDp = 220, showBackground = true)
@Composable
private fun IslandToastPreview() {
    BitChordTheme(darkTheme = true) {
        Column(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            IslandToastPill("Added to queue", IslandToast.Kind.Info)
            IslandToastPill("Couldn't reach the party server", IslandToast.Kind.Error)
            IslandToastPill(
                "A considerably longer notice that runs past two lines and has to be cut off with an ellipsis",
                IslandToast.Kind.Info,
            )
        }
    }
}
