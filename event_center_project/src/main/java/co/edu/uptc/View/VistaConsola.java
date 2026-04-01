package co.edu.uptc.View;

import java.util.InputMismatchException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class VistaConsola {
    private Scanner scanner;
    private Object clienteLogeado;
    private Object adminLogeado;
    private ResourceBundle mensajes;

    public VistaConsola() {
        this.scanner = new Scanner(System.in);
        this.clienteLogeado=null;
        this.adminLogeado=null;
        Locale idiomaInicial= Locale.of("es");
        this.mensajes= ResourceBundle.getBundle("co.edu.uptc.Resources.textos", idiomaInicial);
    }

    private void cambiarIdioma(String idioma){
        Locale nuevoIdioma= Locale.of(idioma);
        this.mensajes= ResourceBundle.getBundle("co.edu.uptc.Resources.textos", nuevoIdioma);
    }

    public void seleccionarIdioma(){
        boolean idiomaValido=false;
        while (!idiomaValido) {
            System.out.println("""
                    Seleccione su idioma / Select your language
                1. Español / Spanish
                2. Ingles / English
                    """);
            try {
                int opIdioma= scanner.nextInt();
                switch (opIdioma) {
                    case 1:
                        cambiarIdioma("es");
                        idiomaValido=true;
                        break;
                    case 2:
                        cambiarIdioma("en");
                        idiomaValido=true;
                        break;
                    default:
                        System.out.println("Opcion invalida / Invalid option (1 o 2) ");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Ingrese por favor un numero, no letras.");
                scanner.nextLine();
            }
        }
    }


    public void iniciarMenu(){
        boolean salir= false;
        while (!salir) {
            System.out.println(mensajes.getString("menu.principal.titulo"));
            System.out.println(mensajes.getString("menu.principal.op1"));
            System.out.println(mensajes.getString("menu.principal.op2"));
            System.out.println(mensajes.getString("menu.principal.op3"));
            System.out.print("->");
            try {
                int opcion= scanner.nextInt();
                scanner.nextLine();
                switch (opcion) {
                    case 1:
                        menuEntradaCliente();
                        break;
                    case 2:
                        loginAdmin();
                        break;
                    case 3:
                        System.out.println(mensajes.getString("general.saliendo"));
                        salir=true;
                        break;
                    default:
                        System.out.println(mensajes.getString("error.opcion.rango"));
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println(mensajes.getString("error.letras"));
                scanner.nextLine();
            }
        }
    }

    private void menuEntradaCliente(){
        boolean volver= false;
        while (!volver) {
            System.out.println(mensajes.getString("menu.cliente.titulo"));
            System.out.println(mensajes.getString("menu.cliente.op1"));
            System.out.println(mensajes.getString("menu.cliente.op2"));
            System.out.println(mensajes.getString("menu.cliente.op3"));
            try {
                int opcion2 = scanner.nextInt();
                scanner.nextLine();

                switch (opcion2) {
                    case 1:
                        loginCliente();
                        break;
                    case 2:
                        registrarCliente();
                        break;
                    case 3:
                        volver=true;
                        break;
                    default:
                        System.out.println(mensajes.getString("error.opcion.rango"));
                        break;
                }
            } catch (InputMismatchException e) {
                    System.out.println(mensajes.getString("error.letras"));
                    scanner.nextLine();
            }
        }
    }

    public void registrarCliente(){
        boolean salir= false;
        while (!salir) {
            System.out.println(mensajes.getString("registro.titulo"));
            System.out.println(mensajes.getString("registro.pedir.cedula"));
            String id= scanner.nextLine();
            System.out.println(mensajes.getString("registro.pedir.nombre"));
            String nombre= scanner.nextLine();
            System.out.println(mensajes.getString("registro.pedir.empresarial"));
            boolean esEmpresarial= false;
            try {
                int opEmpresarial=scanner.nextInt();
                scanner.nextLine();
                switch (opEmpresarial) {
                    case 1:
                        esEmpresarial=true;
                        salir=true;
                        break;
                    case 2:
                        esEmpresarial=false;
                        salir=true;
                        break;
                    default:
                        System.out.println(mensajes.getString("error.opcion.binaria"));
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println(mensajes.getString("error.letras.binaria"));
            }
        }
        System.out.println(mensajes.getString("registro.guardando"));
        this.clienteLogeado=new Object();
        System.out.println(mensajes.getString("registro.exito"));
    }

    private void loginCliente(){
        boolean autenticado= false;
        while (!autenticado) {
            System.out.println(mensajes.getString("login.cliente.titulo"));
            System.out.println(mensajes.getString("login.pedir.cedula"));
            System.out.println(mensajes.getString("login.cancelar"));
            String id = scanner.nextLine();
            if (id.equals("0")) {
                System.out.println(mensajes.getString("login.cancelando"));
                break;
            }
        }
    }

    private void menuInternoCliente(){
        boolean cerrarSesion=false;
        while (!cerrarSesion) {
            System.out.println(mensajes.getString("panel.cliente.titulo"));
            System.out.println(mensajes.getString("panel.cliente.op1"));
            System.out.println(mensajes.getString("panel.cliente.op2"));
            System.out.println(mensajes.getString("panel.cliente.op3"));
            System.out.print("->");
            try {
                int opcion=scanner.nextInt();
                scanner.nextLine();
                switch (opcion) {
                    case 1:
                        
                        break;
                    case 2:
                        
                        break;
                    case 3:
                        System.out.println(mensajes.getString("general.cerrando.sesion"));
                        this.clienteLogeado=null;
                        cerrarSesion=true;
                        break;
                    default:
                        System.out.println(mensajes.getString("error.opcion.rango"));
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println(mensajes.getString("error.letras"));
                scanner.nextLine();
            }
        }
    }
    //MENÚS ADMINISTRADOR
    private void loginAdmin(){
        System.out.println(mensajes.getString("login.admin.titulo"));
        System.out.println(mensajes.getString("login.admin.usuario"));
        String usuario= scanner.nextLine();
        System.out.println(mensajes.getString("login.admin.contrasena"));
        String contrasena= scanner.nextLine();

    }

    private void menuInternoAdmin(){
        boolean cerrarSesion=false;
        while (!cerrarSesion) {
            System.out.println(mensajes.getString("panel.admin.titulo"));
            System.out.println(mensajes.getString("panel.admin.op1"));
            System.out.println(mensajes.getString("panel.admin.op2"));
            System.out.println(mensajes.getString("panel.admin.op3"));
            try {
                int opcion= scanner.nextInt();
                scanner.nextLine();
                switch (opcion) {
                    case 1:
                        
                        break;
                    case 2:
                        
                        break;
                    case 3:
                        System.out.println(mensajes.getString("general.cerrando.sesion"));
                        this.adminLogeado=null;
                        cerrarSesion=true;
                        break;
                    default:
                        System.out.println(mensajes.getString("error.opcion.rango"));
                        break;
                }
                
            } catch (InputMismatchException e) {
                System.out.println(mensajes.getString("error.letras"));
                scanner.nextLine();
            }
        }
    }
    
    
}
