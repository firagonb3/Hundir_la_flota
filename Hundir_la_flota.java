import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/** 
 * @author  Manel Botet Fernández de Sevilla
 * @author  Marc Lecha Blesa
*/

public class Hundir_la_flota {
    private static Scanner sc = new Scanner(System.in);

    

    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String BLUE = "\u001B[34m";
    public static final String RESET = "\u001B[0m";

    public static final int iBASE = 36;

    public static final String sDirectorioDefault = "Game_Files";
    public static final String sDirectorioTablero = "tableros";
    public static File fDIRECTORIO = new File(System.getenv("APPDATA"),sDirectorioDefault);

    //public static int iContadorDeTurnos = 0;
    public static int iTocadosContadorJugador1 = 0;
    public static int iTocadosContadorJugador2 = 0;

    // esto se pude camviar, el resto de variables no tocarlas por si acaso.

    public static final char cMAR = '*';
    public static final char cBARCO = 'B';
    public static final char cTOCADOS = 'T';
    public static final char cVACIO = 'A';
    public static final char cHUNDIDO = 'H';

    // Comprovar Sistema Operativo
    // -----------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------
    public static final ProcessBuilder CLS = fnComprovarOS();

    public static ProcessBuilder fnComprovarOS() {
        try{
            String OS = System.getProperty("os.name"); 
            
            if(OS.contains("Windows")){        
                return new ProcessBuilder("cmd", "/c", "cls");
            } else {
                return new ProcessBuilder("clear");
            } 
        }catch(Exception e){
        }
        return null;
    }
    // -----------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------

    // Limpiar pantalla
    // -----------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------
    public static void pdLimpiarPantalla() {
        try {
            Process start = CLS.inheritIO().start();
            start.waitFor();
         } catch (Exception e) {
         }
        
    }
    // -----------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------

    // detectar tiempo de juego.
    // -----------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------
    //public static long lInicio;
    public static long TiempoDeJuego() {
       return System.currentTimeMillis();
    }
    
    /** 
     * @return long pasar los valores de la primera ejecucion del System.currentTimeMillis().
     */
    public static String TiempoDeJuego(Long lInicio) {
        int iS = 0, iM = 0, iH = 0;
        long lFin = System.currentTimeMillis();
        long lTiempo = ((lFin - lInicio)/1000);

        // calcular timpo transcurido
        for (int iTiempo = 0; iTiempo < lTiempo; iTiempo++) {
            iS++;
            if (iM == 60) {
                iH++;
                iM = 0;
            }
            if (iS == 60) {
                iM++;
                iS = 0;
            }
        }
        return iH + ":" + iM + ":" + iS;
    }           
    // -----------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------

    // Mostrar menu y seleconar opciones.
    // -----------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------
    /** 
     * @return boolean devuelve un true o false en funcion de la opcion selecionada.
     */
    public static boolean fnMenu() {
        do {
            pdLimpiarPantalla();
            System.out.println("//////////////////////////////");
            System.out.println("//   1. Jugador vs Bot      //");
            System.out.println("//   2. Jugador vs jugador  //");
            System.out.println("//   3. Configuracion       //");
            System.out.println("//   4. Salir               //");
            System.out.println("//////////////////////////////");
    
            System.out.print("Elige una opcion: ");
            switch (sc.nextLine()) {
                case "1":
                    bJugador2 = false;
                    pdDetectarDirectorio();
                    return true;
                case "2":
                    bJugador2 = true;
                    pdDetectarDirectorio();
                    return true;
                case "3":
                    while (fnMenuConfig()){}
                    break;
                default:
                    return false;
            }
        } while (true);
    }


    // sub-menu config
    // -----------------------------------------------------------------------------------------------------
    public static boolean fnMenuConfig() {
        pdLimpiarPantalla();
        System.out.println("Ruta: " + fDIRECTORIO);
        System.out.println("////////////////////////////");
        System.out.println("/   1. Cambiar ruta        /");
        System.out.println("/   2. Listar ficheros     /");
        System.out.println("/   3. Leer ficheros       /");
        System.out.println("/   4. Eliminar fichero    /");
        System.out.println("/   q. Volver              /");
        System.out.println("////////////////////////////");
        System.out.print("Elije una opcion: ");
        
        switch (sc.nextLine()) {
            
            
            case "q":
                return false;
            case "1":
                pdSelecionarRuta();
                return true;
            case "2":
                pdListarDirectorio();
                return true;
            case "3":
                pdLeerFicheros();
                return true;
            case "4":
                pdEliminarFichero();
                return true;
            default:
                return true;
        }
    }

    //funcion para selecionar ruta
    // -----------------------------------------------------------------------------------------------------
    public static void pdSelecionarRuta() {
        
        System.out.print("Escribe la ruta: ");
        String sRuta = sc.nextLine();
        fDIRECTORIO = new File(sRuta, sDirectorioDefault);
        if (!fDIRECTORIO.exists()) {
            fDIRECTORIO.mkdirs();
        }

        File fDirectorioTablero = new File(fDIRECTORIO,sDirectorioTablero);
        if(!fDirectorioTablero.exists()){
            fDirectorioTablero.mkdirs();
        }
    }

    
    // Funcion para listar ficheros 
    // -----------------------------------------------------------------------------------------------------
    public static void pdListarDirectorio() {
        pdLimpiarPantalla();
        String[] afDirectorio = fDIRECTORIO.list();
        if (fDIRECTORIO.exists()) {
            for (int iPosicion = 0; iPosicion < afDirectorio.length; iPosicion++) {
                System.out.println(afDirectorio[iPosicion]);
            }
            System.out.println("!!! Presione cualquier tecla para volver !!!");
            sc.nextLine();
        } else {
            System.out.println("!!! El Directorio no existe !!!");
        }
    }

    // Funcion para Leer ficheros
    // -----------------------------------------------------------------------------------------------------
    public static void pdLeerFicheros() {
        pdLimpiarPantalla();
        try {
            System.out.print("Pon el nombre del  archivo: ");
            String sFichero = sc.nextLine();

            File fComprobar = new File(fDIRECTORIO,sFichero);
            FileReader fr = new FileReader(fComprobar);
            BufferedReader bf = new BufferedReader(fr);

            Scanner sLector = new Scanner(fComprobar);
            
            String sCadena;
            long lNumerodeLineas = 0;

            if (fComprobar.exists()) {
                while ((sCadena = bf.readLine()) != null) {
                    lNumerodeLineas++;
                }
                
                for (int i = 0; i < lNumerodeLineas; i++) {
                    System.out.println(sLector.nextLine());
                }
                System.out.println("!!! Presione cualquier tecla para volver !!!");
                sc.nextLine();
            }
        } catch (Exception e) {
        }
    }

    // Funcion para Eliminar fichero
    // -----------------------------------------------------------------------------------------------------
    public static void pdEliminarFichero() {
        System.out.print("Pon el nombre del  archivo: ");
        String sFichero = sc.nextLine();

        File fComprobar = new File(fDIRECTORIO,sFichero);

        if (fComprobar.exists()) {
            fComprobar.delete();
        }

    }

    // -----------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------

    
    // Genedador de tablero aleatorio
    // -----------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------
    public static final char cTableroVacio = cMAR;
    public static final int iTAMANOTABLERO = 10;

    public static final int iBarcosNivel4 = 1;
    public static final int iBarcosNivel3 = 2;
    public static final int iBarcosNivel2 = 3;
    public static final int iBarcosNivel1 = 4;

    public static final int iBarcosNivel4longitud = 4;
    public static final int iBarcosNivel3longitud = 3;
    public static final int iBarcosNivel2longitud = 2;
    public static final int iBarcosNivel1longitud = 1;

    public static final int iBarcosTocados = (iBarcosNivel4longitud*iBarcosNivel4) + (iBarcosNivel3longitud*iBarcosNivel3) + (iBarcosNivel2longitud*iBarcosNivel2) + (iBarcosNivel1longitud*iBarcosNivel1);
    
    // Crear Tablero
    // ----------------------------------------------------------------------------------------
    /** 
     * @return char[][] devuelve un array vidirecional genedado automaticamente.
     */
    public static char[][] fnGenedarTablero() {
        char[][] cTablero = new char[iTAMANOTABLERO][iTAMANOTABLERO];

        for (int iPosicionX = 0; iPosicionX < cTablero.length; iPosicionX++) {
            for (int iPosicionY = 0; iPosicionY < cTablero.length; iPosicionY++) {
                cTablero[iPosicionX][iPosicionY] = cTableroVacio;
            }
        }

        pdGenedarBarcos(cTablero, iBarcosNivel4longitud, iBarcosNivel4);
        pdGenedarBarcos(cTablero, iBarcosNivel3longitud, iBarcosNivel3);
        pdGenedarBarcos(cTablero, iBarcosNivel2longitud, iBarcosNivel2);
        pdGenedarBarcos(cTablero, iBarcosNivel1longitud, iBarcosNivel1);

        /*
        for (int iPosicionX = 0; iPosicionX < cTablero.length; iPosicionX++) {
            for (int iPosicionY = 0; iPosicionY < cTablero.length; iPosicionY++) {
                if (cTablero[iPosicionX][iPosicionY] != cBARCO) {
                    cTablero[iPosicionX][iPosicionY] = cMAR;  
                }
            }
        }
        */

        return cTablero;
    }
    
    // Genedadaor de barcosen posicion aleatoria
    // ----------------------------------------------------------------------------------------
    /** 
     * @param cTablero          almacenar la matriz viridecional.
     * @param iLongitudBarco    almacenar la longitd del Barco.
     * @param iCantodad         almacena la cantidad de barcos para genedar.
     */
    public static void pdGenedarBarcos(char[][] cTablero, int iLongitudBarco, int iCantodad) {
        int iPosicionX = 0, iPosicionY =0, iOrizontalVertucal = 0, contador =0;

        do {
            iPosicionX = (int)Math.floor(Math.random()*(iTAMANOTABLERO)); 
            iPosicionY = (int)Math.floor(Math.random()*(iTAMANOTABLERO));
            iOrizontalVertucal = (int)Math.floor(Math.random()*2);

            if (fnComprobarPosicionBarc(cTablero, iPosicionX, iPosicionY, iOrizontalVertucal, iLongitudBarco)) {
                pdCrearBarco(cTablero, iPosicionX, iPosicionY, iOrizontalVertucal, iLongitudBarco);
                contador++;
            }
            
        } while (contador < iCantodad);
        
    }
   
    // Crear Barco
    // ----------------------------------------------------------------------------------------
    /** 
     * @param cTablero              almacenar la matriz viridecional.
     * @param iPosicionX            almacenar la posicion Y de la matriz viridecional.
     * @param iPosicionY            almacenar la posicion X de la matriz viridecional.
     * @param iOrizontalVertucal    almacenar la posicion vertical o orizontal del barco.
     * @param iLongitudBarco        almacenar la longitd del Barco.
     */
    public static void pdCrearBarco(char[][] cTablero, int iPosicionX, int iPosicionY,int iOrizontalVertucal, int iLongitudBarco) {
        if (iOrizontalVertucal == 0) {
            for (int iPosicion = 0; iPosicion < iLongitudBarco; iPosicion++) {
                cTablero[iPosicionX][iPosicionY+iPosicion] = cBARCO;
                pdCordenadasBarco(iPosicionX, iPosicionY+iPosicion, false);
            } 
        } else {
            for (int iPosicion = 0; iPosicion < iLongitudBarco; iPosicion++) {
                cTablero[iPosicionX+iPosicion][iPosicionY] = cBARCO;
                pdCordenadasBarco(iPosicionX+iPosicion, iPosicionY, false);
            } 
        }
        pdCordenadasBarco(0, 0, true);
    }
    
    // Comprobar si se puede genedar el barco en la posicion actual
    // ----------------------------------------------------------------------------------------
    /** 
     * @param cTablero              almacenar la matriz viridecional.
     * @param iPosicionX            almacenar la posicion Y de la matriz viridecional.
     * @param iPosicionY            almacenar la posicion X de la matriz viridecional.
     * @param iOrizontalVertucal    almacenar la posicion vertical o orizontal del barco.
     * @param iLongitudBarco        almacenar la longitd del Barco.
     * @return boolean
     */
    public static boolean fnComprobarPosicionBarc(char[][] cTablero, int iPosicionX, int iPosicionY, int iOrizontalVertucal, int iLongitudBarco) {
        int iContador =0;
        try {
            if (iOrizontalVertucal == 0) {
                if (cTablero[iPosicionX][iPosicionY] == cTableroVacio & cTablero[iPosicionX][iPosicionY+(iLongitudBarco-1)] == cTableroVacio) {
                    return fnComrpobarLongitudBarco(cTablero, iPosicionX, iPosicionY, iContador,iOrizontalVertucal, iLongitudBarco);
                } else {
                    return false; 
                }  
            } else {
                if (cTablero[iPosicionX][iPosicionY] == cTableroVacio & cTablero[iPosicionX+(iLongitudBarco-1)][iPosicionY] == cTableroVacio) {
                    return fnComrpobarLongitudBarco(cTablero, iPosicionX, iPosicionY, iContador,iOrizontalVertucal, iLongitudBarco);
                } else {
                    return false; 
                } 
            }
                
        } catch (Exception e) {
            return false;
        }
        
    }
    
    // Comprobar si la longitd del barco es balida para introducir el barco.
    // ----------------------------------------------------------------------------------------
    /** 
     * @param cTablero              almacenar la matriz viridecional.
     * @param iPosicionX            almacenar la posicion Y de la matriz viridecional.
     * @param iPosicionY            almacenar la posicion X de la matriz viridecional.
     * @param iContador             almacenar almacena y incremanta el valor cadavez que se llama la funcion asi misma.
     * @param iOrizontalVertucal    almacenar la posicion vertical o orizontal del barco.
     * @param iLongitudBarco        almacenar la longitd del Barco.
     * @return boolean              devuelve un true si se pude gendar un barco y un false si no se puede genedar.
     */
    public static boolean fnComrpobarLongitudBarco(char[][] cTablero, int iPosicionX, int iPosicionY, int iContador, int iOrizontalVertucal, int iLongitudBarco) {
        if (iOrizontalVertucal == 0) {
            if (iContador <= iLongitudBarco) {
                if (fnComprobarCordenadasActuales(cTablero, iPosicionX, iPosicionY)) {
                    return fnComrpobarLongitudBarco(cTablero, iPosicionX, iPosicionY+1, iContador+1, iOrizontalVertucal, iLongitudBarco);
                } else {
                    return false;
                }
            } else {
                return true;
            } 
        } else {
            if (iContador <= iLongitudBarco) {
                if (fnComprobarCordenadasActuales(cTablero, iPosicionX, iPosicionY)) {
                    return fnComrpobarLongitudBarco(cTablero, iPosicionX+1, iPosicionY, iContador+1, iOrizontalVertucal, iLongitudBarco);
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
        
    }
    
    // comprobar si las cordenadas actuales son validas para la geredacion de lbarco.
    // ----------------------------------------------------------------------------------------
    /** 
     * @param cTablero      almacenar la matriz viridecional
     * @param iPosicionX    almacenar la posicion Y de la matriz viridecional
     * @param iPosicionY    almacenar la posicion X de la matriz viridecional
     * @return boolean      la funcion devolvera false si las comprovaciones son erronias y si son correctas seran true
     */
    public static boolean fnComprobarCordenadasActuales(char[][] cTablero, int iPosicionX, int iPosicionY) {
        boolean bGenedar = true;
        
            try {
                if (cTablero[iPosicionX-1][iPosicionY-1] != cTableroVacio) {
                    bGenedar = false;
                }
            } catch (Exception e) {}
            try {
                if (cTablero[iPosicionX-1][iPosicionY] != cTableroVacio) {
                    bGenedar = false;
                }
            } catch (Exception e) {}
            try {
                if (cTablero[iPosicionX-1][iPosicionY+1] != cTableroVacio) {
                    bGenedar = false;
                }
            } catch (Exception e) {}   

            try {
                if (cTablero[iPosicionX][iPosicionY-1] != cTableroVacio) {
                    bGenedar = false;
                }
            } catch (Exception e) {}   
            try {
                if (cTablero[iPosicionX][iPosicionY+1] != cTableroVacio) {
                    bGenedar = false;
                }
            } catch (Exception e) {}
            try {
                if (cTablero[iPosicionX+1][iPosicionY+1] != cTableroVacio) {
                    bGenedar = false;
                }
            } catch (Exception e) {}
            try {
                if (cTablero[iPosicionX+1][iPosicionY] != cTableroVacio) {
                    bGenedar = false;
                } 
            } catch (Exception e) {}
            try {
                if (cTablero[iPosicionX+1][iPosicionY-1] != cTableroVacio) {
                    bGenedar = false;
                }
            } catch (Exception e) {}
        return bGenedar;
    }
    // -----------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------

    // Mostrar tablero.
    // -----------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------
    /** 
     * @param cTablero          variable para almacenar array vididecional.
     * @param bTurnojugador     variable para mostrar los barcos en funcion de si juegas con el bot o no.
     * @param bMostarTocados    mostrada los barcos tocados en funcion de si es false o true.
     */
    public static void fnMostrarTablero(char[][] cTablero, boolean bTurnojugador, boolean bMostarTocados) {
        //pdLimpiarPantalla();
        System.out.println("     1  2  3  4  5  6  7  8  9  10");
        System.out.println("----------------------------------");
        for (int iPosicionX = 0; iPosicionX < cTablero.length; iPosicionX++) {
            System.out.print(" " + Character.forDigit(iPosicionX + 10, iBASE) + " |");
            for (int iPosicionY = 0; iPosicionY < cTablero.length; iPosicionY++) {
                if (cTablero[iPosicionX][iPosicionY] == cHUNDIDO) {
                    System.out.print( GREEN+ " " + cTablero[iPosicionX][iPosicionY] + " " + RESET);
                } else if (cTablero[iPosicionX][iPosicionY] == cTOCADOS) {
                    System.out.print( RED + " " + cTablero[iPosicionX][iPosicionY] + " " + RESET);
                } else if (cTablero[iPosicionX][iPosicionY] == cBARCO ) {
                    if (!bJugador2 && bTurnojugador) {
                        System.out.print( BLUE + " " + cMAR + " " + RESET);
                    } else {
                        System.out.print(" " + cMAR + " " + RESET);
                    }
                    //System.out.print( BLUE + " " + cMAR + " " + RESET);
                    // System.out.print(" " + cMAR + " " + RESET);
                } else {
                    System.out.print(" " + cTablero[iPosicionX][iPosicionY] + " ");
                }
            }
            System.out.println("");
        }
        System.out.println("----------------------------------");
        if (bMostarTocados) {
            if (!bTurnojugador) {
                System.out.println("tocados: " + iTocadosContadorJugador1 + " de " + iBarcosTocados);
            } else {
                System.out.println("tocados: " + iTocadosContadorJugador2 + " de " + iBarcosTocados);
            }    
        }
        
        //System.out.println("Turnos: " + (iContadorDeTurnos + 1) + " de " + (iTurnos + 1) + " : tocados: " + iTocadosContador + " de " + iBarcosTocados);
        //System.out.println("tocados: " + iTocadosContador + " de " + iBarcosTocados);
    }

    
    /** 
     * @param cTablero          variable para almacenar array vididecional.
     * @param cTablero2         variable para almacenar array vididecional 2.
     * @param bMostarTocados    mostrada los barcos tocados en funcion de si es false o true.
     */
    public static void fnMostrarTodosTableros(char[][] cTablero, char[][] cTablero2, boolean bMostarTocados) {
        pdLimpiarPantalla();
        System.out.println("jugador");
        fnMostrarTablero(cTablero, true, bMostarTocados);

        if (bJugador2) {
            System.out.println("jugador 2:");
        } else {
            System.out.println("Bot:");    
        }
        
        fnMostrarTablero(cTablero2, false, bMostarTocados);
    }
    // -----------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------

    
    // gestionar turnos
    // -----------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------

    public static boolean bTurno;
    public static boolean bInicio = true;
    public static boolean bJugador2;
    public static boolean bBot;

    
    /** 
     * @param cTablero      variable para almacenar array vididecional.
     * @param cTablero2     variable para almacenar array vididecional 2.
     * @return boolean      devolvera un true si el turno a finalizado y camviada de turno, y un false si el turno continua.
     */
    public static boolean fnControlTurnos(char[][] cTablero, char[][] cTablero2) {
        if (bInicio) {
            bBot = false;
            if (fnTurno(cTablero2, bBot)) {
                return true;
            }
            else {
                bInicio = false;
                return false;
            }
        } else {
            if (bJugador2) {
                bBot = false;
            } else {
                bBot = true;
            }
            if (fnTurno(cTablero,bBot)) {
                return true;
            } else {
                bInicio = true;
                return false;
            }
        }
    }
    
    // Turno del jugador
    // -----------------------------------------------------------------------------------------------------
    /** 
     * @param cTablero  variable para almacenar array vididecional.
     * @param bBot      variable para detectar si el bot esta activado o no.
     * @return boolean  devolvera un true si el turno a finalizado y un false si el turno continua.
     */
    public static boolean fnTurno(char[][] cTablero, boolean bBot) {

        System.out.print("Pon las cordenadas o Escribe Salir para cerrar: ");
        if (bBot) {
            while (!fnSelecionarCasilla(String.valueOf(Character.forDigit(((int)Math.floor(Math.random()*(iTAMANOTABLERO))) + 10,iBASE)) + String.valueOf((int)Math.floor(Math.random()*(iTAMANOTABLERO+1))), cTablero)) {
                //System.out.println("!!! Cordenadas Erronias !!!");
            }
        } else {
            while (!fnSelecionarCasilla(sc.nextLine(), cTablero)) {
                System.out.println("!!! Cordenadas Erronias !!!");
            }
        }
        
        if (bTurno) {
            if (iTocadosContadorJugador1 == iBarcosTocados | iTocadosContadorJugador2 == iBarcosTocados) {
                return false;
            }
            return true; 
        } else {
            //iContadorDeTurnos++;
            return false;
            
        }
        
    }
    // -----------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------

    // Selecionar casillas y comprobar que sean correctas.
    // -----------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------
     /** 
     * @param sCordenadas   variable para almacenar las cordenadas.
     * @param cTablero      variable para al macenar el array vidimensional.
     * @return boolean      la funcion devuelve true o false si se a selecionado la casilla correctamente.
     */
    public static boolean fnSelecionarCasilla(String sCordenadas, char[][] cTablero) {
        int  iPosicionX,  iPosicionY;

        if (sCordenadas.equalsIgnoreCase("salir")) {
            System.exit(0);
        }

        try {
            iPosicionX = Character.getNumericValue(sCordenadas.charAt(0)) - 10;
            iPosicionY = Integer.parseInt(sCordenadas.substring(1)) - 1;    
        } catch (Exception e) {
            return false;
        }

        if (iPosicionX >= 0 & iPosicionX < cTablero.length & iPosicionY >= 0 & iPosicionY < cTablero.length) {
            fnDesvelarCasilla(iPosicionX, iPosicionY, cTablero, sCordenadas);
            //pdComprovarBarcoHundido(cTablero, iPosicionX, iPosicionY);
            return true;
        } else {
            return false;
        }
    }
   
    // Desvelar casillas y marcar los barcos tocados
    // -----------------------------------------------------------------------------------------------------
     /** 
     * @param iPosicionX    comprovar posicion de la matriz vidideccional en Y.
     * @param iPosicionY    comprovar posicion de la matriz vidideccional en X.
     * @param cTablero      matriz vidideccional.
     */
    public static void fnDesvelarCasilla(int iPosicionX, int iPosicionY, char[][] cTablero, String sCordenadas) {
        if (cTablero[iPosicionX][iPosicionY] == cVACIO ) {
            bTurno = true;
        } else if (cTablero[iPosicionX][iPosicionY] == cBARCO ) {
            pdAlmacenaDatos(cTOCADOS, sCordenadas);
            cTablero[iPosicionX][iPosicionY] = cTOCADOS;
            System.out.println("ASDF");
            pdLeerHundido(cTablero);
            bTurno = true;
            if (bInicio) {
                iTocadosContadorJugador1++;
            } else {
                iTocadosContadorJugador2++;
            }
        } else if (cTablero[iPosicionX][iPosicionY] == cMAR) {
            pdAlmacenaDatos(cVACIO, sCordenadas);
            cTablero[iPosicionX][iPosicionY] = cVACIO ;
            bTurno = false;
        }
    }
    // -----------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------

    // Comprobar Ganador
    // -----------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------
    /** 
     * @param cTablero      Pasar primer tablero.
     * @param ctablero2     Pasar segundo tablero.
     * @return boolean      Es de tipo boolean y debuelve el valor en funcion de si se ha ganado o no.
     */
    public static boolean fnControlFinal(char[][] cTablero, char[][] ctablero2, long lInicio) {
        System.out.println("cosass2");
        if (iTocadosContadorJugador1 == iBarcosTocados) {
            fnMostrarTodosTableros(cTablero, ctablero2, false);
            System.out.println("!!! Ha ganado el jugador 1 !!!");
            System.out.println(TiempoDeJuego(lInicio));
            sc.nextLine();
            return true;
        } else if (iTocadosContadorJugador2 == iBarcosTocados) {
            fnMostrarTodosTableros(cTablero, ctablero2, false);
            System.out.println("!!! Ha ganado el jugador 2 !!!");
            System.out.println("Tiempo transcurido: " + TiempoDeJuego(lInicio));
            sc.nextLine();
            return true;
        } else {
            return false;
        }
    }
    // -----------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------
    
    // Registro de datos 
    // -----------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------
    public static File fArchivo;
    public static File fbarcos;
    public static File fbarcos2;
    public static int iTurnos = 0;

    // Almacenar cordenadas barcos
    // ----------------------------------------------------------------------------------------
    public static void pdCordenadasBarco(int iPosicionX, int iPosicionY, boolean bFinCordenadas) {
        BufferedWriter bw = null;
        FileWriter fw = null;
       
        try {
            if (Jugadores) {
                fw = new FileWriter(fbarcos.getAbsoluteFile(), true);
            } else {
                fw = new FileWriter(fbarcos2.getAbsoluteFile(), true);
            }
            
            bw = new BufferedWriter(fw);

            if (!bFinCordenadas) {
                bw.write( iPosicionX + "" + iPosicionY + " ");
            } else {
                bw.write("\n");
            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fw != null)
                    fw.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    // Detectar direcorio 
    // ----------------------------------------------------------------------------------------
    public static void pdDetectarDirectorio() {
        File fDirectorioTablero = new File(fDIRECTORIO,sDirectorioTablero);
        if (!fDIRECTORIO.exists()) {
            fDIRECTORIO.mkdirs();
            pdCrearFichero();
        } else {
            pdCrearFichero();
        }
        if (!fDirectorioTablero.exists()) {
            fDirectorioTablero.mkdirs();
        }
    }

    // Crear ficheros
    // ----------------------------------------------------------------------------------------
    public static void pdCrearFichero() {
        int iContador = 1;

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm");
        String sFecha = dtf.format(LocalDateTime.now());

        
        File[] fLista = fDIRECTORIO.listFiles();

        for (int i = 0; i < fLista.length; i++) {
            if (fLista[i].isFile()) {
                iContador++;
            }
        }

        fArchivo = new File(fDIRECTORIO, sFecha + "_" + iContador + ".txt");
        fbarcos = new File(fDIRECTORIO, sDirectorioTablero + "/Jugador1.txt");
        fbarcos2 = new File(fDIRECTORIO, sDirectorioTablero + "/Jugador2.txt");

        try {
            if (fbarcos.exists()) {
                fbarcos.delete();
            }
            if (fbarcos2.exists()) {
                fbarcos2.delete();
            }
            fbarcos.createNewFile();
            fbarcos2.createNewFile();
            fArchivo.createNewFile();
        } catch (Exception e) {
            System.out.println(e);
        }
        
    }

    // Almacenar informacion en ficheros
    // ----------------------------------------------------------------------------------------
    public static void pdAlmacenaDatos(char cStado, String sCordenadas) {
        String sMovimiento;

        BufferedWriter bw = null;
        FileWriter fw = null;
       
        try {
            fw = new FileWriter(fArchivo.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);

            iTurnos++;

            switch (cStado) {
                case cVACIO:
                    sMovimiento = "Agua";
                    break;
                case cTOCADOS:
                    sMovimiento = "Tocado";
                    break;
                case cHUNDIDO:
                    sMovimiento = "Hundido";
                    break;
                default:
                    sMovimiento = "Error";
                    break;
            }

            if (bInicio) {
                bw.write("[TURNO " + iTurnos + "] jugador 1: " + sCordenadas + " -> " + sMovimiento + "\n");
            } else {
                bw.write("[TURNO " + iTurnos + "] jugador 2: " + sCordenadas + " -> " + sMovimiento + "\n");
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fw != null)
                    fw.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    // -----------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------


    // Hundir barcos
    // -----------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------
    public static void pdLeerHundido(char[][] cTablero) {
        try {
            String sRuta;
            if (!bInicio) {
                sRuta = fDIRECTORIO + File.separator + sDirectorioTablero + "/Jugador2.txt";
            } else {
                sRuta = fDIRECTORIO + File.separator + sDirectorioTablero + "/Jugador1.txt";
            }
            //System.out.println(fDIRECTORIO);
            File f = new File(sRuta);
            Scanner lector = new Scanner(f);

            //System.out.println("sRuta" + sRuta);
            String[] aCordenadas;
            String sCordenadas = "";

            int iPosicionX , iPosicionY;

            int iContador = 0;

            while (lector.hasNext()){
                iContador = 0;
                //System.out.println("1" + sCordenadas);
                sCordenadas=lector.nextLine();
                //System.out.println("2" + sCordenadas);
                aCordenadas = sCordenadas.split(" ");
                //System.out.println("Cordenadas barco: " + sCordenadas);
                for (int iPosicion = 0; iPosicion < aCordenadas.length; iPosicion++) {

                    //System.out.println("CONVERSION " + aCordenadas[iPosicion]);

                    //System.out.println("for: " + iPosicion);
                    iPosicionX = Character.getNumericValue(aCordenadas[iPosicion].charAt(0));
                    iPosicionY = Integer.parseInt(aCordenadas[iPosicion].substring(1));

                    if (fnDetectarHundido(cTablero, iPosicionX, iPosicionY)) {
                        iContador++;
                    }

                    //System.out.println("contador: " + iContador + " : " + aCordenadas.length);
                    if (iContador == aCordenadas.length) {
                        pdHundirBarco(cTablero, iPosicionX, iPosicionY, iContador);
                    }                     
                }
            }

            lector.close();

        } catch (Exception e) {
            //TODO: handle exception
            System.out.println(e);
        }
    }

    // Detectar posiciones de tocados desvelados
    // ----------------------------------------------------------------------------------------
    public static boolean fnDetectarHundido(char[][] cTablero, int iPosicionX,int iPosicionY) {
        for (int i = 0; i < cTablero.length; i++) {
            for (int j = 0; j < cTablero.length; j++) {
                if (i == iPosicionX & j == iPosicionY) {
                    if (cTablero[i][j] == cTOCADOS) {
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
        return false;
    }

    // Marcar barcos como hundidos
    // ----------------------------------------------------------------------------------------
    public static void pdHundirBarco(char[][] cTablero, int iPosicionX, int iPosicionY, int iContador) {
        if (cTablero[iPosicionX][iPosicionY] == cTOCADOS) {
            cTablero[iPosicionX][iPosicionY] = cHUNDIDO;
        }

        try {
            if (cTablero[iPosicionX][iPosicionY+1] == cTOCADOS) {
                pdHundirBarco(cTablero, iPosicionX, iPosicionY+1, iContador);
            } 
        } catch (Exception e) {
        }
        
        try {
            if (cTablero[iPosicionX-1][iPosicionY] == cTOCADOS) {
                pdHundirBarco(cTablero, iPosicionX-1, iPosicionY, iContador);
            } 
        } catch (Exception e) {
        }
        
        try {
            if (cTablero[iPosicionX][iPosicionY-1] == cTOCADOS) {
                pdHundirBarco(cTablero, iPosicionX, iPosicionY-1, iContador);
            }
        } catch (Exception e) {
        }
        try {
            if (cTablero[iPosicionX+1][iPosicionY] == cTOCADOS) {
                pdHundirBarco(cTablero, iPosicionX+1, iPosicionY, iContador);
            }  
        } catch (Exception e) {
        }
    }
    // -----------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------

    // Metodo Main
    // -----------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------

    public static boolean Jugadores = false;

    /** 
     * @param args Metodo main
     */
    public static void main(String[] args) {
        char[][] cTableroJugador;
        char[][] cTableroJugador2;
        long lInicio;

        while (fnMenu()) {
            iTocadosContadorJugador1 = 0;
            iTocadosContadorJugador2 = 0;
            cTableroJugador = fnGenedarTablero();
            Jugadores=true;
            cTableroJugador2 = fnGenedarTablero();
            lInicio = TiempoDeJuego();
            do {
                do {
                    fnMostrarTodosTableros(cTableroJugador, cTableroJugador2, true);
                } while (fnControlTurnos(cTableroJugador, cTableroJugador2));
            } while (!fnControlFinal(cTableroJugador, cTableroJugador2, lInicio));
        }
    }
    // -----------------------------------------------------------------------------------------------------
    // -----------------------------------------------------------------------------------------------------
}