package com.example.todoapp.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.todoapp.ui.components.NeonButton
import kotlinx.coroutines.launch

data class OnboardingPage(
    val title: String,
    val description: String,
    val color: Color
)

val pages = listOf(
    OnboardingPage(
        "Priorisation par IA",
        "Notre algorithme intelligent trie automatiquement vos tâches selon leur priorité réelle.",
        Color(0xFF3B82F6)
    ),
    OnboardingPage(
        "Maîtrisez votre Temps",
        "Suivez facilement vos deadlines et votre planning quotidien.",
        Color(0xFF2563EB)
    ),
    OnboardingPage(
        "Analyse d'Habitudes",
        "L'application s'adapte à votre routine pour améliorer votre productivité.",
        Color(0xFF1D4ED8)
    )
)

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    val colors = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { index ->

            val page = pages[index]

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(40.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                // ICON DOT
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .background(
                            page.color.copy(alpha = 0.15f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .background(page.color, CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = page.title,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = colors.onBackground
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = page.description,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = colors.onBackground.copy(alpha = 0.7f)
                )
            }
        }

        // FOOTER
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(32.dp)
        ) {

            if (pagerState.currentPage == pages.lastIndex) {

                NeonButton(
                    text = "Get Started",
                    onClick = onFinish,
                    modifier = Modifier.fillMaxWidth()
                )

            } else {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    TextButton(onClick = onFinish) {
                        Text("Skip")
                    }

                    NeonButton(
                        text = "Next",
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        }
                    )
                }
            }
        }
    }
}