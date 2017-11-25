import java.awt.*;
import java.util.ArrayList;


public class KnightShortestPaths {
    static Pos[][] chessboard = new Pos[8][8];
    static Point[] stonesLocation = new Point[7];
    static Point[] jumpDefinition = new Point[8];

    static ArrayList<Pos> pathList = new ArrayList<>();

    public static void main(String[] args) {
        int numberOfPaths;
        int boardSize = 8;

        Pos start = new Pos(1, 1, 0, 0, 0);
        Pos end = new Pos(1, 8, Integer.MAX_VALUE, 0, 0);

        jumpDefinition[0] = new Point(1, 2);
        jumpDefinition[1] = new Point(2, 1);
        jumpDefinition[2] = new Point(1, -2);
        jumpDefinition[3] = new Point(2, -1);
        jumpDefinition[4] = new Point(-1, 2);
        jumpDefinition[5] = new Point(-2, 1);
        jumpDefinition[6] = new Point(-1, -2);
        jumpDefinition[7] = new Point(-2, -1);

        stonesLocation[0] = new Point(1, 6);
        stonesLocation[1] = new Point(2, 6);
        stonesLocation[2] = new Point(3, 6);
        stonesLocation[3] = new Point(4, 6);
        stonesLocation[4] = new Point(5, 6);
        stonesLocation[5] = new Point(1, 5);
        stonesLocation[6] = new Point(4, 5);

        numberOfPaths = getNumberOfShortestPaths(boardSize, jumpDefinition, stonesLocation, start, end);
        System.out.println("Number of PATHS " + numberOfPaths);
    }

    public static int getNumberOfShortestPaths(int boardSize, Point[] jumpDefinition, Point[] stonesLocation, Pos start, Pos end) {
        int allPath;
        final Pos[][] chessboard = new Pos[boardSize][boardSize];
        populateChessBoard();

        // Change indexes for chessboard positions
        start.x--;
        start.y--;
        end.x--;
        end.y--;

        // Change indexes for stone locations according chessboard position
        System.out.println("Stones location");
        for (Point stone : stonesLocation) {
            stone.setLocation(stone.x - 1, stone.y - 1);
            System.out.println((stone.x + 1) + " " + (stone.y + 1));
            System.out.println(" ");
        }

        chessboard[start.x][start.y] = new Pos(start.x, start.y, 0, 0, 0);
        // add start position to the pathList
        pathList.add(new Pos(start.x + 1, start.y + 1, 0, 0, 0));
        Pos endPosition = new Pos(end.x + 1, end.y + 1, 0, 0, 0);

        int i = 0;
        boolean isEndPosition = false;
        while (!isEndPosition) {
            if (i < pathList.size()) {
                if (pathList.get(i).fromX == endPosition.x && pathList.get(i).fromY == endPosition.y) {
                    isEndPosition = true;
                }
                Pos pos = pathList.get(i);
                Pos chessboardPos = new Pos(pos.x - 1, pos.y - 1, pos.depth, 0, 0);

                /* Perform Breadth First Search
                   bfs(chessboardPos, pos.depth + 1); */
                bfs(jumpDefinition, chessboardPos, pos.depth + 1);

                i++;
            } else {
                break;
            }
        }

        allPath = getAllShortestPath(start, end);
        return allPath;
    }


    //Breadth First Search
    private static void bfs(Point[] jumpDefinition, Pos current, int depth) {
        for (int k = 0; k < jumpDefinition.length; k++) {
            int i = jumpDefinition[k].x;
            int j = jumpDefinition[k].y;

            Pos next = new Pos(current.x + i, current.y + j, depth, current.x, current.y);
            Pos nextToList = new Pos(current.x + i + 1, current.y + j + 1, depth, current.x + 1, current.y + 1);

            if (inRange(next.x, next.y)) {
                if (isValidJump(current, next)) {
                    Pos position = chessboard[next.x][next.y];
                        /*
                         * Get the current position object at this location on chessboard.
						 * If this location was reachable with a costlier depth, this iteration has given a shorter way to reach
						 */
                    if (position.depth >= depth) {
                        int oldWays = chessboard[current.x + i][current.y + j].ways;
                        int oldWaysFrom = chessboard[current.x][current.y].waysFrom;
                        chessboard[current.x + i][current.y + j] = new Pos(current.x, current.y, depth, 0, 0);
                        chessboard[current.x + i][current.y + j].ways = oldWays + 1;
                        chessboard[current.x][current.y].waysFrom = oldWaysFrom + 1;
                        // add position to pathList
                        pathList.add(nextToList);
                    }
                }
            }
        }

    }

    private static boolean inRange(int x, int y) {
        return 0 <= x && x < 8 && 0 <= y && y < 8;
    }

    public static boolean isValidJump(Pos current, Pos next) {
        int deltaX = next.x - current.x;
        int deltaY = next.y - current.y;
        boolean valid = false;
        for (int k = 0; k < jumpDefinition.length; k++) {
            int jumpX = jumpDefinition[k].x;
            int jumpY = jumpDefinition[k].y;
            if (jumpX == deltaX && jumpY == deltaY) {
                valid = true;
                break;
            }
        }
        return valid && !isStone(stonesLocation, next);
    }

    public static boolean isStone(Point[] stones, Pos position) {
        boolean isStone = false;
        for (Point stone : stones) {
            if (stone.x == position.x && stone.y == position.y) {
                isStone = true;
                break;
            }
        }
        return isStone;
    }

    /*Populate initial chessboard values*/
    private static void populateChessBoard() {
        for (int i = 0; i < chessboard.length; i++) {
            for (int j = 0; j < chessboard[0].length; j++) {
                chessboard[i][j] = new Pos(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 0, 0);
            }
        }
    }

    private static int getAllShortestPath(Pos start, Pos end) {
        ArrayList<Pos> removedList = new ArrayList<>();
        int toPositionX;
        int toPositionY;
        int numberOfPaths = 0;
        int endPosition = 0;
        boolean newPath = true;

        while (newPath) {
            newPath = false;
            int depth = 0;
            //Find end position in pathList
            for (int i = pathList.size() - 1; i >= 0; i--) {
                if (pathList.get(i).x != (end.x + 1) || pathList.get(i).y != (end.y + 1)) {
                    continue;
                } else {
                    endPosition = i;
                    depth = pathList.get(i).depth;
                    System.out.println("Path "+ (numberOfPaths+1));
                    break;
                }
            }
            if (endPosition >= pathList.size() || endPosition == 0) {
                return numberOfPaths;
            }

            toPositionX = pathList.get(endPosition).x;
            toPositionY = pathList.get(endPosition).y;
            for (int i = endPosition; i >= 0; i--) {
                Pos currentSearch = new Pos(toPositionX, toPositionY, depth, 0, 0);
                boolean containsInPathList = pathList.contains(currentSearch);

                /* restore position that was removed, but there is a way
                   to this one in current iteration */
                if (!containsInPathList) {
                    int indexInRemoved = removedList.lastIndexOf(currentSearch);
                    if (indexInRemoved != -1) {
                        Pos restoredPosition = new Pos(removedList.get(indexInRemoved).x, removedList.get(indexInRemoved).y, removedList.get(indexInRemoved).depth, removedList.get(indexInRemoved).fromX, removedList.get(indexInRemoved).fromY);
                        pathList.add(i, restoredPosition);
                    }
                }
                boolean WaysToPositionIsPositive = chessboard[pathList.get(i).x - 1][pathList.get(i).y - 1].ways > 0;
                boolean WaysFromPositionIspositive = chessboard[pathList.get(i).x - 1][pathList.get(i).y - 1].waysFrom > 0;
                boolean isStart = pathList.get(i).x == (start.x + 1) && pathList.get(i).y == (start.y + 1);

                if ((WaysToPositionIsPositive || WaysFromPositionIspositive || isStart) && pathList.get(i).x == toPositionX && pathList.get(i).y == toPositionY && pathList.get(i).depth == depth) {
                    chessboard[pathList.get(i).x - 1][pathList.get(i).y - 1].ways--;
                    chessboard[pathList.get(i).x - 1][pathList.get(i).y - 1].waysFrom--;
                    toPositionX = pathList.get(i).fromX;
                    toPositionY = pathList.get(i).fromY;
                    depth--;
                    newPath = true;
                    System.out.println("[" + pathList.get(i).x + " " + pathList.get(i).y + "] ");

                    // retain start position
                    if (!isStart) {
                        removedList.add(pathList.get(i));
                        pathList.remove(i);
                    }
                    if (depth == 0) {
                        numberOfPaths++;
                    }
                }
            }
        }
        return numberOfPaths;
    }
}


