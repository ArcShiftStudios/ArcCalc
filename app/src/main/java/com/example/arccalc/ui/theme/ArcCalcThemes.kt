package com.example.arccalc.ui.theme

import androidx.compose.ui.graphics.Color
import com.example.arccalc.model.DesignFinish

data class ArcThemeDefinition(
    val id: String,
    val name: String,
    val description: String,
    val category: String, // "Material 3 Expressive", "Automotive / Supercar", "Cyber & Neon", "Wireframe", "AMOLED & Mineral"
    val defaultFinish: DesignFinish,
    val isDark: Boolean,
    val previewDots: List<Color>,
    val palette: TonalRolePalette
)

object ArcCalcThemes {

    // 1. Botanical Sage
    val BotanicalSage = ArcThemeDefinition(
        id = "botanical_sage",
        name = "Botanical Sage",
        description = "Organic sage green tone harmonized with deep forest obsidian",
        category = "AMOLED & Mineral",
        defaultFinish = DesignFinish.GLOW_CONCAVE,
        isDark = true,
        previewDots = listOf(Color(0xFF0F1511), Color(0xFF5B9E6E), Color(0xFF67B77E)),
        palette = TonalRolePalette(
            primaryAccent = Color(0xFF5B9E6E),
            operators = Color(0xFF385E43),
            equalsAction = Color(0xFF67B77E),
            clearAction = Color(0xFF4A6B53),
            background = Color(0xFF0F1511),
            displaySurface = Color(0xFF161F19),
            row2Keys = Color(0xFF223027),
            row3Keys = Color(0xFF223027),
            numberKeys = Color(0xFF1C2720),
            textPrimary = Color(0xFFE2F0E6),
            textSecondary = Color(0xFF8AA893),
            borderGlow = Color(0xFF437552).copy(alpha = 0.4f),
            isDark = true
        )
    )

    // 2. Arc Shift Chromatic
    val ArcShiftChromatic = ArcThemeDefinition(
        id = "arc_shift_chromatic",
        name = "Arc Shift Chromatic",
        description = "Signature modern dark aesthetic with cohesive chromatic tonal rows and velvet depth",
        category = "Cyber & Neon",
        defaultFinish = DesignFinish.GLOW_CONCAVE,
        isDark = true,
        previewDots = listOf(Color(0xFF0B0D14), Color(0xFF00E5FF), Color(0xFFFF3366)),
        palette = TonalRolePalette(
            primaryAccent = Color(0xFF00E5FF),
            operators = Color(0xFF3D7BFF),
            equalsAction = Color(0xFFFF3366),
            clearAction = Color(0xFF00B4D8),
            background = Color(0xFF0B0D14),
            displaySurface = Color(0xFF131722),
            row2Keys = Color(0xFF1C2333),
            row3Keys = Color(0xFF1C2333),
            numberKeys = Color(0xFF161C2A),
            textPrimary = Color(0xFFF0F4FF),
            textSecondary = Color(0xFF8E9BB5),
            borderGlow = Color(0xFF00E5FF).copy(alpha = 0.35f),
            isDark = true
        )
    )

    // 3. AMOLED Stealth
    val AmoledStealth = ArcThemeDefinition(
        id = "amoled_stealth",
        name = "AMOLED Stealth",
        description = "Pure black concave depth with metallic charcoal glow from design reference",
        category = "AMOLED & Mineral",
        defaultFinish = DesignFinish.GLOW_CONCAVE,
        isDark = true,
        previewDots = listOf(Color(0xFF000000), Color(0xFF94A3B8), Color(0xFFEF4444)),
        palette = TonalRolePalette(
            primaryAccent = Color(0xFF94A3B8),
            operators = Color(0xFF334155),
            equalsAction = Color(0xFFEF4444),
            clearAction = Color(0xFF475569),
            background = Color(0xFF000000),
            displaySurface = Color(0xFF0A0A0C),
            row2Keys = Color(0xFF14161A),
            row3Keys = Color(0xFF14161A),
            numberKeys = Color(0xFF101216),
            textPrimary = Color(0xFFF8FAFC),
            textSecondary = Color(0xFF94A3B8),
            borderGlow = Color(0xFF475569).copy(alpha = 0.4f),
            isDark = true
        )
    )

    // 4. Wireframe Dark
    val WireframeDark = ArcThemeDefinition(
        id = "wireframe_dark",
        name = "Wireframe Dark",
        description = "Architectural CAD blueprint with circular reticle crosshairs and solid red equals on pitch black",
        category = "Wireframe",
        defaultFinish = DesignFinish.WIREFRAME,
        isDark = true,
        previewDots = listOf(Color(0xFF050505), Color(0xFFFFFFFF), Color(0xFFFF3B30)),
        palette = TonalRolePalette(
            primaryAccent = Color(0xFFFFFFFF),
            operators = Color(0xFF000000),
            equalsAction = Color(0xFFFF3B30),
            clearAction = Color(0xFF000000),
            background = Color(0xFF050505),
            displaySurface = Color(0xFF050505),
            row2Keys = Color(0xFF000000),
            row3Keys = Color(0xFF000000),
            numberKeys = Color(0xFF000000),
            textPrimary = Color(0xFFFFFFFF),
            textSecondary = Color(0xFF9E9E9E),
            borderGlow = Color(0xFFFFFFFF),
            isDark = true,
            isWireframeReticle = true
        )
    )

    // 5. Wireframe Light
    val WireframeLight = ArcThemeDefinition(
        id = "wireframe_light",
        name = "Wireframe Light",
        description = "Clean architectural drafting blueprint with circular CAD crosshairs and solid red equals on pure white",
        category = "Wireframe",
        defaultFinish = DesignFinish.WIREFRAME,
        isDark = false,
        previewDots = listOf(Color(0xFFFFFFFF), Color(0xFF000000), Color(0xFFFF3B30)),
        palette = TonalRolePalette(
            primaryAccent = Color(0xFF000000),
            operators = Color(0xFFFFFFFF),
            equalsAction = Color(0xFFFF3B30),
            clearAction = Color(0xFFFFFFFF),
            background = Color(0xFFFFFFFF),
            displaySurface = Color(0xFFFFFFFF),
            row2Keys = Color(0xFFFFFFFF),
            row3Keys = Color(0xFFFFFFFF),
            numberKeys = Color(0xFFFFFFFF),
            textPrimary = Color(0xFF000000),
            textSecondary = Color(0xFF555555),
            borderGlow = Color(0xFF000000),
            isDark = false,
            isWireframeReticle = true
        )
    )

    // 6. Minimal Light
    val MinimalLight = ArcThemeDefinition(
        id = "minimal_light",
        name = "Minimal Light",
        description = "Refined modern minimalist white porcelain aesthetic with subtle tactile edges",
        category = "Material 3 Expressive",
        defaultFinish = DesignFinish.M3_FLAT,
        isDark = false,
        previewDots = listOf(Color(0xFFF8FAFC), Color(0xFF2563EB), Color(0xFF0F172A)),
        palette = TonalRolePalette(
            primaryAccent = Color(0xFF2563EB),
            operators = Color(0xFFE2E8F0),
            equalsAction = Color(0xFF2563EB),
            clearAction = Color(0xFFE2E8F0),
            background = Color(0xFFF8FAFC),
            displaySurface = Color(0xFFFFFFFF),
            row2Keys = Color(0xFFEDF2F7),
            row3Keys = Color(0xFFEDF2F7),
            numberKeys = Color(0xFFF1F5F9),
            textPrimary = Color(0xFF0F172A),
            textSecondary = Color(0xFF64748B),
            borderGlow = Color(0xFFCBD5E1),
            isDark = false
        )
    )

    // 7. Material 3 Expressive
    val Material3Expressive = ArcThemeDefinition(
        id = "m3_expressive",
        name = "Material 3 Expressive",
        description = "Dynamic Google Material You theme that adapts to your phone wallpaper and system dark/light mode",
        category = "Material 3 Expressive",
        defaultFinish = DesignFinish.M3_FLAT,
        isDark = true,
        previewDots = listOf(Color(0xFF141218), Color(0xFFD0BCFF), Color(0xFF4F378B)),
        palette = TonalRolePalette(
            primaryAccent = Color(0xFFD0BCFF),
            operators = Color(0xFF4F378B),
            equalsAction = Color(0xFFD0BCFF),
            clearAction = Color(0xFF4A4458),
            background = Color(0xFF141218),
            displaySurface = Color(0xFF1D1B20),
            row2Keys = Color(0xFF2B2930),
            row3Keys = Color(0xFF2B2930),
            numberKeys = Color(0xFF211F26),
            textPrimary = Color(0xFFE6E0E9),
            textSecondary = Color(0xFFCAC4D0),
            borderGlow = Color(0xFFD0BCFF).copy(alpha = 0.3f),
            isDark = true
        )
    )

    // 8. BMW Motorsport
    val BmwMotorsport = ArcThemeDefinition(
        id = "bmw_motorsport",
        name = "BMW Motorsport",
        description = "M-Power racing heritage: Navy Blue, Light Blue, and M-Red on Carbon Anthracite",
        category = "Automotive / Supercar",
        defaultFinish = DesignFinish.GLOW_CONCAVE,
        isDark = true,
        previewDots = listOf(Color(0xFF0D0F13), Color(0xFF0066B1), Color(0xFFE00000)),
        palette = TonalRolePalette(
            primaryAccent = Color(0xFF0066B1),
            operators = Color(0xFF00205B),
            equalsAction = Color(0xFFE00000),
            clearAction = Color(0xFF0066B1),
            background = Color(0xFF0D0F13),
            displaySurface = Color(0xFF141820),
            row2Keys = Color(0xFF1B202B),
            row3Keys = Color(0xFF1B202B),
            numberKeys = Color(0xFF161A23),
            textPrimary = Color(0xFFFFFFFF),
            textSecondary = Color(0xFF9AA7BC),
            borderGlow = Color(0xFF0066B1).copy(alpha = 0.5f),
            isDark = true
        )
    )

    // 9. Porsche Racing
    val PorscheRacing = ArcThemeDefinition(
        id = "porsche_racing",
        name = "Porsche Racing",
        description = "Guards Red, Speed Yellow, and Carrara White on track titanium surfaces",
        category = "Automotive / Supercar",
        defaultFinish = DesignFinish.GLOW_CONCAVE,
        isDark = true,
        previewDots = listOf(Color(0xFF121214), Color(0xFFFEE100), Color(0xFFD5001C)),
        palette = TonalRolePalette(
            primaryAccent = Color(0xFFFEE100),
            operators = Color(0xFF2A2022),
            equalsAction = Color(0xFFD5001C),
            clearAction = Color(0xFFD5001C),
            background = Color(0xFF121214),
            displaySurface = Color(0xFF1A1A1E),
            row2Keys = Color(0xFF24242A),
            row3Keys = Color(0xFF24242A),
            numberKeys = Color(0xFF1E1E24),
            textPrimary = Color(0xFFF4F4F4),
            textSecondary = Color(0xFFA0A0A8),
            borderGlow = Color(0xFFD5001C).copy(alpha = 0.5f),
            isDark = true
        )
    )

    // 10. Koenigsegg Ghost
    val KoenigseggGhost = ArcThemeDefinition(
        id = "koenigsegg_ghost",
        name = "Koenigsegg Ghost",
        description = "Swedish Ghost Squadron forged carbon weave with Jesko apex gold and aero orange",
        category = "Automotive / Supercar",
        defaultFinish = DesignFinish.GLOW_CONCAVE,
        isDark = true,
        previewDots = listOf(Color(0xFF0B0E11), Color(0xFFF5C242), Color(0xFFFF6700)),
        palette = TonalRolePalette(
            primaryAccent = Color(0xFFF5C242),
            operators = Color(0xFF161B22),
            equalsAction = Color(0xFFFF6700),
            clearAction = Color(0xFFD4D8DD),
            background = Color(0xFF0B0E11),
            displaySurface = Color(0xFF12161D),
            row2Keys = Color(0xFF181E26),
            row3Keys = Color(0xFF181E26),
            numberKeys = Color(0xFF141820),
            textPrimary = Color(0xFFF0F6FC),
            textSecondary = Color(0xFF8B949E),
            borderGlow = Color(0xFFF5C242).copy(alpha = 0.65f),
            isDark = true
        )
    )

    // 11. Pagani Huayra
    val PaganiHuayra = ArcThemeDefinition(
        id = "pagani_huayra",
        name = "Pagani Huayra",
        description = "Exposed amber bronze-carbon weave with cyan anodized aeronautic titanium switches",
        category = "Automotive / Supercar",
        defaultFinish = DesignFinish.GLOW_CONCAVE,
        isDark = true,
        previewDots = listOf(Color(0xFF16120E), Color(0xFF00E5FF), Color(0xFFD62828)),
        palette = TonalRolePalette(
            primaryAccent = Color(0xFF00E5FF),
            operators = Color(0xFF261D15),
            equalsAction = Color(0xFFD62828),
            clearAction = Color(0xFFC5A059),
            background = Color(0xFF16120E),
            displaySurface = Color(0xFF1F1A14),
            row2Keys = Color(0xFF2A221B),
            row3Keys = Color(0xFF2A221B),
            numberKeys = Color(0xFF231D17),
            textPrimary = Color(0xFFFFF8F0),
            textSecondary = Color(0xFFC5A059),
            borderGlow = Color(0xFF00E5FF).copy(alpha = 0.6f),
            isDark = true
        )
    )

    // 12. Toyota Gazoo Racing
    val ToyotaGazooRacing = ArcThemeDefinition(
        id = "toyota_gr",
        name = "Toyota GR Racing",
        description = "High-contrast Le Mans cockpit: Gazoo Racing scarlet red, pure white, and obsidian carbon",
        category = "Automotive / Supercar",
        defaultFinish = DesignFinish.GLOW_CONCAVE,
        isDark = true,
        previewDots = listOf(Color(0xFF0A0A0A), Color(0xFFE60012), Color(0xFFFFFFFF)),
        palette = TonalRolePalette(
            primaryAccent = Color(0xFFE60012),
            operators = Color(0xFF1C1C1E),
            equalsAction = Color(0xFFE60012),
            clearAction = Color(0xFF2C2C2E),
            background = Color(0xFF0A0A0A),
            displaySurface = Color(0xFF141416),
            row2Keys = Color(0xFF1E1E22),
            row3Keys = Color(0xFF1E1E22),
            numberKeys = Color(0xFF17171A),
            textPrimary = Color(0xFFFFFFFF),
            textSecondary = Color(0xFFA1A1AA),
            borderGlow = Color(0xFFE60012).copy(alpha = 0.6f),
            isDark = true
        )
    )

    // 13. Bugatti Pur Sang
    val BugattiPurSang = ArcThemeDefinition(
        id = "bugatti_pur_sang",
        name = "Bugatti Pur Sang",
        description = "Tourbillon grand luxury: French Racing Royal Blue, deep sapphire cockpit, and Macaron Red",
        category = "Automotive / Supercar",
        defaultFinish = DesignFinish.GLOW_CONCAVE,
        isDark = true,
        previewDots = listOf(Color(0xFF061124), Color(0xFF0052CC), Color(0xFFEB0029)),
        palette = TonalRolePalette(
            primaryAccent = Color(0xFF0052CC),
            operators = Color(0xFF0A1E3F),
            equalsAction = Color(0xFFEB0029),
            clearAction = Color(0xFFE2E8F0),
            background = Color(0xFF061124),
            displaySurface = Color(0xFF0C1D3A),
            row2Keys = Color(0xFF12274C),
            row3Keys = Color(0xFF12274C),
            numberKeys = Color(0xFF0E2142),
            textPrimary = Color(0xFFF8FAFC),
            textSecondary = Color(0xFF93C5FD),
            borderGlow = Color(0xFF0052CC).copy(alpha = 0.65f),
            isDark = true
        )
    )

    // 14. McLaren Papaya
    val McLarenPapaya = ArcThemeDefinition(
        id = "mclaren_papaya",
        name = "McLaren Papaya",
        description = "Formula 1 aerodynamic cockpit with historic Bruce McLaren Papaya Spark and Cyber Aero Blue",
        category = "Automotive / Supercar",
        defaultFinish = DesignFinish.GLOW_CONCAVE,
        isDark = true,
        previewDots = listOf(Color(0xFF16181C), Color(0xFFFF8000), Color(0xFF00B4D8)),
        palette = TonalRolePalette(
            primaryAccent = Color(0xFFFF8000),
            operators = Color(0xFF22252B),
            equalsAction = Color(0xFFFF8000),
            clearAction = Color(0xFF00B4D8),
            background = Color(0xFF16181C),
            displaySurface = Color(0xFF1C1F24),
            row2Keys = Color(0xFF262B33),
            row3Keys = Color(0xFF262B33),
            numberKeys = Color(0xFF20242B),
            textPrimary = Color(0xFFFFFFFF),
            textSecondary = Color(0xFF94A3B8),
            borderGlow = Color(0xFFFF8000).copy(alpha = 0.6f),
            isDark = true
        )
    )

    // 15. Ferrari Scuderia
    val FerrariScuderia = ArcThemeDefinition(
        id = "ferrari_scuderia",
        name = "Ferrari Scuderia",
        description = "Maranello racing spirit: Rosso Corsa racing red with Giallo Modena dials and Carbon Nero",
        category = "Automotive / Supercar",
        defaultFinish = DesignFinish.GLOW_CONCAVE,
        isDark = true,
        previewDots = listOf(Color(0xFF0D0D0E), Color(0xFFD40000), Color(0xFFFFE600)),
        palette = TonalRolePalette(
            primaryAccent = Color(0xFFFFE600),
            operators = Color(0xFF241516),
            equalsAction = Color(0xFFD40000),
            clearAction = Color(0xFFD40000),
            background = Color(0xFF0D0D0E),
            displaySurface = Color(0xFF161517),
            row2Keys = Color(0xFF221C1D),
            row3Keys = Color(0xFF221C1D),
            numberKeys = Color(0xFF1A1718),
            textPrimary = Color(0xFFFFFFFF),
            textSecondary = Color(0xFFFFE600),
            borderGlow = Color(0xFFD40000).copy(alpha = 0.6f),
            isDark = true
        )
    )

    // 16. Neon Cyberpunk (Phosphor rows with concave radial fade to black)
    val NeonCyberpunk = ArcThemeDefinition(
        id = "neon_cyberpunk",
        name = "Neon Cyberpunk",
        description = "Pitch black AMOLED with vibrant phosphor rows (Green, Orange, Blue) and concave inner fade",
        category = "Cyber & Neon",
        defaultFinish = DesignFinish.GLOW_CONCAVE,
        isDark = true,
        previewDots = listOf(Color(0xFF00E676), Color(0xFFFF6A00), Color(0xFF0055FF)),
        palette = TonalRolePalette(
            primaryAccent = Color(0xFF00E676),
            operators = Color(0xFF141416),
            equalsAction = Color(0xFF26040A),
            clearAction = Color(0xFF200307),
            background = Color(0xFF000000),
            displaySurface = Color(0xFF000000),
            row2Keys = Color(0xFF180A02),
            row3Keys = Color(0xFF020A1E),
            numberKeys = Color(0xFF02160C),
            textPrimary = Color(0xFFFFFFFF),
            textSecondary = Color(0xFF00E676),
            borderGlow = Color(0xFF00E676),
            row1BorderGlow = Color(0xFF00E676),
            row2BorderGlow = Color(0xFFFF6A00),
            row3BorderGlow = Color(0xFF0055FF),
            operatorsBorderGlow = Color(0xFFCBD5E1),
            isDark = true,
            isNeonConcaveGradient = true
        )
    )

    // 17. Neon Monochrome (Pure pitch black with silver ambient halo and crimson accent)
    val NeonMonochrome = ArcThemeDefinition(
        id = "neon_monochrome",
        name = "Neon Monochrome",
        description = "Pitch black AMOLED with ambient silver halo glow and crimson accent from reference",
        category = "Cyber & Neon",
        defaultFinish = DesignFinish.GLOW_CONCAVE,
        isDark = true,
        previewDots = listOf(Color(0xFF000000), Color(0xFFCBD5E1), Color(0xFF881337)),
        palette = TonalRolePalette(
            primaryAccent = Color(0xFFCBD5E1),
            operators = Color(0xFF141416),
            equalsAction = Color(0xFF1C0508),
            clearAction = Color(0xFF141416),
            background = Color(0xFF000000),
            displaySurface = Color(0xFF000000),
            row2Keys = Color(0xFF141416),
            row3Keys = Color(0xFF141416),
            numberKeys = Color(0xFF141416),
            textPrimary = Color(0xFFFFFFFF),
            textSecondary = Color(0xFFCBD5E1),
            borderGlow = Color(0xFFCBD5E1),
            row1BorderGlow = Color(0xFFCBD5E1),
            row2BorderGlow = Color(0xFFCBD5E1),
            row3BorderGlow = Color(0xFFCBD5E1),
            operatorsBorderGlow = Color(0xFFCBD5E1),
            isDark = true,
            isNeonConcaveGradient = true
        )
    )

    // 18. Midnight Indigo
    val MidnightIndigo = ArcThemeDefinition(
        id = "midnight_indigo",
        name = "Midnight Indigo",
        description = "Deep twilight blue with electric violet and starlight silver accents",
        category = "AMOLED & Mineral",
        defaultFinish = DesignFinish.GLOW_CONCAVE,
        isDark = true,
        previewDots = listOf(Color(0xFF090A15), Color(0xFF818CF8), Color(0xFFA855F7)),
        palette = TonalRolePalette(
            primaryAccent = Color(0xFF818CF8),
            operators = Color(0xFF312E81),
            equalsAction = Color(0xFFA855F7),
            clearAction = Color(0xFF4338CA),
            background = Color(0xFF090A15),
            displaySurface = Color(0xFF101225),
            row2Keys = Color(0xFF191C36),
            row3Keys = Color(0xFF191C36),
            numberKeys = Color(0xFF14162B),
            textPrimary = Color(0xFFF5F3FF),
            textSecondary = Color(0xFF9CA3AF),
            borderGlow = Color(0xFF6366F1).copy(alpha = 0.4f),
            isDark = true
        )
    )

    // 19. Dark Emerald
    val DarkEmerald = ArcThemeDefinition(
        id = "dark_emerald",
        name = "Dark Emerald",
        description = "Lush racing green and jade velvet surfaces with bright mint phosphor",
        category = "AMOLED & Mineral",
        defaultFinish = DesignFinish.GLOW_CONCAVE,
        isDark = true,
        previewDots = listOf(Color(0xFF06150F), Color(0xFF34D399), Color(0xFF10B981)),
        palette = TonalRolePalette(
            primaryAccent = Color(0xFF34D399),
            operators = Color(0xFF064E3B),
            equalsAction = Color(0xFF10B981),
            clearAction = Color(0xFF047857),
            background = Color(0xFF06150F),
            displaySurface = Color(0xFF0C2218),
            row2Keys = Color(0xFF133224),
            row3Keys = Color(0xFF133224),
            numberKeys = Color(0xFF0F281D),
            textPrimary = Color(0xFFECFDF5),
            textSecondary = Color(0xFF6EE7B7),
            borderGlow = Color(0xFF10B981).copy(alpha = 0.4f),
            isDark = true
        )
    )

    // 20. Crimson Velvet
    val CrimsonVelvet = ArcThemeDefinition(
        id = "crimson_velvet",
        name = "Crimson Velvet",
        description = "Rich burgundy wine and ruby glow with champagne highlights",
        category = "AMOLED & Mineral",
        defaultFinish = DesignFinish.GLOW_CONCAVE,
        isDark = true,
        previewDots = listOf(Color(0xFF140508), Color(0xFFFB7185), Color(0xFFE11D48)),
        palette = TonalRolePalette(
            primaryAccent = Color(0xFFFB7185),
            operators = Color(0xFF4C0519),
            equalsAction = Color(0xFFE11D48),
            clearAction = Color(0xFF9F1239),
            background = Color(0xFF140508),
            displaySurface = Color(0xFF200A10),
            row2Keys = Color(0xFF2E0F17),
            row3Keys = Color(0xFF2E0F17),
            numberKeys = Color(0xFF250C13),
            textPrimary = Color(0xFFFFF1F2),
            textSecondary = Color(0xFFFDA4AF),
            borderGlow = Color(0xFFE11D48).copy(alpha = 0.4f),
            isDark = true
        )
    )

    // 21. Sunset Coral
    val SunsetCoral = ArcThemeDefinition(
        id = "sunset_coral",
        name = "Sunset Coral",
        description = "Warm glowing gradient of amber, orange, and radiant coral",
        category = "Material 3 Expressive",
        defaultFinish = DesignFinish.GLOW_CONCAVE,
        isDark = true,
        previewDots = listOf(Color(0xFF170C08), Color(0xFFFB923C), Color(0xFFFBBF24)),
        palette = TonalRolePalette(
            primaryAccent = Color(0xFFFB923C),
            operators = Color(0xFF7C2D12),
            equalsAction = Color(0xFFFBBF24),
            clearAction = Color(0xFFC2410C),
            background = Color(0xFF170C08),
            displaySurface = Color(0xFF23140D),
            row2Keys = Color(0xFF331D14),
            row3Keys = Color(0xFF331D14),
            numberKeys = Color(0xFF291710),
            textPrimary = Color(0xFFFFF7ED),
            textSecondary = Color(0xFFFDBA74),
            borderGlow = Color(0xFFF97316).copy(alpha = 0.4f),
            isDark = true
        )
    )

    // 22. Racing Gold
    val RacingGold = ArcThemeDefinition(
        id = "racing_gold",
        name = "Racing Gold",
        description = "Striking luxury gold and cyber amber on obsidian matte",
        category = "Automotive / Supercar",
        defaultFinish = DesignFinish.GLOW_CONCAVE,
        isDark = true,
        previewDots = listOf(Color(0xFF100E08), Color(0xFFFCD34D), Color(0xFFF59E0B)),
        palette = TonalRolePalette(
            primaryAccent = Color(0xFFFCD34D),
            operators = Color(0xFF451A03),
            equalsAction = Color(0xFFF59E0B),
            clearAction = Color(0xFF78350F),
            background = Color(0xFF100E08),
            displaySurface = Color(0xFF1A170E),
            row2Keys = Color(0xFF262215),
            row3Keys = Color(0xFF262215),
            numberKeys = Color(0xFF1F1C11),
            textPrimary = Color(0xFFFEF3C7),
            textSecondary = Color(0xFFFDE68A),
            borderGlow = Color(0xFFF59E0B).copy(alpha = 0.4f),
            isDark = true
        )
    )

    val allPresets: List<ArcThemeDefinition> = listOf(
        BotanicalSage,
        ArcShiftChromatic,
        AmoledStealth,
        WireframeDark,
        WireframeLight,
        MinimalLight,
        Material3Expressive,
        BmwMotorsport,
        PorscheRacing,
        KoenigseggGhost,
        PaganiHuayra,
        ToyotaGazooRacing,
        BugattiPurSang,
        McLarenPapaya,
        FerrariScuderia,
        NeonCyberpunk,
        NeonMonochrome,
        MidnightIndigo,
        DarkEmerald,
        CrimsonVelvet,
        SunsetCoral,
        RacingGold
    )

    fun getTheme(id: String, customSeedHex: Long, isDark: Boolean = true): ArcThemeDefinition {
        if (id == "custom_dynamic") {
            val seedColor = Color(customSeedHex)
            val derived = ColorHarmonizer.deriveHarmonicPalette(seedColor, isDark)
            return ArcThemeDefinition(
                id = "custom_dynamic",
                name = "Custom Dynamic Palette",
                description = "Custom generated harmonic palette from your chosen seed color",
                category = "Custom",
                defaultFinish = DesignFinish.GLOW_CONCAVE,
                isDark = isDark,
                previewDots = listOf(derived.background, derived.primaryAccent, derived.equalsAction),
                palette = derived
            )
        }

        // Check if matching wallpaper preset
        val wp = WallpaperPresets.find { it.id == id || "wp_${it.id}" == id }
        if (wp != null) {
            val derived = ColorHarmonizer.deriveHarmonicPalette(wp.color, isDark)
            return ArcThemeDefinition(
                id = wp.id,
                name = wp.name,
                description = "Harmonized Material 3 palette extracted from ${wp.name}",
                category = "Wallpaper Colors M3",
                defaultFinish = DesignFinish.M3_FLAT,
                isDark = isDark,
                previewDots = listOf(derived.background, derived.primaryAccent, derived.equalsAction),
                palette = derived
            )
        }

        if (id == "m3_expressive") {
            return if (isDark) {
                Material3Expressive
            } else {
                ArcThemeDefinition(
                    id = "m3_expressive",
                    name = "Material 3 Expressive",
                    description = "Dynamic Google Material You theme that adapts to your phone wallpaper and system dark/light mode",
                    category = "Material 3 Expressive",
                    defaultFinish = DesignFinish.M3_FLAT,
                    isDark = false,
                    previewDots = listOf(Color(0xFFFEF7FF), Color(0xFF6750A4), Color(0xFFE8DEF8)),
                    palette = TonalRolePalette(
                        primaryAccent = Color(0xFF6750A4),
                        operators = Color(0xFFE8DEF8),
                        equalsAction = Color(0xFF6750A4),
                        clearAction = Color(0xFFF9DEDC),
                        background = Color(0xFFFEF7FF),
                        displaySurface = Color(0xFFF3EDF7),
                        row2Keys = Color(0xFFE8DEF8),
                        row3Keys = Color(0xFFDFE0EB),
                        numberKeys = Color(0xFFE6E0E9),
                        textPrimary = Color(0xFF1D1B20),
                        textSecondary = Color(0xFF49454F),
                        borderGlow = Color(0xFFCAC4D0),
                        isDark = false
                    )
                )
            }
        }

        return allPresets.firstOrNull { it.id == id } ?: Material3Expressive
    }
}
