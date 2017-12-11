
import java.util.*;

public class Model {
    private static final int FIELD_WIDTH = 4;
    private Tile[][] gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
    int score, maxTile;
    Stack previousStates = new Stack();
    Stack previousScores = new Stack();

    private boolean isSaveNeeded = true;

    public void randomMove() {
        int n = ((int) (Math.random() * 100)) % 4;
        switch (n) {
            case 0:
                left();
                break;
            case 1:
                right();
                break;
            case 2:
                down();
                break;
            case 3:
                up();
                break;
        }

    }


    private void saveState(Tile[][] tiles) {
        Tile[][] newGameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                newGameTiles[i][j] = new Tile(tiles[i][j].value);
            }
        }
        previousStates.push(newGameTiles);
        previousScores.push(score);
        isSaveNeeded = false;
    }

    public void rollback() {
        if (!previousScores.empty() && !previousStates.empty()) {
            score = (int) previousScores.pop();
            gameTiles = (Tile[][]) previousStates.pop();
        }


    }

    public Model() {
        resetGameTiles();
    }

    public Tile[][] getGameTiles() {
        return this.gameTiles;
    }

    private List<Tile> getEmptyTiles() {
        List<Tile> outList = new ArrayList<>();
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                if (gameTiles[i][j].isEmpty()) outList.add(gameTiles[i][j]);
            }
        }
        return outList;

    }

    public void addTile() {
        List<Tile> emptyTilesList = getEmptyTiles();
        if (emptyTilesList.size() > 0) {
            int indexRandomTile = (int) (emptyTilesList.size() * Math.random());
            emptyTilesList.get(indexRandomTile).value = Math.random() < 0.9 ? 2 : 4;
        }
    }

    public void resetGameTiles() {
        score = 0;
        maxTile = 2;
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                gameTiles[i][j] = new Tile();
            }
        }
        addTile();
        addTile();
    }

    private boolean compressTiles(Tile[] tiles) {
        boolean out = false;
        for (int i = 1; i < tiles.length; i++) {
            for (int k = 1; k <= i; k++) {
                if (!tiles[i - k + 1].isEmpty() && tiles[i - k].isEmpty()) {
                    tiles[i - k] = tiles[i - k + 1];
                    tiles[i - k + 1] = new Tile();
                    out = true;
                }
            }
        }
        return out;
    }

    private boolean mergeTiles(Tile[] tiles) {
        int index1 = 0, index2 = 1;
        boolean out = false;

        while (index1 < FIELD_WIDTH - 1) {
            if (tiles[index1].value > 0 && tiles[index1].value == tiles[index2].value) {
                tiles[index1].value += tiles[index1].value;
                tiles[index2].value = 0;
                score += tiles[index1].value;
                if (tiles[index1].value > maxTile) maxTile = tiles[index1].value;
                compressTiles(tiles);
                out = true;
            }
            index1++;
            index2++;
        }
        return out;
    }

    public void left() {
        if (isSaveNeeded) {
            saveState(gameTiles);
        }

        boolean isChange = false;

        for (int i = 0; i < FIELD_WIDTH; i++) {
            boolean isCompress = compressTiles(gameTiles[i]);
            boolean isMerge = mergeTiles(gameTiles[i]);

            if (isCompress || isMerge) {
                isChange = true;
            }
        }
        isSaveNeeded = true;

        if (isChange) {
            addTile();
        }

    }

    public void down() {
        saveState(gameTiles);
        rotate();
        left();
        rotate();
        rotate();
        rotate();
    }

    public void right() {
        saveState(gameTiles);
        rotate();
        rotate();
        left();
        rotate();
        rotate();
    }

    public void up() {
        saveState(gameTiles);
        rotate();
        rotate();
        rotate();
        left();
        rotate();
    }


    public void rotate() {
        Tile[][] tmpGameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];

        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                tmpGameTiles[j][FIELD_WIDTH - i - 1] = gameTiles[i][j];
            }
        }
        gameTiles = tmpGameTiles;
    }

    public void print() {
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                System.out.print(gameTiles[i][j].value + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public boolean canMove() {
        if (getEmptyTiles().size() > 0) {
            return true;
        }

        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 1; j < FIELD_WIDTH; j++) {
                if (gameTiles[i][j].value == gameTiles[i][j - 1].value) {
                    return true;
                }
            }
        }

        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 1; j < FIELD_WIDTH; j++) {
                if (gameTiles[j][i].value == gameTiles[j - 1][i].value) {
                    return true;
                }
            }
        }

        return false;

    }

    private int getWeitOfTiles(Tile[][] tiles) {
        int out = 0;
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                out += tiles[i][j].value;
            }
        }
        return out;
    }

    boolean hasBoardChanged() {
        return getWeitOfTiles(gameTiles) != getWeitOfTiles(((Tile[][]) previousStates.peek()));
    }

    MoveEfficiency getMoveEfficiency(Move move) {
        MoveEfficiency moveEfficiency;
        move.move();
        if (hasBoardChanged()) {
            moveEfficiency = new MoveEfficiency(getEmptyTiles().size(), score, move);
        } else {
            moveEfficiency = new MoveEfficiency(-1, 0, move);
        }
        rollback();
        return moveEfficiency;
    }

    void autoMove() {
        PriorityQueue<MoveEfficiency> priorityQueue = new PriorityQueue<>(4, Collections.reverseOrder());
        priorityQueue.add(getMoveEfficiency(this::up));
        priorityQueue.add(getMoveEfficiency(() -> left()));

        priorityQueue.add(getMoveEfficiency(new Move() {
            @Override
            public void move() {
                down();
            }
        }));
        priorityQueue.add(getMoveEfficiency(new Move() {
            @Override
            public void move() {
                right();
            }
        }));


        if (gameTiles[0][0].isEmpty()) {
            up();
        } else {
            priorityQueue.peek().getMove().move();
        }


    }

}
