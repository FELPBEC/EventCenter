package co.edu.uptc.viewController;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import co.edu.uptc.View.App;

public class MainViewController {

    @FXML
    private ComboBox<String> comboIdioma;

    @FXML
    private ResourceBundle resources;

    @FXML
    public void initialize() {
        comboIdioma.getItems().addAll(
            resources.getString("idioma.es"),
            resources.getString("idioma.en")
        );
    }

    @FXML
    private void switchToClientLogin( )throws IOException {
        App.setRoot("loginClient");
    }

    @FXML
    private void switchToAdminLogin( ) throws IOException {
        App.setRoot("loginView");
    }

    @FXML
    private void switchToExit( ) {
        System.exit(0);
    }

    @FXML
    private void switchToLanguageChange() {
        String seleccion = comboIdioma.getValue();
        if (seleccion == null) return;

        Locale nuevaLocale;
        if (seleccion.equals(resources.getString("idioma.en"))) {
            nuevaLocale = Locale.of("en");
        } else {
            nuevaLocale = Locale.of("es");
        }

        Locale.setDefault(nuevaLocale);

        try {
            Stage stage = (Stage) comboIdioma.getScene().getWindow();
            ResourceBundle bundle = ResourceBundle.getBundle("co.edu.uptc.i18n.textos", nuevaLocale);
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/co/edu/uptc/fxml/mainView.fxml"), bundle);
            Parent root = loader.load();
            
            MainViewController nuevoController = loader.getController();
            nuevoController.setLanguageSelection(seleccion);
            
            stage.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setLanguageSelection(String idioma) {
        if (comboIdioma != null) {
            comboIdioma.setValue(idioma);
        }
    }
}