
package co.edu.uptc.viewController;

import javafx.fxml.FXML;

public class MainViewController {

    /**
     * Este método está enlazado al botón del archivo FXML mediante 'onAction="#handleProbarBoton"'
     */
    @FXML
    private void handleProbarBoton() {
        System.out.println("=========================================");
        System.out.println("¡ÉXITO: El botón de la interfaz gráfica responde correctamente!");
        System.out.println("=========================================");
    }
}