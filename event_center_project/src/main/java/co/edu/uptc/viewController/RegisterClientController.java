package co.edu.uptc.viewController;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.io.IOException;

import co.edu.uptc.Model.Client;
import co.edu.uptc.Services.SessionManager;
import co.edu.uptc.Services.SistemaController;
import co.edu.uptc.View.App;
import javafx.event.ActionEvent;

public class RegisterClientController {
    
    @FXML
    private TextField txtDocument;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private TextField txtName;
    @FXML
    private TextField txtUsernameEmail;
    @FXML
    private TextField txtDomianEmail;
    @FXML
    private TextField txtPhoneNumber;
    @FXML
    private RadioButton radYes;
    @FXML
    private ToggleGroup empresarial;
    @FXML
    private RadioButton radNo;
    @FXML
    private Button btnRegister;

    private SistemaController sistema = SistemaController.getInstance();

    @FXML
public void initialize() {
   Platform.runLater(() -> {
        radYes.setSelected(false);
        radNo.setSelected(false);
    });
}
    @FXML
    public void register() {
        try {
            // 1. Obtención y validación de datos no vacíos
            if (txtDocument.getText().isEmpty() || txtPassword.getText().isEmpty() || 
                txtName.getText().isEmpty() || txtUsernameEmail.getText().isEmpty() || 
                txtPhoneNumber.getText().isEmpty()) {
                mostrarAlerta("Error", "Todos los campos son obligatorios.");
                return;
            }

            // 2. Validación de formato de ID
            String idStr = txtDocument.getText();
            if (!sistema.isCedulaValida(idStr)) {
                mostrarAlerta("Error", "El número de documento no es válido.");
                return;
            }

            // Convertimos de forma segura
            int id = Integer.parseInt(idStr);

            // 3. Validación de formato de contraseña
            String rawPassword = txtPassword.getText();
            if (!sistema.isPasswordValida(rawPassword)) {
                String requisitos = "La contraseña no es válida.\n\n" +
                        "Requisitos:\n" +
                        "• Mínimo 8 caracteres.\n" +
                        "• Al menos 1 letra mayúscula.\n" +
                        "• Al menos 1 carácter especial (ej: @, #, $, *).";
    
                 mostrarAlerta("Error de formato", requisitos);
                    return;
            }
            // 4. Construcción del cliente
            String email = txtUsernameEmail.getText() + "@" + txtDomianEmail.getText();
            boolean isEmpresarial = (empresarial.getSelectedToggle() == radYes);
            String passwordEncriptada = sistema.encriptarPassword(rawPassword);

            Client nuevoCliente = new Client(txtName.getText(), id, passwordEncriptada, 
                                             txtPhoneNumber.getText(), email, isEmpresarial);

            // 5. Registro con control de flujo
            if (sistema.registrarCliente(nuevoCliente)) {
                // 2. ÉXITO: Iniciamos la sesión inmediatamente
        SessionManager.getInstance().iniciarSesionCliente(nuevoCliente);
        
        // 3. Notificamos y navegamos
        mostrarAlerta("Éxito", "Bienvenido, " + nuevoCliente.getUserName());
                mostrarAlerta("Éxito", "Cliente registrado exitosamente.");
                limpiarCampos();
            } else {
                mostrarAlerta("Error", "El cliente con ID " + id + " ya existe en el sistema.");
            }

        } catch (NumberFormatException e) {
            mostrarAlerta("Error de formato", "El documento debe ser un número entero.");
        } catch (Exception e) {
            // Este catch atrapa cualquier otro error inesperado (como archivos JSON corruptos)
            mostrarAlerta("Error crítico", "Ocurrió un error inesperado: " + e.getMessage());
            e.printStackTrace(); // Útil para depurar en consola
        }
    }
    private void mostrarAlerta(String titulo, String contenido) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    private void limpiarCampos() {
        txtDocument.clear();
        txtPassword.clear();
        txtName.clear();
        txtUsernameEmail.clear();
        txtDomianEmail.clear();
        txtPhoneNumber.clear();
        empresarial.selectToggle(null);
    }

    @FXML
    private void switchToBack()throws IOException {
       App.setRoot("loginClient");
    }

}
