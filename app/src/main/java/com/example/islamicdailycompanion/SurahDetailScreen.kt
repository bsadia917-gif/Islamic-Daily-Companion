package com.example.islamicdailycompanion

import android.text.Html
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.regex.Pattern

private val IndoPakFont = FontFamily(
    Font(
        resId = R.font.indopak_nastaleeq,
        weight = FontWeight.Normal
    )
)

private val MushafBackground = Color(0xFFFFFCF2)

private val DarkGreen = Color(0xFF1B5E20)

private val ArabicTextColor = Color(0xFF202020)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SurahDetailScreen(
    surahName: String,
    onBackClick: () -> Unit
) {

    val context =
        androidx.compose.ui.platform.LocalContext.current

    val surahs = remember {
        QuranRepository.loadQuran(context)
    }

    val surah = remember(
        surahName,
        surahs
    ) {
        surahs.find {
            it.name == surahName
        }
    }

    if (surah == null) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "Surah not found"
            )
        }

        return
    }

    Scaffold(

        containerColor = MushafBackground,

        topBar = {

            TopAppBar(

                title = {

                    Text(
                        text = surah.name,
                        fontFamily = IndoPakFont,
                        fontSize = 25.sp,
                        color = DarkGreen
                    )
                },

                navigationIcon = {

                    IconButton(
                        onClick = onBackClick
                    ) {

                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }

    ) { paddingValues ->

        LazyColumn(

            modifier = Modifier
                .fillMaxSize()
                .background(
                    MushafBackground
                )
                .padding(paddingValues),

            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 20.dp,
                bottom = 40.dp
            ),

            verticalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            /*
             * =========================
             * SURAH HEADER
             * =========================
             */

            item {

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment =
                        Alignment.CenterHorizontally
                ) {

                    Text(
                        text = surah.name,
                        fontFamily = IndoPakFont,
                        fontSize = 38.sp,
                        color = DarkGreen,
                        textAlign = TextAlign.Center
                    )

                    Spacer(
                        modifier = Modifier.height(2.dp)
                    )

                    Text(
                        text = surah.transliteration,
                        fontSize = 16.sp,
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text = "${surah.totalVerses} Ayahs",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    Spacer(
                        modifier = Modifier.height(18.dp)
                    )

                    /*
                     * Decorative line
                     */
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(
                                Color(0xFFD8D1BE)
                            )
                    )

                    Spacer(
                        modifier = Modifier.height(18.dp)
                    )

                    /*
                     * BISMILLAH
                     *
                     * Surah 9 mein Bismillah nahi.
                     *
                     * Surah 1 mein JSON ke andar
                     * Bismillah already ayah 1 ka
                     * part hai, is liye duplicate nahi
                     * dikhayenge.
                     */
                    if (
                        surah.number != 9 &&
                        surah.number != 1
                    ) {

                        Text(
                            text =
                                "بِسْمِ اللَّهِ الرَّحْمَنِ الرَّحِيمِ",

                            fontFamily = IndoPakFont,

                            fontSize = 32.sp,

                            color = DarkGreen,

                            textAlign =
                                TextAlign.Center,

                            modifier =
                                Modifier.fillMaxWidth()
                        )

                        Spacer(
                            modifier =
                                Modifier.height(20.dp)
                        )
                    }
                }
            }

            /*
             * =========================
             * AYAT
             * =========================
             */

            items(
                items = surah.ayahs,
                key = {
                    it.number
                }
            ) { ayah ->

                MushafAyah(
                    ayah = ayah
                )
            }

            /*
             * =========================
             * END OF SURAH
             * =========================
             */

            item {

                Spacer(
                    modifier =
                        Modifier.height(18.dp)
                )

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(
                        text = "۞",
                        fontFamily = IndoPakFont,
                        fontSize = 30.sp,
                        color = DarkGreen
                    )
                }
            }
        }
    }
}


/*
 * ========================================
 * SINGLE AYAH
 * ========================================
 */

@Composable
private fun MushafAyah(
    ayah: Ayah
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 7.dp
            )
    ) {

        /*
         * Arabic Tajweed Text
         */
        Text(
            text = parseTajweed(
                ayah.arabic
            ),

            fontFamily = IndoPakFont,

            fontSize = 31.sp,

            lineHeight = 62.sp,

            color = ArabicTextColor,

            textAlign = TextAlign.Right,

            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        /*
         * Ayah Number
         */
        Text(
            text = "۝ ${ayah.number}",

            fontFamily = IndoPakFont,

            fontSize = 18.sp,

            color = DarkGreen,

            textAlign = TextAlign.Center,

            modifier = Modifier.fillMaxWidth()
        )
    }
}


/*
 * ========================================
 * TAJWEED PARSER
 * ========================================
 */

private fun parseTajweed(
    html: String
): AnnotatedString {

    return buildAnnotatedString {

        /*
         * Ayah number jo JSON ke andar:
         *
         * <span class=end>١</span>
         *
         * hota hai usko remove karenge.
         */
        val cleanedHtml =
            html.replace(
                Regex(
                    "<span\\s+class=end>.*?</span>"
                ),
                ""
            )

        val pattern =
            Pattern.compile(
                "<tajweed\\s+class=([^>]+)>(.*?)</tajweed>",
                Pattern.DOTALL
            )

        val matcher =
            pattern.matcher(
                cleanedHtml
            )

        var lastEnd = 0

        while (matcher.find()) {

            /*
             * Normal text
             */
            if (
                matcher.start() > lastEnd
            ) {

                val normalText =
                    cleanedHtml.substring(
                        lastEnd,
                        matcher.start()
                    )

                append(
                    cleanHtml(
                        normalText
                    )
                )
            }

            /*
             * Tajweed class
             */
            val rule =
                matcher.group(1)
                    ?.trim()
                    ?.lowercase()
                    ?: ""

            val tajweedText =
                matcher.group(2)
                    ?: ""

            val cleanText =
                cleanHtml(
                    tajweedText
                )

            pushStyle(
                SpanStyle(
                    color = tajweedColor(
                        rule
                    )
                )
            )

            append(
                cleanText
            )

            pop()

            lastEnd =
                matcher.end()
        }

        /*
         * Remaining text
         */
        if (
            lastEnd < cleanedHtml.length
        ) {

            val remaining =
                cleanedHtml.substring(
                    lastEnd
                )

            append(
                cleanHtml(
                    remaining
                )
            )
        }
    }
}


/*
 * ========================================
 * CLEAN HTML
 * ========================================
 */

private fun cleanHtml(
    text: String
): String {

    return Html
        .fromHtml(
            text,
            Html.FROM_HTML_MODE_LEGACY
        )
        .toString()
}


/*
 * ========================================
 * TAJWEED COLORS
 * ========================================
 */

private fun tajweedColor(
    rule: String
): Color {

    return when {

        /*
         * Qalqalah
         */
        rule.contains(
            "qalaqah"
        ) ->
            Color(0xFF9C27B0)

        /*
         * Ghunnah
         */
        rule.contains(
            "ghunnah"
        ) ->
            Color(0xFFE91E63)

        /*
         * Idgham
         */
        rule.contains(
            "idgham"
        ) ->
            Color(0xFF1976D2)

        /*
         * Ikhfa
         */
        rule.contains(
            "ikhfa"
        ) ->
            Color(0xFFFF9800)

        /*
         * Iqlab
         */
        rule.contains(
            "iqlab"
        ) ->
            Color(0xFF43A047)

        /*
         * Madd
         */
        rule.contains(
            "madda"
        ) ->
            Color(0xFF00897B)

        /*
         * Laam Shamsiyah
         */
        rule.contains(
            "laam_shamsiyah"
        ) ->
            Color(0xFF43A047)

        /*
         * Ham Wasl
         */
        rule.contains(
            "ham_wasl"
        ) ->
            Color(0xFF43A047)

        /*
         * Silent
         */
        rule.contains(
            "silent"
        ) ->
            Color(0xFF757575)

        /*
         * Default Arabic
         */
        else ->
            ArabicTextColor
    }
}