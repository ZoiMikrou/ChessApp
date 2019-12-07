package com.chessapp.knightpaths

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.GridLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.chessapp.knightpaths.util.CoRoutines
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private val boardTiles = Array(6) { arrayOfNulls<ImageView>(6) }
    private val movesList = arrayOf(2, 3, 4, 5, 6, 7, 8, 9, 10)
    var chessBoard = ChessBoard()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appPreference = ChessAppPreferences(this)
        val savedListMoveIndex = appPreference.getNumberOfMoves()

        header_description.setText(R.string.start_position_text)
        button_find_paths.isEnabled = false

        val spinnerAdapter = ArrayAdapter<Int>(this, android.R.layout.simple_spinner_dropdown_item, movesList)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner_view!!.adapter = spinnerAdapter
        spinner_view!!.setSelection(savedListMoveIndex)
        spinner_view!!.onItemSelectedListener = this

        loadChessBoard()

        if (appPreference.getKnightStartX() != -1 && appPreference.getKnightEndX() != -1) {
            chessBoard.placeSavedPositions(appPreference.getKnightStartX(), appPreference.getKnightStartY(),
                appPreference.getKnightEndX(), appPreference.getKnightEndY())
            mapChessBoardToView()
            startComputing(movesList[savedListMoveIndex])
        }

        button_reset.setOnClickListener {
            chessBoard = ChessBoard()
            header_description.setText(R.string.start_position_text)
            mapChessBoardToView()
            button_find_paths.isEnabled = false
            knight_paths.text = ""
            appPreference.setKnightStartX(-1)
            appPreference.setKnightStartY(-1)
            appPreference.setKnightEndX(-1)
            appPreference.setKnightEndY(-1)
        }

        button_find_paths.setOnClickListener {
            startComputing(spinner_view.selectedItem as Int)
        }
    }

    private fun startComputing(numberOfMoves: Int) {
        val startTile = chessBoard.findPosition(ChessBoard.STARTING_POS)
        val destTile = chessBoard.findPosition(ChessBoard.ENDING_POS)
        if (startTile != null && destTile != null) {
            CoRoutines.default {
                val paths = chessBoard.findKnightPaths(startTile.i, startTile.j, destTile, numberOfMoves, 6, chessBoard)
                CoRoutines.transferToMain {
                    presentPaths(paths)
                }
            }

        }
    }

    private fun presentPaths(paths: List<ArrayList<Tile>>) {
        var text = ""
        if (paths.isEmpty()) {
            text = getString(R.string.no_available_paths)
        }
        for(i in 1..paths.size) {
            if (i != 1) text += "\n"
            text += "\n" + getString(R.string.path_text) + " " + i + ": "
            for (j in 0 until paths[i-1].size) {
                text += paths[i-1][j].toString()
                if (j != paths[i-1].size-1) {
                    text += " ,"
                }
            }
        }

        knight_paths.text = text
        knight_paths.movementMethod = ScrollingMovementMethod()
    }

    fun mapChessBoardToView() {
        for (i in chessBoard.board.indices) {
            for (j in chessBoard.board.indices) {
                when (chessBoard.board[i][j]) {
                    ChessBoard.STARTING_POS -> {
                        boardTiles[i][j]?.setImageResource(R.drawable.black_horse)
                        boardTiles[i][j]?.isEnabled = false
                    }
                    ChessBoard.ENDING_POS -> {
                        boardTiles[i][j]?.setImageResource(R.drawable.black_horse)
                        boardTiles[i][j]?.isEnabled = false
                    }
                    else -> {
                        boardTiles[i][j]?.setImageResource(0)
                        boardTiles[i][j]?.isEnabled = true
                    }
                }
            }
        }
    }

    // Generate chess board
    private fun loadChessBoard() {
        for (i in boardTiles.indices) {
            for (j in boardTiles.indices) {
                boardTiles[i][j] = ImageView(this)
                boardTiles[i][j]?.layoutParams = GridLayout.LayoutParams().apply {
                    rowSpec = GridLayout.spec(i)
                    columnSpec = GridLayout.spec(j)
                    width = 220
                    height = 220
                }

                val isLightTile: Boolean = (i % 2) == (j % 2)

                boardTiles[i][j]?.setOnClickListener(TileClickListener(i, j))
                boardTiles[i][j]?.x

                // Color tiles respectively
                if (isLightTile) {
                    boardTiles[i][j]?.setBackgroundColor(ContextCompat.getColor(this, R.color.lightChessTile))
                } else {
                    boardTiles[i][j]?.setBackgroundColor(ContextCompat.getColor(this, R.color.darkChessTile))
                }

                layout_chess_board.addView(boardTiles[i][j])
            }
        }
    }

    inner class TileClickListener(
        private val i: Int,
        private val j: Int
    ) : View.OnClickListener {
        override fun onClick(p0: View?) {
            val tile = Tile(i, j)
            val appPreference = ChessAppPreferences(applicationContext)

            if (!chessBoard.checkPositionPlaced(ChessBoard.STARTING_POS)) {
                chessBoard.placePosition(tile, ChessBoard.STARTING_POS)
                header_description.setText(R.string.end_position_text)
                appPreference.setKnightStartX(i)
                appPreference.setKnightStartY(j)
                button_find_paths.isEnabled = false
            }
            else if (chessBoard.checkPositionPlaced(ChessBoard.STARTING_POS) &&
                !chessBoard.checkPositionPlaced(ChessBoard.ENDING_POS)) {
                chessBoard.placePosition(tile, ChessBoard.ENDING_POS)
                header_description.setText(R.string.find_paths_text)
                appPreference.setKnightEndX(i)
                appPreference.setKnightEndY(j)
                button_find_paths.isEnabled = true
            }

            mapChessBoardToView()
        }
    }

    private fun shouldFindPathsButtonEnabled (): Boolean {
        return chessBoard.checkPositionPlaced(ChessBoard.STARTING_POS) &&
                chessBoard.checkPositionPlaced(ChessBoard.ENDING_POS)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        button_find_paths.isEnabled = false
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val appPreference = ChessAppPreferences(applicationContext)
        spinner_view.setSelection(position)
        appPreference.setNumberOfMoves(position)
        knight_paths.text = ""
        if (shouldFindPathsButtonEnabled()) {
            button_find_paths.isEnabled = true
        }
    }
}
