package co.edu.uptc.View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) {
        try {
            Font.loadFont(App.class.getResourceAsStream("/co/edu/uptc/fonts/Pacifico-Regular.ttf"), 10);
            Font.loadFont(App.class.getResourceAsStream("/co/edu/uptc/fonts/IBMPlexMono-Regular.ttf"), 10);
            Font.loadFont(App.class.getResourceAsStream("/co/edu/uptc/fonts/Caveat-Regular.ttf"), 10);

            scene = new Scene(loadFXML("mainView"));
            
            stage.setTitle("Centro de Eventos Elite - Sistema de Gestión");
            stage.setScene(scene);
            
            stage.setResizable(true);  
            stage.setMaximized(true); 
            
            stage.show();
            
        } catch (IOException e) {
            System.err.println("Error crítico: No se pudo cargar el FXML inicial 'mainView'.");
            e.printStackTrace();
        } catch (NullPointerException e) {
            System.err.println("Error de carga: Asegúrate de que las fuentes estén en /co/edu/uptc/fonts/.");
            e.printStackTrace();
        }
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        Locale locale = Locale.getDefault();
        ResourceBundle bundle = ResourceBundle.getBundle("co.edu.uptc.i18n.textos", locale);
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/co/edu/uptc/fxml/" + fxml + ".fxml"), bundle);
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch(args);
    }
}