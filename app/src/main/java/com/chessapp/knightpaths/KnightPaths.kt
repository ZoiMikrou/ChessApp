package com.chessapp.knightpaths

class KnightPaths {

    private var pathsList = mutableListOf<ArrayList<Tile>>()

    private fun recursiveSolution(tile: Tile, steps: Int, size: Int, chess: ChessBoard, maxSteps: Int) {

        if (steps == 0) {
            val newPathList = arrayListOf<Tile>()
            newPathList.addAll(pathsList[pathsList.lastIndex])
            newPathList.removeAt(newPathList.lastIndex)
            pathsList.add(newPathList)
            return
        }

        if (steps > 0) {

            // Movements for the knight
            val dx = intArrayOf(-2, -1, 1, 2, -2, -1, 1, 2)
            val dy = intArrayOf(-1, -2, -2, -1, 1, 2, 2, 1)

            // Find all possible positions knight can move
            for (k in 0..7) {

                // Check validity of position
                if (dx[k] + tile.i >= 0 && dx[k] + tile.i <= size - 1
                    && dy[k] + tile.j >= 0 && dy[k] + tile.j <= size - 1) {

                    // Call function with k-1 moves left
                    if (pathsList.isEmpty()) {
                        val list = arrayListOf<Tile>()
                        list.add(Tile(tile.i, tile.j))
                        pathsList.add(list)
                    } else if (pathsList[pathsList.lastIndex].isEmpty()) {
                        pathsList[pathsList.lastIndex].add(Tile(tile.i, tile.j))
                    }

                    pathsList[pathsList.lastIndex].add(Tile(dx[k] + tile.i, dy[k] + tile.j))
                    recursiveSolution(Tile(dx[k] + tile.i, dy[k] + tile.j),
                        steps - 1, size, chess, maxSteps)
                }
            }
        }

        //If no moves more found generate new path from existing and continue recursive solution
        if (pathsList[pathsList.lastIndex].isNotEmpty()) {
            if (pathsList[pathsList.lastIndex].size == maxSteps + 1) {
                val newPathList = arrayListOf<Tile>()
                pathsList.add(newPathList)
            } else {
                pathsList[pathsList.lastIndex].removeAt(pathsList[pathsList.lastIndex].lastIndex)
            }
        }

        return
    }

    fun findPaths (tile: Tile, steps: Int, size: Int, chess: ChessBoard, maxSteps: Int):
            List<ArrayList<Tile>> {
        recursiveSolution(tile, steps, size, chess, maxSteps)
        return pathsList.filter { path -> path.isNotEmpty() }
    }
}