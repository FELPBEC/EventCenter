package co.edu.uptc.Interfaces;

import java.util.List;
/**
 * Interfaz genérica que sirve como definición general o guía general para nuestros repositorios.
 * @param <T> Es un tipo genérico de datos, donde dice que nuestro repositorio manejará objetos de tipo type.
 * @author Julian Moreno
 */
public interface Repository<T>{

    /**
     * Se encarga de guardar un objeto de tipo Type trayendo una lista del archivo de persistencia donde se guardan los objetos.
     * Añadiendo ese objeto tipo type a esa lista para luego, en base a esa nueva lista sobreescribir el archivo de persistencia.
     * Si el archivo no existe, lo crea.
     * @param entity el objeto tipo Type que va a guardar.
     */
    public void save(T entity);
    /**
     *Lee el archivo de persistencia y luego, a través de un objeto Gson, lo convertimos a una lista indicándole el tipo de clase de los objetos almacenados.
     * @return Una lista con los objetos que estaban almacenados en el archivo de persistencia.
     * Si el archivo está vacío o hay un error al leer el archivo, devuelve una lista vacía.
     */
    public List<T> findAll();
    /**
     * Se encarga de guardar toda una lista con objetos editados y la remplaza por la lista original, actualizandola.
     * @param list Es la nueva lista con la que vamos a remplazar a la anterior.
     */
    public void updateAll(List<T> list);

    /**Método que actualiza un objeto en concreto sobreescribiendolo 
     * 
     * @param position posición del objeto que se desea modificar en la lista
     * @param entity   el objeto modificado
     */
    public void updateNew(int position, T entity);

    /**Elimina un objeto de la lista según una posición dada
     * 
     * @param position la posición del objeto que se desea eliminar
     */
    public void deleteObject(int position);


}
