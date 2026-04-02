package co.edu.uptc.Persistence;

import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import co.edu.uptc.Interfaces.Repository;
/**
 * Se implementó la interfaz de Repository para gestionar la persistencia en archivos
 * de datos a través de archivos JSON.
 * @param <T> Es un tipo genérico de datos, donde dice que nuestro repositorio manejará objetos de tipo type.
 */
public class JsonRepository<T> implements Repository<T> {
    private String fileName;
    private Gson gson;
    private Type typeClass;
    /**
     * Constructor de la clase JsonRepository.
     * @param fileName Es el nombre del archivo donde se van a guardar los objetos o datos.
     * @param typeClass Indica el tipo de clase de los objetos o datos que vamos a guardar para que al generar una lista sea del tipo correspondiente.
     */
    public JsonRepository(String fileName, Type typeClass) {
        this.fileName = fileName;
        this.typeClass = typeClass;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }
    @Override
    public void save(T entity){
        List<T> listObjects= findAll();
        if (listObjects==null) {
            listObjects=new ArrayList<>();
        }
        listObjects.add(entity);
        try(FileWriter writer= new FileWriter(fileName)) {
            gson.toJson(listObjects,writer);

        } catch (Exception e) {
            System.err.println("Error al escribir en el archivo json "+fileName+" :"+e.getMessage());
        }
    }
    
    @Override
    public List<T> findAll(){
        try(FileReader reader= new FileReader(fileName)) {
            List<T> listObjects= gson.fromJson(reader, typeClass);
            return listObjects != null ? listObjects : new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Atención: Error al leer el archivo " + fileName + " - " + e.getMessage());
            return new ArrayList<>();
        }
    }
    @Override
    public void updateAll(List<T> list){
        try (FileWriter writer = new FileWriter(fileName)){
            gson.toJson(list,writer);
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
