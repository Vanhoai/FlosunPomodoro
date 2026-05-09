package com.flosun.pomodoro.ui.components.shared

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.flosunn.core.extensions.noRippleEffectClickable
import com.flosunn.core.extensions.rippleEffectClickable
import com.flosun.pomodoro.R
import com.flosun.pomodoro.core.constants.DEBUG_TAG
import com.flosun.pomodoro.ui.components.core.CoreAsyncImage
import com.flosun.pomodoro.ui.components.core.CoreTextField
import timber.log.Timber

private const val MAX_ITEMS = 5
private val contract = ActivityResultContracts.PickMultipleVisualMedia(MAX_ITEMS)

@Composable
fun RewardSection(
    reward: String,
    onChangedReward: (String) -> Unit,
    rewardImages: List<String>,
    onChangedRewardImages: (List<String>) -> Unit,
    onDeleteRewardImage: (String) -> Unit,
) {
    val pickMultipleMedia = rememberLauncherForActivityResult(contract) { uris ->
        if (uris.isEmpty()) return@rememberLauncherForActivityResult
        val newImages = emptySet<String>()
            .plus(rewardImages)
            .plus(uris.map { it.toString() })

        onChangedRewardImages(newImages.toList())
    }

    Text(
        text = "Reward",
        fontSize = 18.sp,
        modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 12.dp)
    )

    CoreTextField(
        value = reward,
        onValueChanged = { onChangedReward(it) },
        maxLines = 4,
        singleLine = false,
        height = 100.dp,
        placeholder = "e.g., Go to travel, buy a new phone, etc.",
        padding = PaddingValues(horizontal = 20.dp)
    )

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        items(rewardImages) { image ->
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .border(
                        width = 1.dp,
                        color = Color(0xFFCCCCCC),
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.TopEnd,
            ) {
                CoreAsyncImage(
                    url = image,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(8.dp)),
                    onPress = {}
                )

                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(20.dp)
                        .noRippleEffectClickable { onDeleteRewardImage(image) },
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_delete),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        }

        item {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFF9F9F9))
                    .rippleEffectClickable {
                        pickMultipleMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_upload),
                    contentDescription = null,
                    tint = Color(0xFFCDCDCD),
                    modifier = Modifier.size(24.dp),
                )
            }
        }
    }
}