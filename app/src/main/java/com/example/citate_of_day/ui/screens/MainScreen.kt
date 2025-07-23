package com.example.citate_of_day.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.citate_of_day.viewmodel.QuoteViewModel

@Composable
fun MainScreen(viewModel: QuoteViewModel = viewModel()) {
    val quote by viewModel.quote.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val infiniteTransition = rememberInfiniteTransition(label = "background")
    val animatedDegree by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "background_rotation"
    )

    var shouldShowCard by remember { mutableStateOf(false) }
    LaunchedEffect(quote) {
        if (quote != null && !isLoading) {
            shouldShowCard = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF1E3C72),
                        Color(0xFF2A5298),
                        Color(0xFF2C3E70),
                        Color(0xFF1E3C72)
                    ),
                    start = Offset.Zero,
                    end = Offset.Infinite,
                    tileMode = TileMode.Clamp
                )
            )
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val radius = size.minDimension * 0.4f

            drawCircle(
                color = Color.White.copy(alpha = 0.05f),
                radius = radius,
                center = Offset(size.width * 0.8f, size.height * 0.2f),
                style = Stroke(width = 2.dp.toPx())
            )

            drawCircle(
                color = Color.White.copy(alpha = 0.03f),
                radius = radius * 0.7f,
                center = Offset(size.width * 0.2f, size.height * 0.8f),
                style = Stroke(width = 1.5f.dp.toPx())
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Quote of day",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .alpha(0.9f)
            )

            AnimatedVisibility(
                visible = isLoading,
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    color = Color.White,
                    strokeWidth = 4.dp
                )
            }

            AnimatedVisibility(
                visible = !isLoading && quote != null && shouldShowCard,
                enter = expandVertically(
                    expandFrom = Alignment.CenterVertically
                ) + fadeIn(initialAlpha = 0.3f),
                exit = shrinkVertically() + fadeOut()
            ) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 16.dp,
                            spotColor = Color(0xFF1E3C72),
                            shape = RoundedCornerShape(20.dp)
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(28.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Text(
                            text = "❝",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E3C72).copy(alpha = 0.2f),
                            textAlign = TextAlign.Start,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text(
                            text = quote?.q ?: "",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            lineHeight = 32.sp,
                            color = Color(0xFF333333),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        HorizontalDivider(
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .width(80.dp)
                                .align(Alignment.CenterHorizontally),
                            thickness = 2.dp,
                            color = Color(0xFF1E3C72).copy(alpha = 0.2f)
                        )

                        Text(
                            text = "— ${quote?.a ?: ""}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.End,
                            fontStyle = FontStyle.Italic,
                            color = Color(0xFF555555),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text(
                            text = "❞",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E3C72).copy(alpha = 0.2f),
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.loadRandomQuote() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF1E3C72)
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 12.dp
                ),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
                modifier = Modifier
                    .height(56.dp)
                    .shadow(
                        elevation = 6.dp,
                        shape = RoundedCornerShape(16.dp),
                        spotColor = Color(0xFF000000).copy(alpha = 0.3f)
                    )
            ) {
                Icon(
                    Icons.Default.Refresh,
                    contentDescription = null,
                    tint = Color(0xFF1E3C72),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    "New quote",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1E3C72)
                )
            }
        }
    }
}