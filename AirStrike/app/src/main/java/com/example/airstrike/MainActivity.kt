package com.example.airstrike

import GameView
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : AppCompatActivity(), GameTask {
    private lateinit var rootLayout: LinearLayout
    private lateinit var startBtn: Button
    private lateinit var mGameView: GameView
    private lateinit var scoreTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startBtn = findViewById(R.id.startBtn)
        rootLayout = findViewById(R.id.rootLayout)
        scoreTextView = findViewById(R.id.score)

        startBtn.setOnClickListener {
            try {
                mGameView = GameView(this, this)
                mGameView.setBackgroundResource(R.drawable.sky)
                rootLayout.addView(mGameView)
                startBtn.visibility = View.GONE
                scoreTextView.visibility = View.GONE
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun closeGame(score: Int) {
        scoreTextView.text = "Score: $score"
        rootLayout.removeView(mGameView)
        startBtn.visibility = View.VISIBLE
        scoreTextView.visibility = View.VISIBLE
    }

    fun navigateToDestinationActivity(view: View) {
        val intent = Intent(this, DestinationActivity::class.java)
        startActivity(intent)
    }
}