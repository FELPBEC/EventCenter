package co.edu.uptc.viewController;

import java.io.IOException;

import co.edu.uptc.Model.Client;
import co.edu.uptc.Services.ClientService;
import co.edu.uptc.View.App;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class ActualizarClienteController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtCorreo;
    @FXML private TextField txtTelefono;
    @FXML private CheckBox chkEmpresarial;

    private Client client;
    private final ClientService clientService = new ClientService();

    public void setClient(Client client) {
        this.client = client;
        if (client != null) {
            txtNombre.setText(client.getUserName());
            txtCorreo.setText(client.getEmail());
            txtTelefono.setText(client.getPhoneNumber());
            chkEmpresarial.setSelected(client.isEmpresarial());
        }
    }

    @FXML
    private void guardarCliente(ActionEvent event) {
        if (client == null) return;

        String nombre = txtNombre.getText() == null ? "" : txtNombre.getText().trim();
        String correo = txtCorreo.getText() == null ? "" : txtCorreo.getText().trim();
        String telefono = txtTelefono.getText() == null ? "" : txtTelefono.getText().trim();
        boolean empresarial = chkEmpresarial.isSelected();

        if (nombre.isEmpty() || correo.isEmpty()) {
            Alert a = new Alert(Alert.AlertType.WARNING, "Nombre y correo son obligatorios", ButtonType.OK);
            a.showAndWait();
            return;
        }

        Client actualizado = new Client(nombre, client.getId(), client.getPassword(), telefono, correo, empresarial);
        boolean exito = clientService.modificarCliente(client.getId(), actualizado);

        if (exito) {
            Alert ok = new Alert(Alert.AlertType.INFORMATION, "Cliente actualizado correctamente", ButtonType.OK);
            ok.showAndWait();
            closeWindow();
        } else {
            Alert err = new Alert(Alert.AlertType.ERROR, "No se pudo actualizar el cliente", ButtonType.OK);
            err.showAndWait();
        }
    }

    @FXML
    private void cancelar(ActionEvent event) {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

    private void closeWindow() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        if (stage != null) stage.close();
    }
}
