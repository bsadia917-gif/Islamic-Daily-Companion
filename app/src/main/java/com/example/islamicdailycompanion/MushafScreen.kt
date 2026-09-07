package com.example.islamicdailycompanion

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MushafScreen() {

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { 604 }
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { pageIndex ->

            val pageNumber = pageIndex + 1

            val pageUrl = remember(pageNumber) {
                "https://www.mp3quran.net/api/quran_pages_svg/%03d.svg"
                    .format(pageNumber)
            }

            AsyncImage(
                model = pageUrl,
                contentDescription = "Mushaf Page $pageNumber",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp)
        ) {

            Text(
                text = "${pagerState.currentPage + 1} / 604",
                fontSize = 14.sp,
                color = Color.DarkGray
            )
        }
    }
}