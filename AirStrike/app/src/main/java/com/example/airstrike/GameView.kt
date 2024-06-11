import android.content.Context
import android.content.SharedPreferences
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import com.example.airstrike.GameTask
import com.example.airstrike.R

class GameView(context: Context, var gameTask: GameTask) : View(context) {
    private var myPaint: Paint? = null
    private var speed = 1
    private var time = 0
    private var score = 0
    private var highScore = 0 // Variable to store the highest score
    private var myPlanePosition = 0
    private val missile = ArrayList<HashMap<String, Any>>()

    var viewWidth = 0
    var viewHeight = 0

    init {
        myPaint = Paint()

        // Retrieve the high score from SharedPreferences when the GameView is initialized
        val sharedPreferences: SharedPreferences = context.getSharedPreferences("game_preferences", Context.MODE_PRIVATE)
        highScore = sharedPreferences.getInt("high_score", 0)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        viewWidth = this.measuredWidth
        viewHeight = this.measuredHeight

        if (time % 700 < 10 + speed) {
            val map = HashMap<String, Any>()
            map["lane"] = (0..2).random()
            map["startTime"] = time
            missile.add(map)
        }
        time = time + 10 + speed
        val myPlaneWidth = viewWidth / 4
        val myPlaneHeight = myPlaneWidth + 10
        val myPlaneX = myPlanePosition * viewWidth / 3 + viewWidth / 15 + 25
        val myPlaneY = viewHeight - 2 - myPlaneHeight

        // Draw the player's plane (car1)
        val car1 = resources.getDrawable(R.drawable.plane, null)
        car1.setBounds(
            myPlaneX,
            myPlaneY,
            myPlaneX + myPlaneWidth - 50, // Adjusted width to fit within lane
            myPlaneY + myPlaneHeight
        )
        car1.draw(canvas)

        // Update high score if the current score is higher
        if (score > highScore) {
            highScore = score

            // Save the updated high score to SharedPreferences
            val sharedPreferences: SharedPreferences = context.getSharedPreferences("game_preferences", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putInt("high_score", highScore)
            editor.apply()
        }

        myPaint!!.color = Color.GREEN

        for (i in missile.indices) {
            try {
                val carX = missile[i]["lane"] as Int * viewWidth / 3 + viewWidth / 15
                val carY = time - missile[i]["startTime"] as Int
                val carWidth = viewWidth / 5
                val carHeight = carWidth + 10

                // Draw other cars (car2)
                val car2 = resources.getDrawable(R.drawable.missile, null)
                car2.setBounds(
                    carX + 25, carY - carHeight, carX + carWidth - 25, carY
                )
                car2.draw(canvas)

                if (missile[i]["lane"] as Int == myPlanePosition) {
                    if (carY > viewHeight - 2 - carHeight && carY < viewHeight - 2) {
                        gameTask.closeGame(score)
                    }
                }
                if (carY > viewHeight + carHeight) {
                    missile.removeAt(i)
                    score++
                    speed = 1 + Math.abs(score / 8)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        myPaint!!.color = Color.WHITE
        myPaint!!.textSize = 40f
        canvas.drawText("Score : $score", 80f, 80f, myPaint!!)
        canvas.drawText("Speed : $speed", 380f, 80f, myPaint!!)
        canvas.drawText("High Score : $highScore", 680f, 80f, myPaint!!) // Display high score
        invalidate()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                val x1 = event.x
                if (x1 < viewWidth / 2) {
                    if (myPlanePosition > 0) {
                        myPlanePosition--
                    }
                }
                if (x1 > viewWidth / 2) {
                    if (myPlanePosition < 2) {
                        myPlanePosition++
                    }
                }
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
            }
        }
        return true
    }
}
