package conta.tela;

import javafx.application.Application;
import javafx.stage.Stage;

// Responsável por fazer o ponto de inicio de execução
public class AdaptadorJavaFx extends Application {

    @Override
    public void start(Stage stage) {
        var form = new TransferenciaFrm();
        form.mostrar(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
