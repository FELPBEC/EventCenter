package co.edu.uptc.View;

import java.util.InputMismatchException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

import co.edu.uptc.Model.Admin;
import co.edu.uptc.Model.Client;
import co.edu.uptc.Services.AdminServices;
import co.edu.uptc.Services.BookingServices;
import co.edu.uptc.Services.ClientService;
import co.edu.uptc.Services.DateConvertor;
import co.edu.uptc.Services.ExportadorService;

public class VistaConsola {
    private Scanner scanner;
    private Client clienteLogeado;
    private Admin adminLogeado;
    private ResourceBundle mensajes;
    private AdminServices adminServices;
    private ClientService clientService;
    private BookingServices bookingServices;
    private ExportadorService exportadorService;

    public VistaConsola() {
        this.scanner = new Scanner(System.in);
        this.clienteLogeado=null;
        this.adminLogeado=null;
        this.adminServices= new AdminServices();
        this.clientService= new ClientService();
        this.bookingServices= new BookingServices();
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
                System.out.println("""
                        Ingrese por favor un numero no letras
            
                        Please enter a number, not letters
                        """);
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
            System.out.println(mensajes.getString("registro.titulo"));
            System.out.println(mensajes.getString("registro.pedir.cedula"));
            int id= scanner.nextInt();
            scanner.nextLine();
            System.out.println(mensajes.getString("registro.pedir.nombre"));
            String nombre= scanner.nextLine();
            System.out.println(mensajes.getString("registro.pedir.contrasena"));
            String contrasena= scanner.nextLine();
            System.out.println(mensajes.getString("registro.pedir.correo"));
            String correo= scanner.nextLine();
            System.out.println(mensajes.getString("registro.pedir.telefono"));
            String telefono= scanner.nextLine();
            boolean esEmpresarial = false;
            boolean tipoValido = false;

            while (!tipoValido) {
                System.out.println(mensajes.getString("registro.pedir.empresarial"));
                try {
                    int tipo = scanner.nextInt();
                    scanner.nextLine();
                    switch (tipo) {
                        case 1:
                            esEmpresarial = true;
                            tipoValido = true;
                            break;
                        case 2:
                            esEmpresarial = false;
                            tipoValido = true;
                            break;
                        default:
                            System.out.println(mensajes.getString("error.opcion.binaria"));
                            break;
                    }
                } catch (InputMismatchException e) {
                    System.out.println(mensajes.getString("error.letras.binaria"));
                    scanner.nextLine();
                }
            }
            System.out.println(mensajes.getString("registro.guardando"));
            Client cliente= new Client(nombre,id,contrasena,telefono,correo,esEmpresarial);
            boolean exito= clientService.registrarCliente(cliente);
            if (exito) {
                System.out.println(mensajes.getString("registro.exito"));
                this.clienteLogeado=cliente;
                menuInternoCliente();
            }else{
                System.out.println(mensajes.getString("registro.error"));
            }
    }

    private void loginCliente(){
        boolean autenticado= false;
        while (!autenticado) {
            System.out.println(mensajes.getString("login.cliente.titulo"));
            System.out.println(mensajes.getString("login.cancelar"));
            System.out.println(mensajes.getString("login.pedir.cedula"));
            int id = scanner.nextInt();
            scanner.nextLine();
            System.out.println(mensajes.getString("login.pedir.contrasena"));
            String constrasena= scanner.nextLine();
            if (id==0||constrasena.equals("0")) {
                System.out.println(mensajes.getString("login.cancelando"));
                break;
            }
            if (clientService.validateAccess(id, constrasena)) {
                System.out.println(mensajes.getString("login.exito"));
                this.clienteLogeado=clientService.buscarClientPorId(id);
                menuInternoCliente();
                autenticado=true;
            }else{
                System.out.println(mensajes.getString("general.error.sesion"));
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
            System.out.println(mensajes.getString("panel.cliente.op4")); 
            System.out.print(mensajes.getString("general.ingreso.opcion"));
            try {
                int opcion=scanner.nextInt();
                scanner.nextLine();
                switch (opcion) {
                    case 1:
                        System.out.println("\n>> [SIMULACION] Lista de salones filtrados...");
                        break;
                    case 2:
                        System.out.println("\n>> [SIMULACION] Proceso de nueva reserva...");
                        break;
                    case 3:
                        System.out.println("\n>> [SIMULACION] Mostrando historial de reservas...");
                        break;
                    case 4:
                        System.out.println(mensajes.getString("general.cerrando.sesion"));
                        this.clienteLogeado=null;
                        cerrarSesion=true;
                        break;
                    default:
                        System.out.println(mensajes.getString("error.opcion.rango.4"));
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
        System.out.println(mensajes.getString("login.admin.cedula"));
        int cedula= scanner.nextInt();
        scanner.nextLine();
        System.out.println(mensajes.getString("login.admin.contrasena"));
        String contrasena= scanner.nextLine();
        if (adminServices.validateAccess(cedula, contrasena)) {
            this.adminLogeado=adminServices.sendAdminById(cedula);
            System.out.println(mensajes.getString("login.exito"));
            menuInternoAdmin();
        }else{
            System.out.println(mensajes.getString("general.error.sesion"));
        }
    }

    private void menuInternoAdmin(){
        boolean cerrarSesion=false;
        while (!cerrarSesion) {
            System.out.println(mensajes.getString("panel.admin.titulo"));
            System.out.println(mensajes.getString("panel.admin.op1"));
            System.out.println(mensajes.getString("panel.admin.op2"));
            System.out.println(mensajes.getString("panel.admin.op3"));
            System.out.println(mensajes.getString("panel.admin.op4"));
            System.out.println(mensajes.getString("panel.admin.op5"));
            System.out.print(mensajes.getString("general.ingreso.opcion"));
            try {
                int opcion= scanner.nextInt();
                scanner.nextLine();
                switch (opcion) {
                    case 1:
                        menuGestionClientesAdmin();
                        break;
                    case 2:
                        menuGestionSalonesAdmin();
                        break;
                    case 3:
                        menuReportesAdmin();
                        break;
                    case 5:
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

    private void menuGestionAdministradores(){
        boolean volver= false;
        while (!volver) {
            
        }
    }

    private void menuGestionClientesAdmin(){
        boolean volver= false;
        while (!volver) {
            System.out.println(mensajes.getString("admin.clientes.titulo"));
            System.out.println(mensajes.getString("admin.clientes.op1"));
            System.out.println(mensajes.getString("admin.clientes.op2"));
            System.out.println(mensajes.getString("admin.clientes.op3"));
            System.out.println(mensajes.getString("admin.sub.volver"));
            System.out.println(mensajes.getString("general.ingreso.opcion"));
            int op= scanner.nextInt();
            switch (op) {
                case 1: 
                    System.out.println("Mostrando todos los clientes en base de datos..."); 
                    break;
                case 2 : 
                    System.out.println(mensajes.getString("admin.clientes.op2.id"));
                    int idcliente= scanner.nextInt();
                    scanner.nextLine();
                    if (clientService.buscarClientPorId(idcliente)!=null) {
                        System.out.println(mensajes.getString("admin.clientes.op2.aviso"));
                        scanner.nextLine();
                        System.out.println(mensajes.getString("registro.pedir.nombre"));
                        String nombre= scanner.nextLine();
                        System.out.println(mensajes.getString("registro.pedir.contrasena"));
                        String contrasenaNueva= scanner.nextLine();
                        System.out.println(mensajes.getString("registro.pedir.correo"));
                        String correo= scanner.nextLine();
                        System.out.println(mensajes.getString("registro.pedir.telefono"));
                        String telefono= scanner.nextLine();
                        boolean esEmpresarial = false;
                        boolean tipoValido = false;
                        while (!tipoValido) {
                            System.out.println(mensajes.getString("registro.pedir.empresarial"));
                            try {
                                int tipo = scanner.nextInt();
                                scanner.nextLine();
                                switch (tipo) {
                                    case 1:
                                        esEmpresarial = true;
                                        tipoValido = true;
                                        break;
                                    case 2:
                                        esEmpresarial = false;
                                        tipoValido = true;
                                        break;
                                    default:
                                        System.out.println(mensajes.getString("error.opcion.binaria"));
                                        break;
                                }
                            } catch (InputMismatchException e) {
                                System.out.println(mensajes.getString("error.letras.binaria"));
                                scanner.nextLine();
                            }
                        }
                        if (clientService.modificarCliente(idcliente, nombre, contrasenaNueva, telefono, correo, esEmpresarial)) {
                            System.out.println(mensajes.getString("general.modificado.exito"));
                        }else{
                            System.out.println(mensajes.getString("general.modificado.error"));
                        }
                    }else{
                        System.out.println(mensajes.getString("general.modificado.error"));
                    }
                    break;
                case 3: 
                    System.out.println(mensajes.getString("admin.cliente.op3.id"));
                    int id=scanner.nextInt();
                    if (clientService.eliminarCliente(id)) {
                        System.out.println(mensajes.getString("general.delete.exito"));
                    }else{
                        System.out.println("general.delete.error");
                    }
                    break;
                case 4: 
                    volver = true; break;
                default: 
                    System.out.println(mensajes.getString("admin.sub.error")); break;
            }
        }
    }
    
    private void menuGestionSalonesAdmin() {
        boolean volver = false;
        while(!volver){
            System.out.println("\n" + mensajes.getString("admin.salones.titulo"));
            System.out.println(mensajes.getString("admin.salones.op1"));
            System.out.println(mensajes.getString("admin.salones.op2"));
            System.out.println(mensajes.getString("admin.salones.op3"));
            System.out.println(mensajes.getString("admin.sub.volver"));
            System.out.print(mensajes.getString("general.ingreso.opcion"));
            
            int op = scanner.nextInt();
            switch (op) {
                case 1: 
                    System.out.println("Pidiendo Codigo, Nombre, Capacidad, y Precio..."); 
                    break;
                case 2:
                    System.out.println("Listando salones..."); 
                    break;
                case 3: 
                    System.out.println("Ingrese codigo de salon para cambiar estado..."); 
                    break;
                case 4: 
                    volver = true; 
                    break;
                default: 
                    System.out.println(mensajes.getString("admin.sub.error")); 
                    break;
            }
        }
    }

    private void menuReportesAdmin() {
        boolean volver = false;
        while(!volver){
            System.out.println("\n" + mensajes.getString("admin.reportes.titulo"));
            System.out.println(mensajes.getString("admin.reportes.op1"));
            System.out.println(mensajes.getString("admin.reportes.op2"));
            System.out.println(mensajes.getString("admin.reportes.op3"));
            System.out.println(mensajes.getString("admin.sub.volver"));
            System.out.print(mensajes.getString("general.ingreso.opcion"));
            
            int op = scanner.nextInt();
            switch (op) {
                case 1: 
                    System.out.println("Ingrese fecha inicio y fin. Calculando total..."); 
                    break;
                case 2: 
                    System.out.println("1. Salon VIP | 2. Salon Esmeralda | ..."); 
                    break;
                case 3: 
                    System.out.println("Archivo exportado exitosamente."); 
                    break;
                case 4: 
                    volver = true; 
                    break;
                default:
                    System.out.println(mensajes.getString("admin.sub.error")); 
                    break;
            }
        }
    }
}
