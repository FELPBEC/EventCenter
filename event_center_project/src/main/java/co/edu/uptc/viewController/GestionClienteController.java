package co.edu.uptc.viewController;

import co.edu.uptc.Model.Client;
import co.edu.uptc.Services.ClientService;
import co.edu.uptc.View.App;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class GestionClienteController implements Initializable {

    @FXML private TextField txtBuscarCliente;
    @FXML private TableView<Client> clientesTable;
    @FXML private TableColumn<Client, Integer> colIdentificacion;
    @FXML private TableColumn<Client, String> colNombres;
    @FXML private TableColumn<Client, String> colCorreo;
    @FXML private TableColumn<Client, String> colTelefono;
    @FXML private TableColumn<Client, String> colTipo;
    @FXML private Button btnToggleMenu;
    @FXML private Button actualizarButton;
    @FXML private Button eliminarButton;
    @FXML private VBox sideMenuContainer;

    private ClientService clientService;
    private FilteredList<Client> filteredClientes;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            clientService = new ClientService();
            ObservableList<Client> clientesData = FXCollections.observableArrayList(clientService.getListClients());
            filteredClientes = new FilteredList<>(clientesData, client -> true);

            colIdentificacion.setCellValueFactory(new PropertyValueFactory<>("id"));
            colNombres.setCellValueFactory(new PropertyValueFactory<>("userName"));
            colCorreo.setCellValueFactory(new PropertyValueFactory<>("email"));
            colTelefono.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
            colTipo.setCellValueFactory(cellData -> {
                String tipo = cellData.getValue().isEmpresarial() ? "Sí" : "No";
                return new ReadOnlyStringWrapper(tipo);
            });

            if (clientesTable != null) {
                clientesTable.setItems(filteredClientes);
            }
        } catch (Exception e) {
            e.printStackTrace();
            javafx.application.Platform.runLater(() -> {
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
                alert.setTitle("Error cargando gestión de clientes");
                alert.setHeaderText(null);
                alert.setContentText(e.toString());
                alert.showAndWait();
            });
        }
    }

    @FXML
    private void buscarCliente() {
        String query = txtBuscarCliente.getText();
        if (query == null || query.isBlank()) {
            filteredClientes.setPredicate(client -> true);
            return;
        }

        String lowerQuery = query.trim().toLowerCase();
        filteredClientes.setPredicate(client ->
            String.valueOf(client.getId()).contains(lowerQuery)
                || client.getUserName().toLowerCase().contains(lowerQuery)
        );
    }

    @FXML
    private void actualizarCliente() {
        Client seleccionado = clientesTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            javafx.scene.control.Alert alerta = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
            alerta.setTitle("Selecciona un cliente");
            alerta.setHeaderText(null);
            alerta.setContentText("Por favor selecciona un cliente de la tabla antes de actualizar.");
            alerta.showAndWait();
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/co/edu/uptc/fxml/actualizarCliente.fxml"));
            Parent root = loader.load();

            Object controller = loader.getController();
            if (!(controller instanceof ActualizarClienteController)) {
                throw new IllegalStateException("El controlador de actualizarCliente.fxml no es válido.");
            }

            ((ActualizarClienteController) controller).setClient(seleccionado);

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.setTitle("Actualizar cliente");
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

            refreshTable();
        } catch (IOException | IllegalStateException ex) {
            ex.printStackTrace();
            javafx.scene.control.Alert alerta = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            alerta.setTitle("Error al abrir el formulario");
            alerta.setHeaderText(null);
            alerta.setContentText("No se pudo abrir el formulario de actualización: " + ex.getMessage());
            alerta.showAndWait();
        }
    }

    @FXML
    private void eliminarCliente() {
        Client seleccionado = clientesTable.getSelectionModel().getSelectedItem();
        if (seleccionado == null) return;

        javafx.scene.control.Alert confirm = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Eliminar cliente");
        confirm.setHeaderText("Confirmar eliminación");
        confirm.setContentText("¿Deseas eliminar al cliente " + seleccionado.getUserName() + "?");
        java.util.Optional<javafx.scene.control.ButtonType> res = confirm.showAndWait();
        if (res.isEmpty() || res.get() != javafx.scene.control.ButtonType.OK) return;

        boolean exito = clientService.eliminarCliente(seleccionado.getId());
        if (exito) {
            refreshTable();
            javafx.scene.control.Alert info = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
            info.setTitle("Cliente eliminado");
            info.setHeaderText(null);
            info.setContentText("El cliente ha sido eliminado correctamente.");
            info.showAndWait();
        } else {
            javafx.scene.control.Alert err = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
            err.setTitle("Error al eliminar");
            err.setHeaderText(null);
            err.setContentText("No fue posible eliminar el cliente.");
            err.showAndWait();
        }
    }

    private void refreshTable() {
        if (filteredClientes == null) return;
        ObservableList<Client> data = FXCollections.observableArrayList(clientService.getListClients());
        filteredClientes = new FilteredList<>(data, filteredClientes.getPredicate());
        clientesTable.setItems(filteredClientes);
    }

    @FXML
    private void toggleMenu() {
        if (sideMenuContainer == null) {
            return;
        }
        boolean visible = sideMenuContainer.isVisible();
        sideMenuContainer.setVisible(!visible);
        sideMenuContainer.setManaged(!visible);
    }
}
