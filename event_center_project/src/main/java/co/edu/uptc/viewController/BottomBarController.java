package co.edu.uptc.viewController;

import co.edu.uptc.View.App;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class BottomBarController {

    @FXML
    private ComboBox<String> comboIdioma;

    @FXML
    private ResourceBundle resources;

    @FXML
    public void initialize() {
        // Poblamos el combo de forma dinámica con el i18n actual
        comboIdioma.getItems().addAll(
            resources.getString("idioma.es"),
            resources.getString("idioma.en")
        );

        // Dejar seleccionado visualmente el idioma que esté activo en el sistema
        Locale actual = Locale.getDefault();
        if (actual.getLanguage().equals("en")) {
            comboIdioma.setValue(resources.getString("idioma.en"));
        } else {
            comboIdioma.setValue(resources.getString("idioma.es"));
        }
    }

    @FXML
    private void switchToLanguageChange(ActionEvent event) {
        String seleccion = comboIdioma.getValue();
        if (seleccion == null) return;

        Locale nuevaLocale = seleccion.equals(resources.getString("idioma.en")) 
                            ?  Locale.of("en") 
                            :  Locale.of("es");

        Locale.setDefault(nuevaLocale);

        try {
            // Recargamos automáticamente la pantalla que esté abierta actualmente
            App.setRoot(App.getCurrentFxml());
        } catch (IOException e) {
            System.err.println("Error al recargar la vista tras el cambio de idioma.");
            e.printStackTrace();
        }
    }
}