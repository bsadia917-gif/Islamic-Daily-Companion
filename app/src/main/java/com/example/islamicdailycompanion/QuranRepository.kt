package com.example.islamicdailycompanion

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject

object QuranRepository {

    private data class QuranLine(
        val surahNumber: Int,
        val ayahNumber: Int,
        val arabic: String
    )

    fun loadQuran(context: Context): List<Surah> {

        return try {

            val jsonText = context.assets
                .open("tajweedquran.json")
                .bufferedReader(Charsets.UTF_8)
                .use { it.readText() }

            val root = JSONObject(jsonText)

            // JSON ke andar "ayahs" array dhoondna
            val ayahsArray = findAyahsArray(root)
                ?: return emptyList()

            val lines = mutableListOf<QuranLine>()

            for (i in 0 until ayahsArray.length()) {

                val ayahObject =
                    ayahsArray.getJSONObject(i)

                val verseKey =
                    ayahObject.optString("verse_key")

                val text =
                    ayahObject.optString("text")

                if (verseKey.isNotEmpty() && text.isNotEmpty()) {

                    val parts =
                        verseKey.split(":")

                    if (parts.size == 2) {

                        val surahNumber =
                            parts[0].toIntOrNull()

                        val ayahNumber =
                            parts[1].toIntOrNull()

                        if (
                            surahNumber != null &&
                            ayahNumber != null
                        ) {

                            lines.add(
                                QuranLine(
                                    surahNumber = surahNumber,
                                    ayahNumber = ayahNumber,
                                    arabic = text
                                )
                            )
                        }
                    }
                }
            }

            createSurahs(lines)

        } catch (e: Exception) {

            e.printStackTrace()
            emptyList()
        }
    }

    /*
     * JSON ke kisi bhi level par "ayahs"
     * array ho to usay automatically find karega.
     */
    private fun findAyahsArray(
        jsonObject: JSONObject
    ): JSONArray? {

        if (jsonObject.has("ayahs")) {

            val array =
                jsonObject.optJSONArray("ayahs")

            if (array != null) {
                return array
            }
        }

        val keys =
            jsonObject.keys()

        while (keys.hasNext()) {

            val key = keys.next()

            when (val value = jsonObject.opt(key)) {

                is JSONObject -> {

                    val result =
                        findAyahsArray(value)

                    if (result != null) {
                        return result
                    }
                }

                is JSONArray -> {

                    for (i in 0 until value.length()) {

                        val item =
                            value.opt(i)

                        if (item is JSONObject) {

                            val result =
                                findAyahsArray(item)

                            if (result != null) {
                                return result
                            }
                        }
                    }
                }
            }
        }

        return null
    }

    private fun createSurahs(
        lines: List<QuranLine>
    ): List<Surah> {

        val surahNames = listOf(
            "الفاتحة", "البقرة", "آل عمران", "النساء",
            "المائدة", "الأنعام", "الأعراف", "الأنفال",
            "التوبة", "يونس", "هود", "يوسف", "الرعد",
            "إبراهيم", "الحجر", "النحل", "الإسراء",
            "الكهف", "مريم", "طه", "الأنبياء", "الحج",
            "المؤمنون", "النور", "الفرقان", "الشعراء",
            "النمل", "القصص", "العنكبوت", "الروم",
            "لقمان", "السجدة", "الأحزاب", "سبإ", "فاطر",
            "يس", "الصافات", "ص", "الزمر", "غافر",
            "فصلت", "الشورى", "الزخرف", "الدخان",
            "الجاثية", "الأحقاف", "محمد", "الفتح",
            "الحجرات", "ق", "الذاريات", "الطور", "النجم",
            "القمر", "الرحمن", "الواقعة", "الحديد",
            "المجادلة", "الحشر", "الممتحنة", "الصف",
            "الجمعة", "المنافقون", "التغابن", "الطلاق",
            "التحريم", "الملك", "القلم", "الحاقة", "المعارج",
            "نوح", "الجن", "المزمل", "المدثر", "القيامة",
            "الإنسان", "المرسلات", "النبأ", "النازعات",
            "عبس", "التكوير", "الإنفطار", "المطففين",
            "الإنشقاق", "البروج", "الطارق", "الأعلى",
            "الغاشية", "الفجر", "البلد", "الشمس", "الليل",
            "الضحى", "الشرح", "التين", "العلق", "القدر",
            "البينة", "الزلزلة", "العاديات", "القارعة",
            "التكاثر", "العصر", "الهمزة", "الفيل", "قريش",
            "الماعون", "الكوثر", "الكافرون", "النصر",
            "المسد", "الإخلاص", "الفلق", "الناس"
        )

        val transliterations = listOf(
            "Al-Fatihah", "Al-Baqarah", "Aal-E-Imran",
            "An-Nisa", "Al-Maidah", "Al-Anam", "Al-Araf",
            "Al-Anfal", "At-Tawbah", "Yunus", "Hud",
            "Yusuf", "Ar-Rad", "Ibrahim", "Al-Hijr",
            "An-Nahl", "Al-Isra", "Al-Kahf", "Maryam",
            "Ta-Ha", "Al-Anbiya", "Al-Hajj", "Al-Muminun",
            "An-Nur", "Al-Furqan", "Ash-Shuara",
            "An-Naml", "Al-Qasas", "Al-Ankabut", "Ar-Rum",
            "Luqman", "As-Sajdah", "Al-Ahzab", "Saba",
            "Fatir", "Ya-Sin", "As-Saffat", "Sad",
            "Az-Zumar", "Ghafir", "Fussilat", "Ash-Shura",
            "Az-Zukhruf", "Ad-Dukhan", "Al-Jathiyah",
            "Al-Ahqaf", "Muhammad", "Al-Fath", "Al-Hujurat",
            "Qaf", "Adh-Dhariyat", "At-Tur", "An-Najm",
            "Al-Qamar", "Ar-Rahman", "Al-Waqiah",
            "Al-Hadid", "Al-Mujadilah", "Al-Hashr",
            "Al-Mumtahanah", "As-Saff", "Al-Jumuah",
            "Al-Munafiqun", "At-Taghabun", "At-Talaq",
            "At-Tahrim", "Al-Mulk", "Al-Qalam", "Al-Haqqah",
            "Al-Maarij", "Nuh", "Al-Jinn", "Al-Muzzammil",
            "Al-Muddaththir", "Al-Qiyamah", "Al-Insan",
            "Al-Mursalat", "An-Naba", "An-Naziat", "Abasa",
            "At-Takwir", "Al-Infitar", "Al-Mutaffifin",
            "Al-Inshiqaq", "Al-Buruj", "At-Tariq", "Al-Ala",
            "Al-Ghashiyah", "Al-Fajr", "Al-Balad", "Ash-Shams",
            "Al-Layl", "Ad-Duha", "Ash-Sharh", "At-Tin",
            "Al-Alaq", "Al-Qadr", "Al-Bayyinah",
            "Az-Zalzalah", "Al-Adiyat", "Al-Qariah",
            "At-Takathur", "Al-Asr", "Al-Humazah", "Al-Fil",
            "Quraysh", "Al-Maun", "Al-Kawthar", "Al-Kafirun",
            "An-Nasr", "Al-Masad", "Al-Ikhlas", "Al-Falaq",
            "An-Nas"
        )

        val result = mutableListOf<Surah>()

        for (surahNumber in 1..114) {

            val surahLines =
                lines.filter {
                    it.surahNumber == surahNumber
                }

            if (surahLines.isNotEmpty()) {

                val ayahs =
                    surahLines.map {
                        Ayah(
                            number = it.ayahNumber,
                            arabic = it.arabic
                        )
                    }

                result.add(
                    Surah(
                        number = surahNumber,
                        name = surahNames[surahNumber - 1],
                        transliteration =
                            transliterations[surahNumber - 1],
                        type = "Quran",
                        totalVerses = ayahs.size,
                        ayahs = ayahs
                    )
                )
            }
        }

        return result
    }
}