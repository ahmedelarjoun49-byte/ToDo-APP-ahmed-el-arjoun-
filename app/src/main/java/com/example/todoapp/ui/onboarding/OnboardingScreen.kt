package com.example.todoapp.ui.onboarding

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.todoapp.ui.components.NeonButton
import com.example.todoapp.ui.components.SmoothNeonBlue
import com.example.todoapp.ui.components.DeepBlueGlow
import com.example.todoapp.ui.theme.*
import kotlinx.coroutines.launch

data class OnboardingPage(
    val title: String,
    val description: String,
    val color: Color
)

// Modification : Textes orientés "IA / Emploi du temps" et palettes de bleus uniformisées
val pages = listOf(
    OnboardingPage(
        "Priorisation par IA",
        "Notre algorithme intelligent trie automatiquement vos tâches en fonction de leur urgence réelle et de leur importance.",
        SmoothNeonBlue
    ),
    OnboardingPage(
        "Maîtrisez votre Temps",
        "Une intégration fluide avec votre emploi du temps quotidien pour ne jamais rater une date limite cruciale.",
        DeepBlueGlow
    ),
    OnboardingPage(
        "Analyse d'Habitudes",
        "L'application apprend de votre routine quotidienne pour vous proposer la bonne tâche au moment idéal.",
        ElectricBlue
    )
)

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepSpace)
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
                // Un petit indicateur visuel de la couleur de la page (Optionnel/Subtile)
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(page.color.copy(alpha = 0.1f), androidx.compose.foundation.shape.CircleShape)
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Icône ou point lumineux bleu
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(page.color, androidx.compose.foundation.shape.CircleShape)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = page.title,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = page.description,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    color = TextSecondary
                )
            }
        }

        // Footer (Utilise maintenant notre NeonButton bleu par défaut)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(40.dp)
        ) {
            if (pagerState.currentPage == pages.size - 1) {
                NeonButton(
                    text = "Get Started",
                    onClick = onFinish,
                    containerColor = SmoothNeonBlue,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onFinish) {
                        Text("Skip", color = TextSecondary)
                    }
                    NeonButton(
                        text = "Next",
                        containerColor = SmoothNeonBlue,
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