package co.edu.uptc.View;

import java.time.LocalDateTime;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

import co.edu.uptc.Model.Admin;
import co.edu.uptc.Model.Booking;
import co.edu.uptc.Model.Client;
import co.edu.uptc.Model.Salon;
import co.edu.uptc.Persistence.AdminJsonRepository;
import co.edu.uptc.Persistence.BookingJsonRepository;
import co.edu.uptc.Persistence.ClientJsonRepository;
import co.edu.uptc.Persistence.SalonJsonRepository;
import co.edu.uptc.Services.AdminServices;
import co.edu.uptc.Services.BookingServices;
import co.edu.uptc.Services.ClientService;
import co.edu.uptc.Services.SalonServices;
import co.edu.uptc.Util.DateConvertor;
import co.edu.uptc.Util.ExportadorService;
import co.edu.uptc.Util.FiltrerService;
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
    private DateConvertor dateConvertor= new DateConvertor();
    private RankedSalon rankedSalon;
    private AdminJsonRepository adminJsonRepository= new AdminJsonRepository("Admins.json");
    private SalonJsonRepository salonJsonRepository= new SalonJsonRepository("Salon.json");
    private BookingJsonRepository bookingJsonRepository= new BookingJsonRepository("Booking.json");
    private ClientJsonRepository clientJsonRepository= new ClientJsonRepository("Cliente.json");
    //Aquí vamos a indicarle de donde sale la lista:
    private List<Admin> adminList = adminJsonRepository.sendJsonAdminList();
    private List<Salon> salonList = salonJsonRepository.sendSalonList();
    private List<Booking> bookingList= bookingJsonRepository.sendJsonBookingList();
    private List<Client> listClient= clientJsonRepository.sendJsonClientList();

    public VistaConsola() {
        this.scanner = new Scanner(System.in);
        this.clienteLogeado=null;
        this.adminLogeado=null;
        this.adminServices= new AdminServices();
        this.clientService= new ClientService();
        this.bookingServices= new BookingServices();
        this.salonServices= new SalonServices();
        this.dateConvertor= new DateConvertor();
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
            System.out.println(mensajes.getString("menu.principal.op1"));//Portal de clientes
            System.out.println(mensajes.getString("menu.principal.op2"));//Portal de administrativos
            System.out.println(mensajes.getString("menu.principal.op3"));//Salir
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
            System.out.println(mensajes.getString("menu.cliente.op1"));//INICIAR SESIÓN
            System.out.println(mensajes.getString("menu.cliente.op2"));//CREAR CUENTA
            System.out.println(mensajes.getString("menu.cliente.op3"));//VOLVER AL MENÚ
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
            boolean exito= clientService.registrarCliente(cliente,listClient);
            if (exito) {
                System.out.println(mensajes.getString("registro.exito"));
                clientJsonRepository.saveClientList(listClient);
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
            if (clientService.validateAccess(id, constrasena,listClient)) {
                System.out.println(mensajes.getString("login.exito"));
                this.clienteLogeado=clientService.buscarClientPorId(id,listClient);
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
            System.out.println(mensajes.getString("panel.cliente.op1"));//VER MIS RESERVAS
            System.out.println(mensajes.getString("panel.cliente.op2"));//SOLICITAR NUEVA RESERVA
            System.out.println(mensajes.getString("panel.cliente.op3"));//ELIMINAR UNA RESERVA
            System.out.println(mensajes.getString("panel.cliente.op4")); //CERRAR SESIÓN
            System.out.print(mensajes.getString("general.ingreso.opcion"));
            try {
                int opcion=scanner.nextInt();
                scanner.nextLine();
                switch (opcion) {
                    case 1:
                        List<Booking> listReservas =bookingServices.sendBookingListByClient(this.clienteLogeado.getId(),bookingList);
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

                    case 2: //HACER UNA RESERVA
                        int id = 0;
                        Salon salonElegido=null;
                        boolean fechaValida=false;
                        LocalDateTime startDateBooking = null;
                        int capacity=0;
                        double budget=0;
                        FiltrerService filtro=new FiltrerService();
                        //Pidiendo cuantas horas durará la reserva
                        System.out.println("\n--- " + mensajes.getString("panel.cliente.op2") + " ---");
                        int horas = 0;
                        boolean horasValidas = false;
                        while (!horasValidas) {
                            try {
                                System.out.println(mensajes.getString("cliente.reserva.pedir.horas"));
                                horas = scanner.nextInt();
                                scanner.nextLine();
                                if (horas<=0) {
                                    horasValidas=false;
                                    System.out.println(mensajes.getString("cliente.reserva.pedir.horas.error"));
                                }else{
                                    horasValidas = true;
                                }
                            } catch (InputMismatchException e) {
                                System.out.println("\n" + mensajes.getString("error.letras"));
                                scanner.nextLine();
                                
                            }
                        }
                        //Pidiendo la fecha de la reserva
                        while (!fechaValida) {
                            try{ 
                                System.out.println(mensajes.getString("cliente.reserva.pedir.año"));
                                int year = scanner.nextInt();
                                System.out.println(mensajes.getString("cliente.reserva.pedir.mes"));
                                int mounth = scanner.nextInt();
                                System.out.println(mensajes.getString("cliente.reserva.pedir.dia"));
                                int day = scanner.nextInt();
                                System.out.println(mensajes.getString("cliente.reserva.pedir.hora"));
                                int hour = scanner.nextInt();
                                System.out.println(mensajes.getString("cliente.reserva.pedir.minuto"));
                                int minute= scanner.nextInt();
                                startDateBooking=LocalDateTime.of(year, mounth, day, hour, minute);

                                filtro= new FiltrerService(startDateBooking , horas);
                                try {
                                    if (startDateBooking.isBefore(LocalDateTime.now())) {
                                        System.out.println(mensajes.getString("cliente.reserva.pedir.fecha.pasado"));
                                    }else{
                                        fechaValida=true;
                                    }
                                } catch (java.time.format.DateTimeParseException e) {
                                    System.out.println(mensajes.getString("general.error.fecha"));
                                }
                            }catch (InputMismatchException e) {
                                System.out.println(mensajes.getString("general.error.formato"));
                                scanner.nextLine();
                            }   
                        } 

                        //Pidiendo filtro por capacidad
                        boolean tipoValido = false;
                        while (!tipoValido) {
                            System.out.println(mensajes.getString("cliente.reserva.pedir.filtro.capacidad"));
                            try {
                                int tipo = scanner.nextInt();
                                scanner.nextLine();
                                switch (tipo) {
                                    case 1:
                                        filtro.setFiltrerByCapacity(true);
                                        tipoValido = true;
                                        System.out.println(mensajes.getString("cliente.reserva.pedir.capacidad"));
                                        capacity=scanner.nextInt();
                                        break;
                                    case 2:
                                        filtro.setFiltrerByCapacity(false);
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

                        //Pidiendo filtro por cantidad
                        tipoValido = false;
                        while (!tipoValido) {
                            System.out.println(mensajes.getString("cliente.reserva.pedir.filtro.presupuesto"));
                            try {
                                int tipo = scanner.nextInt();
                                scanner.nextLine();
                                switch (tipo) {
                                    case 1:
                                        filtro.setFiltrerByPrice(true);
                                        tipoValido = true;
                                        System.out.println(mensajes.getString("cliente.reserva.pedir.presupuesto"));
                                        budget=scanner.nextDouble();
                                        break;
                                    case 2:
                                        filtro.setFiltrerByPrice(false);
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
            //APLICANDO FILTROS Y SELECCIONANDO EL SALÓN
                        rankedSalon.setNumberOfReservations(salonList,bookingList);
                        List<Salon> salonesDisponibles= filtro.sendFiltrerSalonList(budget, capacity, salonList,bookingList);
                        if (salonesDisponibles.isEmpty()) { //No hay salones que cumplan con los requerimientos
                            System.out.println("No hay salones disponibles para esa fecha");
                        }else{ //Si hay salones disponibles
                            for (Salon salon : salonesDisponibles) {
                                System.out.println("-------------------------------------------------\n");
                                System.out.println(mensajes.getString("salon.services.mostrar.clase"));
                                System.out.println("-------------------------------------------------");
                                System.out.println(mensajes.getString("salon.services.mostrar.id") + " " + salon.getId());
                                System.out.println(mensajes.getString("salon.services.mostrar.nombre") + " " + salon.getSalonName());
                                System.out.println(mensajes.getString("salon.services.mostrar.capacidad") + " " + salon.getCapacity());
                                System.out.println(mensajes.getString("salon.services.mostrar.precio.por.hora") + salon.getPriceByHour());
                                System.out.println(mensajes.getString("salon.services.mostrar.numero.reservaciones") + " " + salon.getNumberOfReservations());
                                System.out.println("-------------------------------------------------");
                            }
                                boolean idvalido=false;
                                while (!idvalido) {
                                    try {
                                        System.out.println(mensajes.getString("cliente.reserva.pedir.salon"));
                                        id= scanner.nextInt();
                                        scanner.nextLine();
                                        for (Salon salon : salonesDisponibles) {
                                            if (salon.getId()==id) {
                                                salonElegido=salon;
                                                idvalido=true;
                                                break;
                                            }
                                        }
                                        if (!idvalido) {
                                            System.out.println(mensajes.getString("cliente.reservas.pedir.id.error"));
                                        }
                                    } catch (InputMismatchException e) {
                                        System.out.println("\n" + mensajes.getString("error.letras"));
                                        scanner.nextLine();
                                    }
                                }
                                try {
                                     //Calculamos la fecha de finalización de la reserva 
                                    LocalDateTime endDateBooking = startDateBooking.plusHours(horas);
                                    //Pasamos a formato String para poder importarlas
                                    String fechaInicioStr=dateConvertor.localDateTimeToString(startDateBooking);
                                    String fechaFinStr = dateConvertor.localDateTimeToString(endDateBooking);
                                    int nuevoId = bookingServices.sendNewId(bookingList);
                                    Booking nuevaReserva = new Booking(nuevoId, this.clienteLogeado, salonElegido, fechaInicioStr, horas, fechaFinStr);
                                    nuevaReserva.setPrice(bookingServices.calculatePriceBooking(nuevaReserva,bookingList));
                                    bookingServices.saveNewBooking(nuevaReserva,bookingList);
                                    bookingJsonRepository.saveBookingList(bookingList);
                                    System.out.println("\n>> " + mensajes.getString("cliente.reserva.exito") + " " + fechaFinStr);
                                    System.out.println(mensajes.getString("booking.services.mostrar.precio")+" $"+nuevaReserva.getPrice());
                                } catch (java.time.format.DateTimeParseException e) {
                                    System.out.println("\n>> " + mensajes.getString("cliente.reserva.error.fecha"));
                                }
                        }
                        break;
                    case 3:
                        listReservas =bookingServices.sendBookingListByClient(this.clienteLogeado.getId(),bookingList);
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
                            boolean idvalido=false;
                            while (!idvalido) {
                                try {
                                    System.out.println(mensajes.getString("cliente.eliminar.reserva"));
                                    id=scanner.nextInt();
                                    scanner.nextLine();
                                    for (Booking booking : listReservas) {
                                        if (booking.getId()==id) {
                                            bookingServices.cancelBooking(id, bookingList);
                                            bookingJsonRepository.saveBookingList(bookingList);
                                            idvalido=true;
                                            break;
                                        }
                                    }
                                    if (!idvalido) {
                                        System.out.println(mensajes.getString("cliente.reservas.pedir.id.error"));
                                    }
                                } catch (InputMismatchException e) {
                                    System.out.println("\n" + mensajes.getString("error.letras"));
                                    scanner.nextLine();
                                }
                            }
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
        try{ 
            System.out.println(mensajes.getString("login.admin.titulo"));
            System.out.println(mensajes.getString("login.admin.cedula"));
            int cedula= scanner.nextInt();
            scanner.nextLine();
            System.out.println(mensajes.getString("login.admin.contrasena"));
            String contrasena= scanner.nextLine();
            if (adminServices.validateAccess(cedula, contrasena,adminList)) {
                this.adminLogeado=adminServices.sendAdminById(cedula,adminList);
                System.out.println(mensajes.getString("login.exito"));
                menuInternoAdmin();
            }else{
                System.out.println(mensajes.getString("general.error.sesion"));
            }
        }catch (InputMismatchException e) {
            System.out.println(mensajes.getString("general.error.formato"));
        }
    }

    private void menuInternoAdmin(){
        boolean cerrarSesion=false;
        while (!cerrarSesion) {
            System.out.println(mensajes.getString("panel.admin.titulo"));
            System.out.println(mensajes.getString("panel.admin.op1"));//GESTIÓN DE CLIENTES
            System.out.println(mensajes.getString("panel.admin.op2"));//GESTIÓN DE SALONES
            System.out.println(mensajes.getString("panel.admin.op3"));//GESTIÓN DE ADMINISTRADORES
            System.out.println(mensajes.getString("panel.admin.op4"));//CERRAR SESIÓN
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
                    case 4:
                        menuGestionAdministradores();
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
            System.out.println(mensajes.getString("admin.admins.titulo"));
            System.out.println(mensajes.getString("admin.admins.op1"));//VER LISTA COMPLETA DE ADMINISTRADORES
            System.out.println(mensajes.getString("admin.admins.op2"));//MODIFICAR UN ADMINISTRADOR
            System.out.println(mensajes.getString("admin.admins.op3"));//ELIMINAR UN ADMINISTRADOR
            System.out.println(mensajes.getString("admin.admins.op4"));//CREAR UN ADMINISTRADOR
            System.out.println(mensajes.getString("admin.admins.op5"));//VOLVER AL MENÚ PRINCIPAL
            System.out.println(mensajes.getString("general.ingreso.opcion"));
            try{
                int op = scanner.nextInt();
                scanner.nextLine();
                switch (op) {
                    case 1:
                        for (Admin admin : adminList) {
                            System.out.println(mensajes.getString("admin.services.mostrar.nombre")+admin.getUserName());
                            System.out.println(mensajes.getString("admin.services.mostrar.id")+admin.getId());
                            System.out.println(mensajes.getString("admin.services.mostrar.numero.telefonico")+admin.getPhoneNumber());
                            System.out.println(mensajes.getString("admin.services.mostrar.correo")+admin.getEmail());
                        }
                        break;
                    case 2:
                            System.out.println(mensajes.getString("admin.admins.op2.pedirID"));
                            int idModificar=scanner.nextInt();
                            scanner.nextLine();
                            boolean datosValidos=false;
                            
                            if(adminServices.sendAdminById(idModificar, adminList)!=null){
                                String name="";
                                String correo="";
                                String contraseña="";
                                String numeroTelefonico="";
                                while (!datosValidos) {
                                try {
                                System.out.println(mensajes.getString("admin.admins.op2.aviso"));
                                System.out.println(mensajes.getString("registro.pedir.nombre"));
                                name= scanner.nextLine();
                                System.out.println(mensajes.getString("registro.pedir.correo"));
                                correo = scanner.nextLine();
                                System.out.println(mensajes.getString("registro.pedir.contrasena"));
                                contraseña= scanner.nextLine();
                                System.out.println(mensajes.getString("registro.pedir.telefono"));
                                numeroTelefonico = scanner.nextLine();
                                datosValidos=true;
                                } catch (InputMismatchException e) {
                                    System.out.println("\n" + mensajes.getString("error.letras"));
                                    scanner.nextLine();
                                }
                                }
                                if(adminServices.updateAdmin(idModificar, new Admin(name, idModificar, correo, contraseña, numeroTelefonico), adminList)){
                                    clientJsonRepository.saveClientList(listClient);
                                    System.out.println(mensajes.getString("modificar.admin.exito"));
                                }else{
                                    System.out.println(mensajes.getString("modificar.admin.error"));
                                }
                                
                            }else{
                                    System.out.println(mensajes.getString("modificar.admin.error"));
                            }

                        break;
                    
                    case 3:
                            System.out.println(mensajes.getString("admin.admins.op3.pedirID"));
                            int idFired=scanner.nextInt();
                            scanner.nextLine();
                            adminServices.fireAdmin(idFired, adminList);
                    break;
                    case 4:
                            System.out.println(mensajes.getString("admin.admins.op4.pedirID"));
                            int idNuevoAdmin=scanner.nextInt();
                            scanner.nextLine();
                            datosValidos=false;
                            
                            if(adminServices.sendAdminById(idNuevoAdmin, adminList)==null){
                                String name="";
                                String correo="";
                                String contraseña="";
                                String numeroTelefonico="";
                                while (!datosValidos) {
                                try {
                                System.out.println(mensajes.getString("admin.admins.op2.aviso"));
                                System.out.println(mensajes.getString("registro.pedir.nombre"));
                                name= scanner.nextLine();
                                System.out.println(mensajes.getString("registro.pedir.correo"));
                                correo = scanner.nextLine();
                                System.out.println(mensajes.getString("registro.pedir.contrasena"));
                                contraseña= scanner.nextLine();
                                System.out.println(mensajes.getString("registro.pedir.telefono"));
                                numeroTelefonico = scanner.nextLine();
                                datosValidos=true;
                                } catch (InputMismatchException e) {
                                    System.out.println("\n" + mensajes.getString("error.letras"));
                                    scanner.nextLine();
                                }
                                }
                                adminServices.saveNewAdmin(new Admin(name, idNuevoAdmin, correo, contraseña, numeroTelefonico), adminList);
                                adminJsonRepository.saveJsonAdminList(adminList);
                            }else{
                                    System.out.println(mensajes.getString("modificar.admin.error"));
                            }
                        break;
                    case 5:
                        volver=true;
                        break;
                    default:
                        break;
                }
            }catch (InputMismatchException e) {
                            System.out.println(mensajes.getString("general.error.formato"));
            }
        }
    }

    private void menuGestionClientesAdmin(){
        boolean volver= false;
        while (!volver) {
            System.out.println(mensajes.getString("admin.clientes.titulo")); 
            System.out.println(mensajes.getString("admin.clientes.op1"));//MOSTRAR LA LISTA DE CLIENTES
            System.out.println(mensajes.getString("admin.clientes.op2"));//MODIFICAR CLIENTE
            System.out.println(mensajes.getString("admin.clientes.op3"));//ElIMINAR UN CLIENTE
            System.out.println(mensajes.getString("admin.sub.volver")); //VOLVER AL MENÚ ANTERIOR
            System.out.println(mensajes.getString("general.ingreso.opcion"));//INGRESE UNA OPCIÓN
            try{
                int op= scanner.nextInt();
                switch (op) {
                    case 1: 
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
                        String nombre =null;
                        String contrasenaNueva =null;
                        String correo =null;
                        String telefono =null;
                        boolean esEmpresarial=false;
                        System.out.println(mensajes.getString("admin.clientes.op2.id"));
                        int idcliente= scanner.nextInt();
                        scanner.nextLine();
                        if (clientService.buscarClientPorId(idcliente, listClient)!=null) {
                            System.out.println(mensajes.getString("admin.clientes.op2.aviso"));
                            scanner.nextLine();
                            System.out.println(mensajes.getString("registro.pedir.nombre"));
                            nombre= scanner.nextLine();
                            System.out.println(mensajes.getString("registro.pedir.contrasena"));
                            contrasenaNueva= scanner.nextLine();
                            System.out.println(mensajes.getString("registro.pedir.correo"));
                            correo= scanner.nextLine();
                            System.out.println(mensajes.getString("registro.pedir.telefono"));
                            telefono= scanner.nextLine();
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
                            if (clientService.modificarCliente(idcliente, new Client(nombre, idcliente, contrasenaNueva, correo, telefono, esEmpresarial),listClient)) {
                                clientJsonRepository.saveClientList(listClient);
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
                        if (clientService.eliminarCliente(id,listClient)) {
                            System.out.println(mensajes.getString("general.delete.exito"));
                            clientJsonRepository.saveClientList(listClient);
                        }else{
                            System.out.println(mensajes.getString("general.delete.error"));
                        }
                        break;
                    case 4: 
                        volver = true; break;
                    default: 
                        System.out.println(mensajes.getString("admin.sub.error")); break;
                }
            }catch (InputMismatchException e) {
                            System.out.println(mensajes.getString("general.error.formato"));
            }
        }
    }
    
    private void menuGestionSalonesAdmin() {
        boolean volver = false;
        while(!volver){
            System.out.println("\n" + mensajes.getString("admin.salones.titulo"));
            System.out.println(mensajes.getString("admin.salones.op1"));//REGISTRAR NUEVO SALÓN
            System.out.println(mensajes.getString("admin.salones.op2"));//VER TODOS LOS SALONES
            System.out.println(mensajes.getString("admin.salones.op3"));//MODIFICAR UN SALÓN
            System.out.println(mensajes.getString("admin.sub.volver"));//VOLVER AL MENÚ ANTERIOR
            System.out.print(mensajes.getString("general.ingreso.opcion"));
            try {
                int op = scanner.nextInt();
                scanner.nextLine();
                switch (op) {
                    case 1: 
                        int id= salonServices.generateNewId(salonList);
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
                        boolean guardadoExitoso = salonServices.addNewSalon(nuevoSalon,salonList);
                        
                        if (guardadoExitoso) {
                            salonJsonRepository.saveSalonList(salonList);
                            System.out.println("\n>> " + mensajes.getString("registro.salon.exito"));
                        } else {
                            System.out.println("\n>> " + mensajes.getString("registro.salon.error"));
                        }
                        break;
                    case 2:
                        rankedSalon.setNumberOfReservations(salonList,bookingList);
                        if (salonList.isEmpty()) {
                            System.out.println("\n>> No hay salones registrados actualmente en el sistema.\n");
                        } else {
                            for (Salon salon : salonList) {
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
                        if (salonServices.searchSalonById(idModificar,salonList)) {
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
                            if (salonServices.updateSalon(idModificar, new Salon(idModificar, nuevoNombre, nuevaCapacidad, nuevoPrecio),salonList)) {
                                salonJsonRepository.saveSalonList(salonList);
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
            System.out.print(mensajes.getString("general.ingreso.opcion"));
            
            int op = scanner.nextInt();
            scanner.nextLine();
            switch (op) {
                case 1: 
                    List<Salon> listSalones = rankedSalon.sendTop5BestSalons(salonList);
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
                case 2:
                    LocalDateTime fechaInicio=null;
                    LocalDateTime fechaFin= null;
                    System.out.println(mensajes.getString("admin.export.title"));
                    try {
                        System.out.println(mensajes.getString("admin.pedir.fecha.inicio"));
                        System.out.println(mensajes.getString("admin.pedir.año"));
                        int year = scanner.nextInt();
                        System.out.println(mensajes.getString("admin.pedir.mes"));
                        int mounth = scanner.nextInt();
                        System.out.println(mensajes.getString("admin.pedir.dia"));
                        int day = scanner.nextInt();
                        System.out.println(mensajes.getString("admin.pedir.hora"));
                        int hour = scanner.nextInt();
                        System.out.println(mensajes.getString("admin.pedir.minuto"));
                        int minute= scanner.nextInt();
                        fechaInicio=LocalDateTime.of(year, mounth, day, hour, minute, 0);
                        boolean fechaValida= false;
                        while (!fechaValida) {
                            System.out.println(mensajes.getString("admin.pedir.fecha.final"));
                            System.out.println(mensajes.getString("admin.pedir.año"));
                            int year2 = scanner.nextInt();
                            System.out.println(mensajes.getString("admin.pedir.mes"));
                            int mounth2 = scanner.nextInt();
                            System.out.println(mensajes.getString("admin.pedir.dia"));
                            int day2 = scanner.nextInt();
                            System.out.println(mensajes.getString("admin.pedir.hora"));
                            int hour2 = scanner.nextInt();
                            System.out.println(mensajes.getString("admin.pedir.minuto"));
                            int minute2= scanner.nextInt();
                            scanner.nextLine();
                            fechaFin= LocalDateTime.of(year2, mounth2, day2, hour2, minute2, 0);
                            if (fechaFin.isBefore(fechaInicio)||fechaFin.isEqual(fechaInicio)) {
                                System.out.println(mensajes.getString("admin.export.date.error.fin"));
                            }else{
                                fechaValida=true;
                            }
                        }
                        String fechaInicioStr=dateConvertor.localDateTimeToString(fechaInicio);
                        String fechaFinStr= dateConvertor.localDateTimeToString(fechaFin);
                        List<Booking> reservasFiltradas = bookingServices.obtenerReservasPorRango(fechaInicioStr, fechaFinStr,bookingList);
                        
                        if (reservasFiltradas.isEmpty()) {
                            System.out.println(mensajes.getString("admin.export.pedir.fecha.error"));
                            break;
                        }
                        
                        double totalIngresos = bookingServices.calcularTotalIngresos(reservasFiltradas);
                        
                        System.out.println(mensajes.getString("admin.export.ingresos")+"$"+totalIngresos);
                        System.out.println(mensajes.getString("admin.export.pedir.nombre.archivo"));
                        String nombreArchivo = scanner.nextLine();
                        
                        System.out.println(mensajes.getString("admin.export.pedir.formato"));
                        int opFormato = scanner.nextInt();
                        if (this.exportadorService == null) {
                            this.exportadorService = new ExportadorService();
                        }
                        
                        boolean exito = false;
                        if (opFormato == 1) {
                            exito = exportadorService.exportarReporteIngresosJson(fechaInicioStr, fechaFinStr, totalIngresos, reservasFiltradas, nombreArchivo);
                        } else if (opFormato == 2) {
                            exito = exportadorService.exportarReporteIngresosCSV(fechaInicioStr, fechaFinStr, totalIngresos, reservasFiltradas, nombreArchivo);
                        } else {
                            System.out.println(mensajes.getString("admin.export.invalid"));
                        }
                        if (exito) {
                            System.out.println(mensajes.getString("admin.export.success"));
                        } else {
                            System.out.println(mensajes.getString("admin.export.error"));
                        }
                        
                    } catch(InputMismatchException e){
                        System.out.println("general.error.formato");
                    }
                    catch (java.time.DateTimeException e) {
                        System.out.println(mensajes.getString("admin.export.date_error"));
                    } catch (Exception e) {
                        System.out.println("general.error");
                    }
                    break;
                case 3: 
                    volver = true; 
                    break;
                default:
                    System.out.println(mensajes.getString("admin.sub.error")); 
                    break;
            }
        }
    }
}
