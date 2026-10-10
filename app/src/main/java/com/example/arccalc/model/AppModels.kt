package com.example.arccalc.model

enum class AngleMode(val label: String) {
    DEG("DEG"),
    RAD("RAD"),
    GRAD("GRAD")
}

enum class FontSizeScale(val label: String, val scale: Float, val percent: String) {
    SMALL("Small", 0.85f, "85%"),
    NORMAL("Normal", 1.0f, "100%"),
    LARGE("Large", 1.15f, "115%"),
    XLARGE("X-Large", 1.30f, "130%")
}

enum class TypographyStyle(val label: String, val displayName: String, val category: String) {
    DOT_MATRIX("Dot Matrix", "123 Dot Matrix", "Retro 8-Bit"),
    SPACE_GEO("Space Geo", "123 Space Geo", "Sci-Fi"),
    TECH_MONO("Tech Mono", "123 Tech Mono", "Terminal"),
    CHAKRA("Chakra Petch", "123 Chakra", "Industrial"),
    FIRA_CODE("Fira Code", "123 Fira Code", "Developer"),
    MODERN_SANS("Modern Sans", "123 Modern Sans", "Material 3"),
    SYNE_DISPLAY("Syne Art", "123 Syne Art", "Avant-Garde"),
    BEBAS_POSTER("Bebas Poster", "123 Bebas Poster", "Condensed"),
    CINZEL_SERIF("Cinzel Serif", "123 Cinzel", "Classic Serif"),
    NOVA_SQUARE("Nova Square", "123 Nova Square", "Cyber Square")
}

enum class DesignFinish(val label: String, val subtitle: String) {
    GLOW_CONCAVE("Tactile Phosphor", "Solid Matte with Border"),
    M3_FLAT("Material 3 Flat", "Velvet Solid Matte"),
    WIREFRAME("Wireframe", "Blueprint Grid")
}

enum class DecimalPrecision(val label: String, val digits: Int) {
    AUTO("Auto", -1),
    TWO("2 Digits", 2),
    FOUR("4 Digits", 4),
    SIX("6 Digits", 6),
    EIGHT("8 Digits", 8),
    TEN("10 Digits", 10)
}

enum class ScientificNotation(val label: String) {
    AUTO("Auto (≥10⁹ or ≤10⁻⁴)"),
    ALWAYS("Always Scientific"),
    ENGINEERING("Engineering")
}
