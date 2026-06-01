package co.edu.uptc.Services;

import co.edu.uptc.Model.Salon;
import co.edu.uptc.Persistence.SalonJsonRepository;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**Clase de servicios de salones que sirve para agregar, eliminar, modificar y leer salones {@link Salon}
 * 
 * @author Felipe Becerra
 * @version v 1.0
 */
public class SalonServices {

    private List<Salon> listSalones;
    private SalonJsonRepository repository;
    private final String IMAGES_BASE_PATH = "../images/";
    public SalonServices() {
        File folder = new File(IMAGES_BASE_PATH);
        if (!folder.exists()) {
        folder.mkdirs(); // Esto creará la carpeta si por alguna razón no existe
        }
        this.repository= new SalonJsonRepository("Salon.json");
        this.listSalones= repository.sendSalonList();
        if (this.listSalones==null) {
            this.listSalones= new ArrayList<>();
        }
    }
    public SalonServices(List<Salon> listSalonesSimulada) {
        File folder = new File(IMAGES_BASE_PATH);
        if (!folder.exists()) {
        folder.mkdirs(); // Esto creará la carpeta si por alguna razón no existe
    }
        this.listSalones = listSalonesSimulada;
        this.repository = null; 
    }

    /**Método que guarda un nuevo salón en la lista y en el archivo json
     * 
     * @param salon el nuevo salón que se ingresa
     */
    public boolean addNewSalon(Salon salon){
        if (buscarSalonPorNombre(salon.getSalonName()) != null) {
            return false; // El salón ya existe
        }
        salon.setId(generateNewId());
        listSalones.add(salon);
        if (this.repository != null) {
            repository.saveSalonList(listSalones);
        }
        return true;
    }

    /**Método que envía un salón através de su id
     * 
     * @param id identificador númerico del salón
     * @return  un objeto salon
     */
    public Salon sendSalonById(int id ){
        return listSalones.stream().filter(s->s.getId()==id).findFirst().orElse(null);
    }

    /**Método para buscar un salón atraves de su id 
     * importante para evitar excepciones si no se encuentra en la lista
     * 
     * @param id identificador númerico del salón
     * @return  verdadero si se encontro el salón
     * @return falso si no se encontro el salón
     */
    public boolean searchSalonById(int id){
        if(sendSalonById(id)!=null){
            return true;
        }else{
            return false;
        }
    }

    /**Método que busca la posición del salón en la lista a traves de su id
     * importante para eliminar y actualizar salones
     * 
     * @param id identificador númerico del salón
     * @return la posición del salón en la lista si lo encuentra
     * @return una posición superior a la lista;
     */
    public int sendSalonPosition(int id){
        int position= listSalones.size()+1;
        for (int i = 0; i < listSalones.size(); i++) {
            if(id==listSalones.get(i).getId()){
                position=i;
            }
        }
        return position;
    }
    
    /**Método para eliminar un salón a través de su posición en la lista
     * Hace uso del método sendSalonPosition para enviarle la posición con base en la id
     * @param id identificador númerico del salón
     */
    public void deleteSalon(int id){
        listSalones.removeIf(salon -> salon.getId()==id);
        if (this.repository != null) {
            repository.saveSalonList(listSalones);
        }
    }

    /**
     * Modifica los datos de un salón existente.
     * @param id El ID del salón que se desea modificar.
     * @param updatedSalon el salón modificado
     * @param listSalons lista de salones donde se hara la modificación
     */
    public boolean updateSalon(int id,Salon updatedSalon) {
        Salon salonEncontrado = sendSalonById(id);
        if (salonEncontrado != null) {
            for (int i = 0; i < listSalones.size(); i++) {
                if (listSalones.get(i).getId() == id) {
                    updatedSalon.setId(id);
                    listSalones.set(i, updatedSalon);
                    if (this.repository != null) {
                        repository.saveSalonList(listSalones);
                    }
                    return true;
                }
            }   
        }
        return false;
    }

    /**Método para generar de forma automatica el siguiente identificador númerico de salón
     * 
     * @return el nuevo identificador númerico de salón
     */
    public int generateNewId(){
        int biggestId=0;
        for (int i = 0; i < listSalones.size(); i++) {
            if(listSalones.get(i).getId()>biggestId){
                biggestId=listSalones.get(i).getId();
            }
        }
        return biggestId+1;
    }

    /**
     * Busca un salón específico por su nombre (ignorando mayúsculas y minúsculas).
     * @param nombre El nombre del salón a buscar.
     * @return El objeto Salon si lo encuentra, de lo contrario retorna null.
     */
    public Salon buscarSalonPorNombre(String nombre) {
        for (Salon salon : listSalones) {
            if (salon.getSalonName().equalsIgnoreCase(nombre)) {
                return salon;
            }
        }
        return null;
    }

    public List<Salon> getListSalones() {
        return this.listSalones;
    }
    public void addImageToSalon(int salonId, File imageFile) throws IOException {
    // Usamos el método que ya tienes en SalonServices, no el repositorio
    Salon salon = sendSalonById(salonId); 
    
    if (salon == null) return;

    // 1. Crear nombre único y ruta de destino
    String fileName = System.currentTimeMillis() + "_" + imageFile.getName();
    Path destinationPath = Paths.get(IMAGES_BASE_PATH, fileName);

    // 2. Copiar físicamente
    Files.copy(imageFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

    // 3. Actualizar modelo
    salon.addImagePath(destinationPath.toString());

    // 4. Persistir los cambios usando el repositorio que ya tienes (this.repository)
    if (this.repository != null) {
        repository.saveSalonList(listSalones);
    }
}
    
}
