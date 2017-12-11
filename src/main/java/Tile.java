import java.awt.*;

public class Tile {
    int value;

    public Tile(int value) {
        this.value = value;
    }

    public Tile() {
        this.value = 0;
    }

    public boolean isEmpty() {
        return value == 0;
    }

    public Color getFontColor() {
        return this.value < 16 ?  new Color(0x776e65) : new Color(0xf9f6f2);
    }

    public Color getTileColor() {
        int outTileColor = 0;

        switch (value) {
            case 0:
                outTileColor = 0xcdc1b4;
                break;
            case 2:
                outTileColor = 0xeee4da;
                break;
            case 4:
                outTileColor = 0xede0c8;
                break;
            case 8:
                outTileColor = 0xf2b179;
                break;
            case 16:
                outTileColor = 0xf59563;
                break;
            case 32:
                outTileColor = 0xf67c5f;
                break;
            case 64:
                outTileColor = 0xf65e3b;
                break;
            case 128:
                outTileColor = 0xedcf72;
                break;
            case 256:
                outTileColor = 0xedcc61;
                break;
            case 512:
                outTileColor = 0xedc850;
                break;
            case 1024:
                outTileColor = 0xedc53f;
                break;
            case 2048:
                outTileColor = 0xedc22e;
                break;
            default:
                outTileColor = 0xff0000;
        }
        return new Color(outTileColor);

    }
}
