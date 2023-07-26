package com.example.spotifymusic

import android.os.Bundle
import android.view.animation.OvershootInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.spotifymusic.ui.theme.SpotifyMusicTheme
import kotlinx.coroutines.delay

class AnimatedHomeScreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SpotifyMusicTheme {
//                SplashScreen()

            }
        }
    }
}
//@Composable
//fun SplashScreen(){
//
//    val scale=remember{
//            Animatable(0f)
//        }
//        LaunchedEffect(key1 = true, block = {
//            scale.animateTo(
//                targetValue = 0.3f,
//                animationSpec = tween(
//                    durationMillis = 500,
//                    easing = {
//                        OvershootInterpolator(2f).getInterpolation(it)
//                    }
//                )
//            )
//        })
//    Splash(scale)
//    }
//@Composable
//fun Splash(scale: Animatable<Float, AnimationVector1D>) {
//
//    delay(5000)
//    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
//        Image(
//            painterResource(id = R.drawable.baseline_music_note_24)
//            ,contentDescription = null,
//
//            Modifier.size(200.dp).alpha(scale.value)
//        )
//    }
//
//}
//
//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    SpotifyMusicTheme {
//        Greeting("Android")
//    }
//}