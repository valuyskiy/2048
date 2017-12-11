import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        Model model = new Model();
        Controller controller = new Controller(model);

        JFrame game = new JFrame();

        game.setTitle("2048");
        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game.setSize(450, 520);
        game.setResizable(false);

        game.add(controller.getView());


        game.setLocationRelativeTo(null);
        game.setVisible(true);



//        model.print();
////        model.rotate();
////        System.out.println();
////        model.print();
//
//
//
//        while (model.canMove()){
//            model.left();
//            model.print();
//            model.up();
//            model.print();
//            model.down();
//            model.print();
//
//        }

    }
}
