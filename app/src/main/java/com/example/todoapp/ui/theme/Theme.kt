package com.example.todoapp.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Configuration du thème sombre (L'identité visuelle forte de ton projet IA)
private val DarkColorScheme = darkColorScheme(
    primary = KingRoyaleBlue,       // Bleu Cobalt Royal pour les boutons et éléments maîtres
    secondary = SmoothNeonBlue,     // Bleu Néon pour les accents technologiques et puces IA
    tertiary = ElectricBlue,        // Reflets cyan électriques secondaires
    background = DeepSpace,         // Fond d'espace profond ultra-sombre
    surface = SurfaceDark,          // Surfaces des cartes de tâches (NeonCard)
    onPrimary = TextPrimary,        // Texte clair sur le bleu royal
    onSecondary = DeepSpace,        // Texte sombre sur les puces claires pour le contraste
    onTertiary = DeepSpace,
    onBackground = TextPrimary,     // Texte principal sur le fond de l'application
    onSurface = TextPrimary,        // Texte sur les cartes
    outline = BorderGray            // Bordures fines ciselées
)

// Configuration du thème clair (Sécurité / Accessibilité demandée par le système Android)
private val LightColorScheme = lightColorScheme(
    primary = KingRoyaleBlue,
    secondary = SmoothNeonBlue,
    tertiary = ElectricBlue,
    background = Color(0xFFF8FAFC),   // Fond blanc bleuté très propre
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = DeepSpace,
    onTertiary = DeepSpace,
    onBackground = DeepSpace,
    onSurface = DeepSpace,
    outline = Color(0xFFE2E8F0)
)

@Composable
fun TodoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Choix dynamique du jeu de couleurs selon les préférences du smartphone
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    // Synchronisation de la barre de statut Android avec notre charte graphique
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // La barre de notification du téléphone prend exactement la couleur de notre fond d'appli
            window.statusBarColor = colorScheme.background.toArgb()
            // Ajuste l'icône de la batterie et de l'heure en blanc (si thème sombre) ou noir (si thème clair)
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Utilise ton fichier Typography.kt existant
        content = content
    )
}