package com.chessapp.knightpaths

import kotlinx.coroutines.delay

class ChessBoard {

    companion object {
        const val STARTING_POS: String = "S"
        const val ENDING_POS: String = "E"
    }

    val board = Array(6) { arrayOfNulls<String>(6) }

    fun placePosition (tile: Tile, position: String) {
        board[tile.i][tile.j] = position
    }

    fun checkPositionPlaced (position: String) : Boolean {
        for (i in board.indices) {
            for (j in board.indices) {
                if (board[i][j] == position) {
                    return true
                }
            }
        }
        return false
    }

    fun findPosition(position: String): Tile? {
        for (i in board.indices) {
            for (j in board.indices) {
                if (board[i][j] == position) {
                    return Tile(i, j)
                }
            }
        }
        return null
    }

    fun placeSavedPositions(startX: Int, startY: Int, endX: Int, endY: Int) {
        // Start Tile
        for (i in board.indices) {
            for (j in board.indices) {
                if (i == startX && j == startY) {
                    board[i][j] = STARTING_POS
                }
            }
        }
        //End Tile
        for (i in board.indices) {
            for (j in board.indices) {
                if (i == endX && j == endY) {
                    board[i][j] = ENDING_POS
                }
            }
        }
    }

    suspend fun findKnightPaths (i: Int, j: Int, destinationTile: Tile, steps: Int, n: Int, m:ChessBoard) : List<ArrayList<Tile>> {
        val knightPaths = KnightPaths ()
        val listOfAllPaths = knightPaths.findPaths(i, j, steps, n, m)
        delay(500)
        return listOfAllPaths.filter {
                path -> path[path.lastIndex].i == destinationTile.i && path[path.lastIndex].j == destinationTile.j
        }
    }
}