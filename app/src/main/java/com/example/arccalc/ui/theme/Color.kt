package com.example.arccalc.ui.theme

import androidx.compose.ui.graphics.Color

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

data class WallpaperPreset(
    val id: String,
    val name: String,
    val hexCode: String,
    val color: Color
)

val WallpaperPresets = listOf(
    WallpaperPreset("botanical_sage", "Botanical Sage", "#73866C", Color(0xFF73866C)),
    WallpaperPreset("bay_blue", "Bay Blue", "#4A90E2", Color(0xFF4A90E2)),
    WallpaperPreset("feather_coral", "Feather Coral", "#E76F51", Color(0xFFE76F51)),
    WallpaperPreset("lavender_dusk", "Lavender Dusk", "#9370DB", Color(0xFF9370DB)),
    WallpaperPreset("mint_quartz", "Mint Quartz", "#52B788", Color(0xFF52B788)),
    WallpaperPreset("peony_rose", "Peony Rose", "#E06D85", Color(0xFFE06D85)),
    WallpaperPreset("hazel_gold", "Hazel Gold", "#C4A454", Color(0xFFC4A454)),
    WallpaperPreset("obsidian_charcoal", "Obsidian Charcoal", "#42474E", Color(0xFF42474E)),
    WallpaperPreset("lemongrass_green", "Lemongrass Green", "#868B6C", Color(0xFF868B6C)),
    WallpaperPreset("deep_azure", "Deep Azure", "#1E88E5", Color(0xFF1E88E5)),
    WallpaperPreset("forest_jade", "Forest Jade", "#2E7D32", Color(0xFF2E7D32)),
    WallpaperPreset("amethyst_violet", "Amethyst Violet", "#7B1FA2", Color(0xFF7B1FA2)),
    WallpaperPreset("terracotta_rust", "Terracotta Rust", "#D35400", Color(0xFFD35400)),
    WallpaperPreset("nordic_sky", "Nordic Sky", "#4FC3F7", Color(0xFF4FC3F7)),
    WallpaperPreset("desert_sand", "Desert Sand", "#C19A6B", Color(0xFFC19A6B)),
    WallpaperPreset("sakura_blossom", "Sakura Blossom", "#F48FB1", Color(0xFFF48FB1)),
    WallpaperPreset("arctic_glacier", "Arctic Glacier", "#80DEEA", Color(0xFF80DEEA)),
    WallpaperPreset("crimson_flame", "Crimson Flame", "#E53935", Color(0xFFE53935)),
    WallpaperPreset("neon_lime", "Neon Lime", "#AEEA00", Color(0xFFAEEA00)),
    WallpaperPreset("royal_cobalt", "Royal Cobalt", "#283593", Color(0xFF283593)),
    WallpaperPreset("sunset_amber", "Sunset Amber", "#FF8F00", Color(0xFFFF8F00)),
    WallpaperPreset("moss_green", "Moss Green", "#689F38", Color(0xFF689F38)),
    WallpaperPreset("mystic_teal", "Mystic Teal", "#00897B", Color(0xFF00897B)),
    WallpaperPreset("aurora_magenta", "Aurora Magenta", "#C2185B", Color(0xFFC2185B))
)
