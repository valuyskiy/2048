import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import static java.awt.event.KeyEvent.*;

public class Controller extends KeyAdapter {
    Model model;
    View view;

    public View getView() {
        return view;
    }

    private static final int WINNING_TILE = 2048;

    public Controller(Model model) {
        this.model = model;
        this.view = new View(this);
    }

    public Tile[][] getGameTiles() {
        return model.getGameTiles();
    }

    public int getScore() {
        return model.score;
    }

    public void resetGame() {
        model.score = 0;
        view.isGameLost = false;
        view.isGameWon = false;
        model.resetGameTiles();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (!model.canMove()) {
            view.isGameLost = true;
        }
        if (keyCode != VK_ESCAPE) {
            if (!view.isGameWon && !view.isGameLost) {
                switch (keyCode) {
                    case VK_LEFT:
                        model.left();
                        break;
                    case VK_RIGHT:
                        model.right();
                        break;
                    case VK_DOWN:
                        model.down();
                        break;
                    case VK_UP:
                        model.up();
                        break;
                    case VK_Z:
                        model.rollback();
                        break;
                    case VK_R:
                        model.randomMove();
                        break;
                    case VK_A:
                        model.autoMove();
                        break;

                }
            }
            if (model.maxTile == WINNING_TILE) {
                view.isGameWon = true;
            }

        } else {
            resetGame();
        }

        view.repaint();
    }
}
