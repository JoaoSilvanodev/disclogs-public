package com.clogs.disclogs.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.clogs.disclogs.ui.theme.DisclogsTheme
import java.util.Locale

@Composable
fun RatingHistogramSection(
    userRating: Double, inLib: Boolean, avgRating: Double, distribution: List<Int>

) {
    var activeIndex by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(1f)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {

                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = "YOURS",
                        color = Color.Gray,
                        fontSize = 10.sp,
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    if (inLib && userRating > 0.0) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = userRating.toString(),
                                color = MaterialTheme.colorScheme.onBackground,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    } else {
                        Text(text = "---", color = Color.Gray, fontSize = 16.sp)
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "COMMUNITY",
                        color = Color.Gray,
                        fontSize = 10.sp,
                        letterSpacing = 1.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (avgRating > 0) String.format(
                                Locale.US, "%.1f", avgRating
                            ) else "---",
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))


            val maxCount = distribution.maxOrNull()?.takeIf { it > 0 } ?: 1

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                distribution.forEachIndexed { index, count ->
                    val relativeHeight = count.toFloat() / maxCount

                    val rawLabel = (index + 1) / 2.0
                    val labelText =
                        if (rawLabel % 1.0 == 0.0) rawLabel.toInt()
                            .toString() else rawLabel.toString()

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Bottom,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                    ) {
                        Box(
                            modifier = Modifier.height(20.dp),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            if (activeIndex == index) {
                                Text(
                                    text = count.toString(),
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                            }
                        }


                        Box(
                            modifier = Modifier
                                .width(50.dp)
                                .weight(1f)
                                .pointerInput(Unit) {
                                    detectTapGestures(
                                        onPress = {
                                            activeIndex = index
                                            tryAwaitRelease()
                                            activeIndex = null
                                        }
                                    )
                                },
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(relativeHeight.coerceAtLeast(0.02f))
                                    .background(
                                        color = if (activeIndex == index)
                                            MaterialTheme.colorScheme.primary
                                        else
                                            MaterialTheme.colorScheme.primary.copy(alpha = if (count > 0) 0.8f else 0.2f),
                                        shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                                    )
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))

                        Box(
                            modifier = Modifier.height(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = labelText,
                                color = if (activeIndex == index) MaterialTheme.colorScheme.primary else Color.Gray,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RatingHistogramSectionPreview() {
    DisclogsTheme {
        RatingHistogramSection(
            userRating = 4.5,
            inLib = true,
            avgRating = 4.2,
            distribution = listOf(2, 5, 10, 20, 45, 30, 15, 8, 4, 1)
        )
    }
}