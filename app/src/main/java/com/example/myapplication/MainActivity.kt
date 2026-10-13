package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bumptech.glide.Glide
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ImageLoadingScreen()
                }
            }
        }
    }
}

@Composable
fun ImageLoadingScreen() {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Валидный URL
    val validUrl = "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png"
    // Невалидный URL
    val invalidUrl = "https://invalid-url-example.com/image.jpg"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ---------- ПУНКТ 4 (часть 1): Загрузка из ресурса с помощью Coil ----------
        Text(text = "1. Из ресурса (Coil)", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Из ресурса",
            modifier = Modifier.size(150.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))

        // ---------- ПУНКТ 4 (часть 2): Загрузка по валидному URL с placeholder ----------
        Text(text = "2. По валидному URL (Coil)", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(validUrl)
                .crossfade(true)
                .listener(
                    onSuccess = { _, _ -> Log.d("IMAGE_LOAD", "Coil УСПЕХ: $validUrl") },
                    onError = { _, result -> Log.e("IMAGE_LOAD", "Coil ОШИБКА: ${result.throwable.message}") }
                )
                .build(),
            placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
            error = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Валидный URL",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))

        // ---------- ПУНКТ 5: Загрузка по НЕвалидному URL с ошибкой (Coil) ----------
        Text(text = "3. По НЕвалидному URL (Coil) + ошибка", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(invalidUrl)
                .crossfade(true)
                .listener(
                    onSuccess = { _, _ -> Log.d("IMAGE_LOAD", "Coil УСПЕХ (невалидный): $invalidUrl") },
                    onError = { _, result -> Log.e("IMAGE_LOAD", "Coil ОШИБКА (невалидный): ${result.throwable.message}") }
                )
                .build(),
            placeholder = painterResource(id = R.drawable.ic_launcher_foreground),
            error = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Невалидный URL",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
        Spacer(modifier = Modifier.height(24.dp))

        // ---------- Дополнительно: Загрузка по валидному URL с помощью Glide ----------
        Text(text = "4. По валидному URL (Glide)", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        AndroidView(
            factory = { ctx ->
                ImageView(ctx).apply {
                    scaleType = ImageView.ScaleType.CENTER_CROP
                }
            },
            update = { imageView ->
                Glide.with(context)
                    .load(validUrl)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_background)
                    .into(imageView)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        )
    }
}