package co.edu.uptc.viewController;

import co.edu.uptc.Model.Salon;
import co.edu.uptc.Services.SalonServices;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

public class RegisterSalonController {

    @FXML private TextField txtSalonName;
    @FXML private TextField txtCapacity;
    @FXML private TextField txtPrice;
    @FXML private Button btnRegister;
    @FXML private Button btnUploadImages;
    @FXML private Button btnBack;
    @FXML private Label lblImageInfo;

    private final SalonServices salonServices = new SalonServices();
    private final List<String> selectedImagePaths = new ArrayList<>();

    @FXML
    public void initialize() {
        Platform.runLater(() -> txtSalonName.requestFocus());
    }

    @FXML
    private void selectImages() {
        Window ownerWindow = btnUploadImages.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar fotos del salón");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        fileChooser.setInitialDirectory(new File(System.getProperty("event_center_project\\src\\main\\resources\\co\\edu\\uptc\\images")));

        List<File> files = fileChooser.showOpenMultipleDialog(ownerWindow);
        if (files != null && !files.isEmpty()) {
            Path projectImagesDir = Paths.get(System.getProperty("event_center_project\\src\\main\\resources\\co\\edu\\uptc\\images"), "imagenes");
            try {
                Files.createDirectories(projectImagesDir);
                for (File file : files) {
                    String relativePath = saveImageToProjectFolder(file, projectImagesDir);
                    if (!selectedImagePaths.contains(relativePath)) {
                        selectedImagePaths.add(relativePath);
                    }
                }
                lblImageInfo.setText(selectedImagePaths.size() + " foto(s) guardadas en imagenes/");
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Error al guardar imágenes", "No se pudo copiar las imágenes a la carpeta de proyecto: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private String saveImageToProjectFolder(File sourceFile, Path imagesDir) throws IOException {
        String fileName = sourceFile.getName();
        Path destination = imagesDir.resolve(fileName);
        int counter = 1;
        while (Files.exists(destination)) {
            String name = fileName;
            int dotIndex = fileName.lastIndexOf('.');
            String baseName = dotIndex > 0 ? fileName.substring(0, dotIndex) : fileName;
            String extension = dotIndex > 0 ? fileName.substring(dotIndex) : "";
            destination = imagesDir.resolve(baseName + "(" + counter + ")" + extension);
            counter++;
        }
        Files.copy(sourceFile.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);
        return Paths.get("imagenes").resolve(destination.getFileName()).toString().replace("\\", "/");
    }

    @FXML
    private void goBack() {
        Stage stage = (Stage) btnBack.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void registerSalon() {
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

            Salon nuevoSalon = new Salon(0, salonName, capacity, price);
            if (!selectedImagePaths.isEmpty()) {
                nuevoSalon.setImagePaths(new ArrayList<>(selectedImagePaths));
            }

            boolean added = salonServices.addNewSalon(nuevoSalon);

            if (added) {
                showAlert(Alert.AlertType.INFORMATION, "Éxito", "Salón registrado correctamente.");
                clearFields();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Ya existe un salón con ese nombre.");
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error de formato", "Capacidad y precio deben ser valores numéricos válidos.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error inesperado", "No se pudo registrar el salón: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void clearFields() {
        txtSalonName.clear();
        txtCapacity.clear();
        txtPrice.clear();
        selectedImagePaths.clear();
        lblImageInfo.setText("No hay fotos seleccionadas.");
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
