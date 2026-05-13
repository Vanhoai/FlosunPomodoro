package com.flosun.pomodoro.presentation.swipe.home.components

import android.graphics.drawable.Icon
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosun.pomodoro.R
import com.flosun.pomodoro.ui.components.core.CoreBottomSheet
import com.flosun.pomodoro.ui.components.core.CoreButton
import com.flosun.pomodoro.ui.theme.AppTheme
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import com.flosun.pomodoro.adapters.database.LocalDatabase
import com.flosun.pomodoro.adapters.database.entities.SoundEntity
import com.flosun.pomodoro.adapters.database.entities.SoundType
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.core.constants.SoundIdKey
import com.flosun.pomodoro.core.services.audio.rememberAudioConnection
import com.flosunn.core.extensions.rippleEffectClickable
import com.flosunn.core.libraries.datastore.rememberPreference
import timber.log.Timber


//val sounds = listOf(
//    Sound("None", Uri.EMPTY),
//    Sound("Rain", "file:///android_asset/audios/rain.mp3".toUri()),
//    Sound("Forest", "file:///android_asset/audios/forest.mp3".toUri()),
//    Sound("Coffee Shop", "file:///android_asset/audios/coffee_shop.mp3".toUri()),
//    Sound("Fireplace", "file:///android_asset/audios/fireplace.mp3".toUri()),
//    Sound("Ocean Waves", "file:///android_asset/audios/ocean_waves.mp3".toUri()),
//    Sound("White Noise", "file:///android_asset/audios/white_noise.mp3".toUri()),
//)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MusicBottomSheet(
    sheetState: SheetState,
    closeBottomSheet: () -> Unit = {},
) {
    val database = LocalDatabase.current
    val audios by database.findSoundsByType(SoundType.AUDIO).collectAsState(emptyList())

    val audioConnection = rememberAudioConnection() ?: return
    var selectedSound by rememberPreference(SoundIdKey, "None")

    fun onSelectSound(sound: SoundEntity) {
        selectedSound = sound.id

        // Convert the Uri to a MediaItem and play it
        val mediaItem = MediaItem.fromUri(sound.uri)
        audioConnection.playMediaItem(mediaItem)
    }

    CoreBottomSheet(
        sheetState = sheetState,
        title = "Sound",
        closeBottomSheet = closeBottomSheet,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .rippleEffectClickable {
                    selectedSound = "None"
                    audioConnection.togglePlayPause()
                }
                .height(52.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_sound),
                contentDescription = null,
                tint = if (selectedSound == "None") AppTheme.colors.primaryColor else Color(
                    0xFF757575
                ),
                modifier = Modifier.padding(start = 8.dp)
            )

            Text(
                text = "None",
                fontSize = 16.sp,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp),
                color = if (selectedSound == "None") AppTheme.colors.primaryColor else Color(
                    0xFF757575
                )
            )
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 20.dp),
            color = Color(0xFFE0E0E0),
        )

        audios.forEachIndexed { index, sound ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(),
                        onClick = { onSelectSound(sound) },
                    )
                    .height(52.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_sound),
                    contentDescription = null,
                    tint = if (selectedSound == sound.id) AppTheme.colors.primaryColor else Color(
                        0xFF757575
                    ),
                    modifier = Modifier.padding(start = 8.dp)
                )

                Text(
                    text = sound.title,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp),
                    color = if (selectedSound == sound.id) AppTheme.colors.primaryColor else Color(
                        0xFF757575
                    )
                )
            }

            if (index != audios.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 20.dp),
                    color = Color(0xFFE0E0E0),
                )
            }
        }

        CoreButton(
            modifier = Modifier.padding(20.dp),
            onPress = {
                closeBottomSheet()
            }
        ) {
            Text(
                text = "Close",
                color = Color.White,
                fontSize = 16.sp,
            )
        }
    }
}