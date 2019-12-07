package com.chessapp.knightpaths

import android.content.Context

class ChessAppPreferences (context: Context) {

    private val moves = "moves"
    private val knightStartXPos = "startX"
    private val knightStartYPos = "startY"
    private val knightEndXPos = "endX"
    private val knightEndYPos = "endY"

    private val movePref = context.getSharedPreferences(moves, Context.MODE_PRIVATE)
    private val knightStartXPref = context.getSharedPreferences(knightStartXPos, Context.MODE_PRIVATE)
    private val knightStartYPref = context.getSharedPreferences(knightStartYPos, Context.MODE_PRIVATE)
    private val knightEndXPref = context.getSharedPreferences(knightEndXPos, Context.MODE_PRIVATE)
    private val knightEndYPref = context.getSharedPreferences(knightEndYPos, Context.MODE_PRIVATE)

    fun getNumberOfMoves(): Int {
        return movePref.getInt(moves, 1)
    }

    fun setNumberOfMoves(no: Int) {
        val editor = movePref.edit()
        editor.putInt(moves, no)
        editor.apply()
    }

    fun getKnightStartX(): Int {
        return knightStartXPref.getInt(knightStartXPos, -1)
    }

    fun setKnightStartX(no: Int) {
        val editor = knightStartXPref.edit()
        editor.putInt(knightStartXPos, no)
        editor.apply()
    }

    fun getKnightStartY(): Int {
        return knightStartYPref.getInt(knightStartYPos, -1)
    }

    fun setKnightStartY(no: Int) {
        val editor = knightStartYPref.edit()
        editor.putInt(knightStartYPos, no)
        editor.apply()
    }

    fun getKnightEndX(): Int {
        return knightEndXPref.getInt(knightEndXPos, -1)
    }

    fun setKnightEndX(no: Int) {
        val editor = knightEndXPref.edit()
        editor.putInt(knightEndXPos, no)
        editor.apply()
    }

    fun getKnightEndY(): Int {
        return knightEndYPref.getInt(knightEndYPos, -1)
    }

    fun setKnightEndY(no: Int) {
        val editor = knightEndYPref.edit()
        editor.putInt(knightEndYPos, no)
        editor.apply()
    }
}