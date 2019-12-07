package com.chessapp.knightpaths

class KnightPaths {

    private var pathsList = mutableListOf<ArrayList<Tile>>()

    private fun recursiveSolution(i: Int, j: Int, steps: Int, n: Int, m: ChessBoard): Int {

        if (steps == 0) {
            val newPathList = arrayListOf<Tile>()
            newPathList.addAll(pathsList[pathsList.lastIndex])
            newPathList.removeAt(newPathList.lastIndex)
            pathsList.add(newPathList)
            return 1
        }

        var res = 0
        if (steps > 0) {

            // Movements for the knight
            val dx = intArrayOf(-2, -1, 1, 2, -2, -1, 1, 2)
            val dy = intArrayOf(-1, -2, -2, -1, 1, 2, 2, 1)

            // Find all possible positions knight can move
            for (k in 0..7) {

                // Check validity of position
                if (dx[k] + i >= 0 && dx[k] + i <= n - 1
                    && dy[k] + j >= 0 && dy[k] + j <= n - 1) {

                    // Call function with k-1 moves left
                    if (pathsList.isEmpty()) {
                        val list = arrayListOf<Tile>()
                        list.add(Tile(i, j))
                        pathsList.add(list)
                    } else if (pathsList[pathsList.lastIndex].isEmpty()) {
                        pathsList[pathsList.lastIndex].add(Tile(i, j))
                    }

                    pathsList[pathsList.lastIndex].add(Tile(dx[k] + i, dy[k] + j))
                    res += recursiveSolution(dx[k] + i, dy[k] + j, steps - 1, n, m)
                }
            }
        }

        //If no moves more found generate new path from existing and continue recursive solution
        if (pathsList[pathsList.lastIndex].isNotEmpty()) {
            if (pathsList[pathsList.lastIndex].size == 4) {
                val newPathList = arrayListOf<Tile>()
                pathsList.add(newPathList)
            } else {
                pathsList[pathsList.lastIndex].removeAt(pathsList[pathsList.lastIndex].lastIndex)
            }
        }

        return res
    }

    fun findPaths (i: Int, j: Int, steps: Int, n: Int, m:ChessBoard): List<ArrayList<Tile>> {
        recursiveSolution(i, j, steps, n, m)
        return pathsList.filter { path -> path.isNotEmpty() }
    }
}