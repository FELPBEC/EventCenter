package co.edu.uptc.View;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

import co.edu.uptc.Model.Admin;
import co.edu.uptc.Model.Booking;
import co.edu.uptc.Model.Client;
import co.edu.uptc.Model.Salon;
import co.edu.uptc.Services.AdminServices;
import co.edu.uptc.Services.BookingServices;
import co.edu.uptc.Services.ClientService;
import co.edu.uptc.Services.SalonServices;
import co.edu.uptc.Util.DateConvertor;
import co.edu.uptc.Util.ExportadorService;
import co.edu.uptc.Util.RankedSalon;

public class VistaConsola {
    private Scanner scanner;
    private Client clienteLogeado;
    private Admin adminLogeado;
    private ResourceBundle mensajes;
    private AdminServices adminServices;
    private ClientService clientService;
    private SalonServices salonServices;
    private BookingServices bookingServices;
    private ExportadorService exportadorService;
    private RankedSalon rankedSalon;
    public VistaConsola() {
        this.scanner = new Scanner(System.in);
        this.clienteLogeado=null;
        this.adminLogeado=null;
        this.adminServices= new AdminServices();
        this.clientService= new ClientService();
        this.bookingServices= new BookingServices();
        this.salonServices= new SalonServices();
        Locale idiomaInicial= Locale.of("es");
        this.rankedSalon= new RankedSalon();
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
                        System.out.println("\n Lista de salones filtrados...");
                        break;
                    case 2:
                        System.out.println("\n--- " + mensajes.getString("panel.cliente.op2") + " ---");
                        System.out.println(mensajes.getString("cliente.reserva.pedir.salon"));
                        int idSalon = scanner.nextInt();
                        scanner.nextLine(); 
                        Salon salonElegido = salonServices.sendSalonById(idSalon);
                        if (salonElegido != null) {
                            System.out.println(mensajes.getString("cliente.reserva.pedir.fecha"));
                            String fechaInicioStr = scanner.nextLine();
                            int horas = 0;
                            boolean horasValidas = false;
                            while (!horasValidas) {
                                try {
                                    System.out.println(mensajes.getString("cliente.reserva.pedir.horas"));
                                    horas = scanner.nextInt();
                                    scanner.nextLine();
                                    horasValidas = true;
                                } catch (InputMismatchException e) {
                                    System.out.println("\n" + mensajes.getString("error.letras"));
                                    scanner.nextLine();
                                }
                            }
                            DateConvertor convertor = new DateConvertor();
                            try {
                                java.time.LocalDateTime fechaInicio = convertor.StringToLocalDateTime(fechaInicioStr);
                                java.time.LocalDateTime fechaFin = fechaInicio.plusHours(horas);
                                String fechaFinStr = convertor.localDateTimeToString(fechaFin);
                                int nuevoId = bookingServices.sendNewId();
                                Booking nuevaReserva = new Booking(nuevoId, this.clienteLogeado, salonElegido, fechaInicioStr, horas, fechaFinStr);
                                nuevaReserva.setPrice(bookingServices.calculatePriceBooking(nuevaReserva));
                                bookingServices.saveNewBooking(nuevaReserva);
                                System.out.println("\n>> " + mensajes.getString("cliente.reserva.exito") + " " + fechaFinStr);
                                System.out.println(mensajes.getString("booking.services.mostrar.precio")+" $"+nuevaReserva.getPrice());
                            } catch (java.time.format.DateTimeParseException e) {
                                System.out.println("\n>> " + mensajes.getString("cliente.reserva.error.fecha"));
                            }
                        } else {
                            System.out.println("\n>> " + mensajes.getString("cliente.reserva.error.salon"));
                        }
                        break;
                    case 3:
                        List<Booking> listReservas =bookingServices.sendBookingListByClient(this.clienteLogeado.getId());
                        if (listReservas.isEmpty()) {
                            System.out.println(mensajes.getString("panel.cliente.op3.vacio"));
                        } else {
                            for (Booking reserva : listReservas) {
                                System.out.println("-------------------------------------------------");
                                System.out.println(mensajes.getString("booking.services.mostrar.id") + " " + reserva.getId());
                                System.out.println(mensajes.getString("booking.services.mostrar.fecha.inicio") + " " + reserva.getStartDate());
                                System.out.println(mensajes.getString("booking.services.mostrar.horas.duracion") + " " + reserva.getAmountOfHours());
                                System.out.println(mensajes.getString("booking.services.mostrar.fecha.finalizacion") +" "+reserva.getEndDate());
                                System.out.println(mensajes.getString("booking.services.mostrar.precio")+" $"+reserva.getPrice());
                                System.out.println("-------------------------------------------------\n");
                                System.out.println(mensajes.getString("salon.services.mostrar.clase"));
                                Salon salon=reserva.getSalon();
                                System.out.println("-------------------------------------------------");
                                System.out.println(mensajes.getString("salon.services.mostrar.id") + " " + salon.getId());
                                System.out.println(mensajes.getString("salon.services.mostrar.nombre") + " " + salon.getSalonName());
                                System.out.println(mensajes.getString("salon.services.mostrar.capacidad") + " " + salon.getCapacity());
                                System.out.println(mensajes.getString("salon.services.mostrar.precio.por.hora") + salon.getPriceByHour());
                                System.out.println(mensajes.getString("salon.services.mostrar.numero.reservaciones") + " " + salon.getNumberOfReservations());

                            }
                            System.out.println("-------------------------------------------------\n");
                        }
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
                    List<Client> listClient= clientService.obtenerListaClientes();
                    if (listClient.isEmpty()) {
                        System.out.println(mensajes.getString("admin.salones.op2.vacio"));
                    } else {
                        for (Client client : listClient) {
                            System.out.println("-------------------------------------------------");
                            System.out.println(mensajes.getString("client.services.mostrar.id") + " " + client.getId());
                            System.out.println(mensajes.getString("client.services.mostrar.nombre") + " " + client.getUserName());
                            System.out.println(mensajes.getString("client.services.mostrar.correo") + " " + client.getEmail());
                            System.out.println(mensajes.getString("client.services.mostrar.numero.telefonico") + " " + client.getPhoneNumber());
                            String esEmpresa;
                            if (client.isEmpresarial()) {
                                esEmpresa="SI/YES";
                            }else{
                                esEmpresa="NO";
                            }
                            System.out.println(mensajes.getString("client.services.mostrar.empresarial") + " " + esEmpresa);
                        }
                        System.out.println("-------------------------------------------------\n");
                    }
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
            try {
                int op = scanner.nextInt();
                scanner.nextLine();
                switch (op) {
                    case 1: 
                        int id= salonServices.generateNewId();
                        System.out.println("\n--- " + mensajes.getString("admin.salones.op1").toUpperCase() + " ---");
                        System.out.println(mensajes.getString("registro.salon.pedir.nombre"));
                        String nombreSalon = scanner.nextLine();
                        
                        int capacidad = 0;
                        double precio = 0.0;
                        boolean datosValidos = false;
                        while (!datosValidos) {
                            try {
                                System.out.println(mensajes.getString("registro.salon.pedir.capacidad"));
                                capacidad = scanner.nextInt();
                                
                                System.out.println(mensajes.getString("registro.salon.pedir.precio"));
                                precio = scanner.nextDouble();
                                scanner.nextLine();
                                datosValidos = true;
                            } catch (InputMismatchException e) {
                                System.out.println("\n" + mensajes.getString("error.letras"));
                                scanner.nextLine();
                            }
                        }
                        Salon nuevoSalon = new Salon(id,nombreSalon, capacidad, precio);
                        boolean guardadoExitoso = salonServices.addNewSalon(nuevoSalon);
                        
                        if (guardadoExitoso) {
                            System.out.println("\n>> " + mensajes.getString("registro.salon.exito"));
                        } else {
                            System.out.println("\n>> " + mensajes.getString("registro.salon.error"));
                        }
                        break;
                    case 2:
                        List<Salon> listSalones = salonServices.enlistSalons();
                        if (listSalones.isEmpty()) {
                            System.out.println("\n>> No hay salones registrados actualmente en el sistema.\n");
                        } else {
                            for (Salon salon : listSalones) {
                                System.out.println("-------------------------------------------------");
                                System.out.println(mensajes.getString("salon.services.mostrar.id") + " " + salon.getId());
                                System.out.println(mensajes.getString("salon.services.mostrar.nombre") + " " + salon.getSalonName());
                                System.out.println(mensajes.getString("salon.services.mostrar.capacidad") + " " + salon.getCapacity());
                                System.out.println(mensajes.getString("salon.services.mostrar.precio.por.hora") + " $" + salon.getPriceByHour());
                                System.out.println(mensajes.getString("salon.services.mostrar.numero.reservaciones") + " " + salon.getNumberOfReservations());
                            }
                            System.out.println("-------------------------------------------------\n");
                        }
                        break;
                    case 3: 
                        System.out.println("\n--- " + mensajes.getString("admin.salones.op3") + " ---");
                        System.out.println(mensajes.getString("modificar.salon.pedir.id"));
                        int idModificar = scanner.nextInt();
                        scanner.nextLine();
                        if (salonServices.searchSalonById(idModificar)) {
                            System.out.println(mensajes.getString("modificar.salon.aviso"));
                            System.out.println(mensajes.getString("registro.salon.pedir.nombre"));
                            String nuevoNombre = scanner.nextLine();
                            int nuevaCapacidad = 0;
                            double nuevoPrecio = 0.0;
                            boolean datosNuevosValidos = false;
                            while (!datosNuevosValidos) {
                                try {
                                    System.out.println(mensajes.getString("registro.salon.pedir.capacidad"));
                                    nuevaCapacidad = scanner.nextInt();
                                    
                                    System.out.println(mensajes.getString("registro.salon.pedir.precio"));
                                    nuevoPrecio = scanner.nextDouble();
                                    scanner.nextLine(); 
                                    datosNuevosValidos = true;
                                } catch (InputMismatchException e) {
                                    System.out.println("\n" + mensajes.getString("error.letras"));
                                    scanner.nextLine();
                                }
                            }
                            if (salonServices.modificarSalon(idModificar, nuevoNombre, nuevaCapacidad, nuevoPrecio)) {
                                System.out.println("\n>> " + mensajes.getString("modificar.salon.exito"));
                            } else {
                                System.out.println("\n>> " + mensajes.getString("modificar.salon.error"));
                            }
                        } else {
                            System.out.println("\n>> " + mensajes.getString("modificar.salon.error"));
                        }
                        break;
                    case 4: 
                        volver = true; 
                        break;
                    default: 
                        System.out.println(mensajes.getString("admin.sub.error")); 
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println(mensajes.getString("error.letras"));
                scanner.nextLine();
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
            scanner.nextLine();
            switch (op) {
                case 1: 
                    System.out.println("Ingrese fecha inicio y fin. Calculando total..."); 
                    break;
                case 2: 
                    List<Salon> listSalones = rankedSalon.sendTop5BestSalons();
                        if (listSalones.isEmpty()) {
                            System.out.println("admin.salones.op2.vacio");
                        } else {
                            for (Salon salon : listSalones) {
                                System.out.println("-------------------------------------------------");
                                System.out.println(mensajes.getString("salon.services.mostrar.id") + " " + salon.getId());
                                System.out.println(mensajes.getString("salon.services.mostrar.nombre") + " " + salon.getSalonName());
                                System.out.println(mensajes.getString("salon.services.mostrar.capacidad") + " " + salon.getCapacity());
                                System.out.println(mensajes.getString("salon.services.mostrar.precio.por.hora") + " $" + salon.getPriceByHour());
                                System.out.println(mensajes.getString("salon.services.mostrar.numero.reservaciones") + " " + salon.getNumberOfReservations());
                            }
                            System.out.println("-------------------------------------------------\n");
                        };
                    break;
                case 3:
                    System.out.println("\n--- EXPORTAR REPORTE DE INGRESOS ---");
                    try {
                        System.out.println("Ingrese la fecha de inicio (Formato EXACTO: yyyy/MM/dd/HH:mm:ss):");
                        String fechaInicio = scanner.nextLine();
                        
                        System.out.println("Ingrese la fecha final (Formato EXACTO: yyyy/MM/dd/HH:mm:ss):");
                        String fechaFin = scanner.nextLine();
                        
                        // 1. Usamos los métodos nuevos para obtener los datos
                        List<Booking> reservasFiltradas = bookingServices.obtenerReservasPorRango(fechaInicio, fechaFin);
                        
                        if (reservasFiltradas.isEmpty()) {
                            System.out.println("\n>> No se encontraron reservas en ese rango de fechas.");
                            break; // Salimos del case si no hay nada que exportar
                        }
                        
                        double totalIngresos = bookingServices.calcularTotalIngresos(reservasFiltradas);
                        
                        System.out.println("\n>> Se encontraron " + reservasFiltradas.size() + " reservas. Ingresos: $" + totalIngresos);
                        System.out.println("Ingrese el nombre para guardar el archivo (Ej: ReporteEnero):");
                        String nombreArchivo = scanner.nextLine();
                        
                        System.out.println("¿En que formato desea exportarlo? (1. JSON | 2. CSV):");
                        int opFormato = scanner.nextInt();
                        scanner.nextLine(); // Limpiar buffer
                        
                        // 2. Nos aseguramos de que el exportador esté inicializado
                        if (this.exportadorService == null) {
                            this.exportadorService = new ExportadorService();
                        }
                        
                        boolean exito = false;
                        
                        // 3. Llamamos a TU lógica de ExportadorService
                        if (opFormato == 1) {
                            exito = exportadorService.exportarReporteIngresosJson(fechaInicio, fechaFin, totalIngresos, reservasFiltradas, nombreArchivo);
                        } else if (opFormato == 2) {
                            exito = exportadorService.exportarReporteIngresosCSV(fechaInicio, fechaFin, totalIngresos, reservasFiltradas, nombreArchivo);
                        } else {
                            System.out.println("\n>> Opción no válida.");
                        }
                        
                        // 4. Mensaje final
                        if (exito) {
                            System.out.println("\n>> ¡Archivo '" + nombreArchivo + "' exportado exitosamente en la carpeta de tu proyecto!");
                        } else {
                            System.out.println("\n>> Error al intentar guardar el archivo.");
                        }
                        
                    } catch (java.time.format.DateTimeParseException e) {
                        System.out.println("\n>> Error: El formato de las fechas no es correcto. Recuerde usar yyyy/MM/dd/HH:mm:ss");
                    }
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
