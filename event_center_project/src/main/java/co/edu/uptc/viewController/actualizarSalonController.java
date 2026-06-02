package co.edu.uptc.viewController;

import co.edu.uptc.Model.Salon;
import co.edu.uptc.Services.SalonServices;
import co.edu.uptc.View.App;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class actualizarSalonController {

    @FXML private TextField txtSalonName;
    @FXML private TextField txtCapacity;
    @FXML private TextField txtPrice;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;
    @FXML private Button btnAddImages;
    @FXML private Button btnRemoveImage;
    @FXML private ListView<String> lstImagePaths;
    @FXML private Label lblImageInfo;

    private final SalonServices salonServices = new SalonServices();
    private final List<String> currentImagePaths = new ArrayList<>();
    private Salon salon;

    @FXML
    public void initialize() {
        Platform.runLater(() -> txtSalonName.requestFocus());
    }

    public void setSalon(Salon salon) {
        this.salon = salon;
        if (salon != null) {
            txtSalonName.setText(salon.getSalonName());
            txtCapacity.setText(String.valueOf(salon.getCapacity()));
            txtPrice.setText(String.valueOf(salon.getPriceByHour()));
            currentImagePaths.clear();
            if (salon.getImagePaths() != null) {
                currentImagePaths.addAll(salon.getImagePaths());
            }
            refreshImageList();
        }
    }

    @FXML
    private void addImages() {
        Window ownerWindow = btnAddImages.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar fotos del salón");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        fileChooser.setInitialDirectory(new File(System.getProperty("event_center_project\\src\\main\\resources\\co\\edu\\uptc\\images")));

        List<File> files = fileChooser.showOpenMultipleDialog(ownerWindow);
        if (files != null && !files.isEmpty()) {
            Path projectImagesDir = Paths.get(System.getProperty("event_center_project\\src\\main\\resources\\co\\edu\\uptc\\images"), "images");
            try {
                Files.createDirectories(projectImagesDir);
                for (File file : files) {
                    String relativePath = saveImageToProjectFolder(file, projectImagesDir);
                    if (!currentImagePaths.contains(relativePath)) {
                        currentImagePaths.add(relativePath);
                    }
                }
                refreshImageList();
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error al guardar imágenes", "No se pudo copiar las imágenes a la carpeta de proyecto: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void removeImage() {
        String selected = lstImagePaths.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Selecciona una imagen", "Por favor selecciona una imagen de la lista para eliminarla.");
            return;
        }
        currentImagePaths.remove(selected);
        refreshImageList();
    }

    @FXML
    private void saveSalon() {
        if (salon == null) {
            showAlert(Alert.AlertType.ERROR, "Error", "No se ha cargado el salón a actualizar.");
            return;
        }

        String salonName = txtSalonName.getText().trim();
        String capacityText = txtCapacity.getText().trim();
        String priceText = txtPrice.getText().trim();

        if (salonName.isEmpty() || capacityText.isEmpty() || priceText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Campos incompletos", "Por favor completa todos los campos.");
            return;
        }

        try {
            int capacity = Integer.parseInt(capacityText);
            double price = Double.parseDouble(priceText);

            Salon updatedSalon = new Salon(salon.getId(), salonName, capacity, price);
            updatedSalon.setNumberOfReservations(salon.getNumberOfReservations());
            updatedSalon.setImagePaths(new ArrayList<>(currentImagePaths));

            boolean success = salonServices.updateSalon(salon.getId(), updatedSalon);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Salón actualizado correctamente.");
                closeWindow();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "No se pudo actualizar el salón.");
            }
            
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error de formato", "Capacidad y precio deben ser valores numéricos válidos.");
        }
    }

    @FXML
    private void cancel() {
        try {
            App.setRoot("gestionSalones");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refreshImageList() {
        lstImagePaths.getItems().setAll(currentImagePaths);
        lblImageInfo.setText(currentImagePaths.size() + " foto(s) en la lista");
    }

    private String saveImageToProjectFolder(File sourceFile, Path imagesDir) throws IOException {
        String fileName = sourceFile.getName();
        Path destination = imagesDir.resolve(fileName);
        int counter = 1;
        while (Files.exists(destination)) {
            int dotIndex = fileName.lastIndexOf('.');
            String baseName = dotIndex > 0 ? fileName.substring(0, dotIndex) : fileName;
            String extension = dotIndex > 0 ? fileName.substring(dotIndex) : "";
            destination = imagesDir.resolve(baseName + "(" + counter + ")" + extension);
            counter++;
        }
        Files.copy(sourceFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
        return Paths.get("imagenes").resolve(destination.getFileName()).toString().replace("\\", "/");
    }

    private void closeWindow() {
        Stage stage = (Stage) txtSalonName.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

