package com.example.islamicdailycompanion

data class Surah(
    val number: Int,
    val name: String,
    val transliteration: String,
    val type: String,
    val totalVerses: Int,
    val ayahs: List<Ayah>
)

data class Ayah(
    val number: Int,
    val arabic: String
)