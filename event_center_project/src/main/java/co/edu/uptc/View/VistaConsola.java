package co.edu.uptc.View;

import java.util.InputMismatchException;
import java.util.Scanner;

public class VistaConsola {
    private Scanner scanner;
    private Object clienteLogeado;
    private Object adminLogeado;

    public VistaConsola() {
        this.scanner = new Scanner(System.in);
        this.clienteLogeado=null;
        this.adminLogeado=null;
    }

    public void iniciarMenu(){
        boolean salir= false;
        while (!salir) {
            System.out.println("""
                    CENTR0 DE EVENTOS
                1. Portal de clientes(Reservas)
                2. Portal administrativo
                3. Salir del sistema
                -> Ingrese una opción;
                    """);
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
                        System.out.println("Saliendo del sistema");
                        salir=true;
                        break;
                    default:
                        System.out.println("Error->Por favor digite una opción válida (1-3)");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Ingrese por favor un numero, no letras.");
            }
        }
    }

    private void menuEntradaCliente(){
        boolean volver= false;
        while (!volver) {
            System.out.println("""
                    PORTAL DE CLIENTES
                1. Ya tengo una cuenta(INICIAR SESION)
                2. Soy nuevo(REGISTRARME)
                3. Volver al menú principal
                ->   """);
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
                        System.out.println("Error->Por favor digite una opción válida (1-3)");
                        break;
                }
            } catch (InputMismatchException e) {
                    System.out.println("ENTRADA NO VALIDA, Ingrese un numero");
                    scanner.nextLine();
            }
        }
    }

    public void registrarCliente(){
        boolean salir= false;
        while (!salir) {
            System.out.println("""
                    REGISTRO DE NUEVO CLIENTE
                Ingrese su numero de documento(Cedula)
                    """);
            String id= scanner.nextLine();
            System.out.println("Ingrese su nombre completo");
            String nombre= scanner.nextLine();
            System.out.println("¿Es usted un cliente empresarial? PRESIONE-->(1 para SI - 2 para NO)");
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
                        System.out.println("OPCION INCORRECTA, por favor digite unicamente 1 o 2");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Error no se aceptan letra, por favor digite 1 o 2");
            }
        }
        System.out.println("Guardando datos");
        this.clienteLogeado=new Object();
        System.out.println("Registro exitoso bienvenido");
    }

    private void loginCliente(){
        boolean autenticado= false;
        while (!autenticado) {
            System.out.println("INICIO DE SESION (CLIENTE)");
            System.out.println("Ingrese su numero de documento (Cedula)");
            System.out.println("O DIGITE 0 PARA CANCELAR Y VOLVER AL MENU ANTERIOR");
            String id = scanner.nextLine();
            if (id.equals(0)) {
                System.out.println("Cancelando inicio de sesion");
                break;
            }
        }
    }

    private void menuInternoCliente(){
        boolean cerrarSesion=false;
        while (!cerrarSesion) {
            System.out.println("""
                    PANEL DE CONTROL DE CLIENTE
                1. Solicitar nueva reserva de salón
                2. Ver mis reservas activas
                3. Cerrar sesión
                    """);
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
                        System.out.println("CERRANDO SESION");
                        this.clienteLogeado=null;
                        cerrarSesion=true;
                        break;
                    default:
                        System.out.println("OPCION INVALIDA. Seleccione las opciones(1,2,3)");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("ENTRADA NO VALIDA, Ingrese un numero");
                scanner.nextLine();
            }
        }
    }
    //MENÚS ADMINISTRADOR
    private void loginAdmin(){
        System.out.println("ACCESO ADMINISTRATIVO");
        System.out.println("Escribe su usuario:");
        String usuario= scanner.nextLine();
        System.out.println("Escribe su contraseña");
        String contrasena= scanner.nextLine();

    }

    private void menuInternoAdmin(){
        boolean cerrarSesion=false;
        while (!cerrarSesion) {
            System.out.println("""
                    PANEL DE REPORTES (ADMIN)
                1. Ver porcentaje de reservas de clientes empresariales
                2. Ver top 5 de salones mas solicitados
                3. Cerrar sesion
                    """);
            try {
                int opcion= scanner.nextInt();
                scanner.nextLine();
                switch (opcion) {
                    case 1:
                        
                        break;
                    case 2:
                        
                        break;
                    case 3:
                        System.out.println("Cerrando sesion");
                        this.adminLogeado=null;
                        cerrarSesion=true;
                        break;
                    default:
                        System.out.println("OPCION INVALIDA. Seleccione las opciones(1,2,3)");
                        break;
                }
                
            } catch (InputMismatchException e) {
                System.out.println("ENTRADA NO VALIDA, Ingrese un numero");
                scanner.nextLine();
            }
        }
    }
    
    
}
