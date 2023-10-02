package com.example.testgame

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.testgame.ui.theme.TestGameTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TestGameTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    RestartGame()
                }
            }
        }
    }
}

@Composable
fun LockScreenOrientation(orientation: Int) {
    val context = LocalContext.current
    DisposableEffect(orientation) {
        val activity = context.findActivity() ?: return@DisposableEffect onDispose {}
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation
        onDispose {
            // restore original orientation when view disappears
            activity.requestedOrientation = originalOrientation
        }
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}


@Composable
fun RestartGame() {
    val flag by remember {
        mutableStateOf(true)
    }
    val healCount by remember {
        mutableStateOf(0)
    }
    val player by remember {
        mutableStateOf(Player((1..30).random(), (1..30).random(), 100))
    }
    val monster by remember {
        mutableStateOf(Monster((1..30).random(), (1..30).random(), 100))

    }

    Game(player, monster, flag, healCount)
}

@Composable
fun Game(playerIn: Player, monsterIn: Monster, flagIn: Boolean, healCountIn: Int) {

    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
    var flag by remember {
        mutableStateOf(flagIn)
    }
    var healCount by remember {
        mutableStateOf(healCountIn)
    }
    var healFlag by remember {
        mutableStateOf(false)
    }
    val player by remember {
        mutableStateOf(playerIn)
    }
    val monster by remember {
        mutableStateOf(monsterIn)
    }
    var reload by remember {
        mutableStateOf(false)
    }
    var playerDices by remember {
        mutableStateOf(listOf<Int>())
    }
//    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
    Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.SpaceBetween) {
        Button(onClick = { healFlag = true }) {

            Image(
                painter = painterResource(
                    id = R.drawable.baseline_monitor_heart_24
                ), contentDescription = "Image", modifier = Modifier.size(20.dp)
            )
            if (healCount < 4 && healFlag) {
                healCount += 1
                val context = LocalContext.current
                Toast.makeText(
                    context,
                    "Игрок вылечен!Аптечек: " + (4 - healCount).toString(),
                    Toast.LENGTH_SHORT
                ).show()
            } else if (healFlag) {
                val context = LocalContext.current
                Toast.makeText(
                    context,
                    "Вы использовали все возможные попытки вылечить игрока",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        Button(onClick = { reload = true }) {
            Image(
                painter = painterResource(
                    id = R.drawable.baseline_refresh_24
                ), contentDescription = "Image", modifier = Modifier.size(20.dp)
            )
        }

    }

    Row(horizontalArrangement = Arrangement.SpaceAround, verticalAlignment = Alignment.Bottom) {
        for (i in playerDices.indices) {
            if (playerDices[i] == 1) {
                Image(
                    painter = painterResource(
                        id = R.drawable.d1
                    ), contentDescription = "Image", modifier = Modifier.size(20.dp)

                )
            }
            if (playerDices[i] == 2) {
                Image(
                    painter = painterResource(
                        id = R.drawable.d2
                    ), contentDescription = "Image", modifier = Modifier.size(20.dp)
                )
            }
            if (playerDices[i] == 3) {
                Image(
                    painter = painterResource(
                        id = R.drawable.d3
                    ), contentDescription = "Image", modifier = Modifier.size(20.dp)
                )
            }
            if (playerDices[i] == 4) {
                Image(
                    painter = painterResource(
                        id = R.drawable.d4
                    ), contentDescription = "Image", modifier = Modifier.size(20.dp)
                )
            }
            if (playerDices[i] == 5) {
                Image(
                    painter = painterResource(
                        id = R.drawable.d5
                    ), contentDescription = "Image", modifier = Modifier.size(20.dp)
                )
            }
            if (playerDices[i] == 6)
                Image(
                    painter = painterResource(
                        id = R.drawable.d6
                    ), contentDescription = "Image", modifier = Modifier.size(20.dp)
                )

        }

    }

    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.Start) {


        Image(
            painter = painterResource(
                id = R.drawable.player1
            ), contentDescription = "Image", modifier = Modifier.size(200.dp)
        )

        LinearProgressIndicator(
            modifier = Modifier.size(width = 175.dp, height = 10.dp),
            color = Color.Green,
            trackColor = Color.Red,
            progress = (player.health.toFloat() / player.maxHealth.toFloat())
        )
    }
    Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.End) {
        Image(
            painter = painterResource(
                id = R.drawable.monster
            ), contentDescription = "Image", modifier = Modifier.size(200.dp)
        )
        LinearProgressIndicator(
            modifier = Modifier.size(width = 175.dp, height = 10.dp),
            color = Color.Green,
            trackColor = Color.Red,
            progress = (monster.health.toFloat() / monster.maxHealth.toFloat())
        )

    }



    Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.SpaceAround) {
        Button(
            onClick = {
                if (flag) {
                    player.attack(monster)
                    flag = false
                    playerDices = player.dices
                } else {
                    monster.attack(player)
                    flag = true
                    playerDices = monster.dices
                }
            },
            modifier = Modifier
                .size(width = 300.dp, height = 50.dp)
                .padding(bottom = 19.dp),
            colors = ButtonDefaults.buttonColors(Color(20, 255, 0)
        )
        ) {
            if (flag) Text(text = "Ваш ход") else Text(text = "Ход монстра")
            if (player.isDead(player.health)) {
                val context = LocalContext.current
                Toast.makeText(
                    context,
                    "Монстр победил!",
                    Toast.LENGTH_SHORT
                ).show()
                reload = true
            } else if (monster.isDead(monster.health)) {
                val context = LocalContext.current
                Toast.makeText(
                    context,
                    "Игрок победил!",
                    Toast.LENGTH_SHORT
                ).show()
                reload = true
            }
        }


    }

    if (reload) {
        playerDices = mutableListOf()
        RestartGame()
    }
    if (healFlag) {
        player.heal()
        healFlag = false
    }

}


//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    TestGameTheme {
//        Game()
//    }
//}