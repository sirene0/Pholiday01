package Pholiday01;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Pholiday01.enums.RequestType;
import Pholiday01.enums.ResponseType;

public class Server {
    int port =8081;
    private static final int max_connections = 50;
    
    private Map<Long, Spectacle> spectacles; 
    private ExecutorService threadpool;
    private ServerSocket serverSocket;

    public Server() {
        spectacles = new ConcurrentHashMap<>();
        initializeSpectacles();
        threadpool = Executors.newFixedThreadPool(max_connections);
    }
    
    public void initializeSpectacles() {

        Spectacle s1 = new Spectacle("Concert A", 100,LocalDateTime.of(2025, 9, 15, 20, 0));
        Spectacle s2 = new Spectacle("Play B", 50,LocalDateTime.of(2025, 9, 16, 18, 0));
        Spectacle s3 = new Spectacle("Wednesday", 175,LocalDateTime.of(2025, 9, 17, 19, 0));
        Spectacle s4 = new Spectacle("One Piece", 200,LocalDateTime.of(2025, 9, 06, 21, 0));
        spectacles.put(s1.getId(), s1);
        spectacles.put(s2.getId(), s2);
        spectacles.put(s3.getId(), s3);
        spectacles.put(s4.getId(), s4);

        for( Spectacle s :spectacles.values()){
            System.out.println("Initialized spectacle : "+s.toString());
        }
        System.out.println("Total spectacles initialized : "+spectacles.size());
    }

    public void start(){
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
            System.out.println("Waiting for client connections...");

            while(!serverSocket.isClosed())
            {
                try{
                    Socket clientsocket = serverSocket.accept();
                    System.out.println("New client connected : "+clientsocket);
                    //create a new thread to handle the client
                    threadpool.submit( new ClientHandler(clientsocket, spectacles) );
                }catch(Exception e){
                    if(serverSocket.isClosed()){
                        System.out.println("Server socket closed, stopping server...");
                    } else {
                        e.printStackTrace();
                    }
                }
            }
        } catch (BindException e) {
            System.out.println("Error : the port "+port+" is already in use.");
            System.out.println("stopping server...");
        } catch (Exception e) {
            System.out.println("Error : an unexpected error occurred.");
            e.printStackTrace();
        }
    }

    public void stop(){
        try {
            System.out.println("Stopping server...");
            if(serverSocket !=null && !serverSocket.isClosed()){
                serverSocket.close();
            }
            threadpool.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        System.out.println("Server is running...");
        // Server logic would go her
        Server server =new Server();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down server...");
            server.stop();
        }));
        server.start();
    }

    public class ClientHandler implements Runnable {
        private Socket clientSocket;
        private Map<Long, Spectacle> spectacles;

        public ClientHandler(Socket clientSocket, Map<Long, Spectacle> spectacles) {
            this.clientSocket = clientSocket;
            this.spectacles = spectacles;
        }

        @Override
        public void run() {
            try (ObjectInputStream in = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream out = new ObjectOutputStream(clientSocket.getOutputStream());) 
                {
                    Request request = (Request) in.readObject();
                    System.out.println("Received request :"+request.getType()+" , from client :"+clientSocket+" , for spectacle title :"+request.getNameSpectacle());
                    Reponse reponse ;

                    switch (request.getType()) {
                        case CONSULTATION:
                            reponse = handleConsultation(request);  
                            break;
                        case RESERVATION:
                            reponse = handleReservation(request);
                            break;
                        case LIST_SPECTACLES:
                            reponse = handleListSpectacles(request);
                            break;
                        default:
                            reponse = new Reponse(ResponseType.ERROR, "Unknown request type: "+request.getType());
                            break;
                    }

                    out.writeObject(reponse);
                    out.flush();

                    System.out.println("Response sent to client :"+clientSocket+" , response type :"+reponse.getReponseType()+" , message :"+reponse.getMessage());

            } catch (Exception e) {
                System.out.println("Error handling client :"+clientSocket);
                e.printStackTrace();
            } finally {
                try {
                    if(clientSocket !=null && !clientSocket.isClosed()){
                        clientSocket.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        private Reponse handleConsultation(Request request){
            Spectacle spectacle = FindByName(request.getNameSpectacle());
            
            if( spectacle ==null){
                return new Reponse(ResponseType.SPECTACLE_NOT_FOUND, "Spectacle not found: "+request.getNameSpectacle());
            }
            
            int placesDisponibles = spectacle.getPlacesDisponibles();
            return new Reponse(ResponseType.PLACES_DISPONIBLES, "Spectacle found: "+spectacle.getTitleSpectacle()+" with "+placesDisponibles+" places available.");
        }
    
        private Reponse handleReservation(Request request){
            Spectacle spectacle = FindByName(request.getNameSpectacle());
            
            if( spectacle ==null){
                return new Reponse(ResponseType.SPECTACLE_NOT_FOUND, "Spectacle not found: "+request.getNameSpectacle());
            }
            if(request.getNumberPlace() <=0){
                return new Reponse(ResponseType.ERROR, " Invalid number of places requested : " + request.getNumberPlace());
            }
            boolean success = spectacle.reserverPlaces(request.getNumberPlace());
            if(success){
                return new Reponse(request.getNumberPlace(), "Reservation successful for "+request.getNumberPlace()+" places in spectacle: "+spectacle.getTitleSpectacle(), ResponseType.RESERVATION_CONFIRMED);
            } else {
                return new Reponse(ResponseType.INSUFFICIENT_PLACES, "Reservation failed. Not enough places available in spectacle: "+spectacle.getTitleSpectacle());
            }
        }

        private Reponse handleListSpectacles(Request request){
            StringBuilder sb = new StringBuilder();
            for(Spectacle s : spectacles.values()){
                sb.append(". ").append(s.toString()).append("\n");
            }
            return new Reponse(ResponseType.LIST_SPECTACLES, sb.toString());
        }

        private Spectacle FindByName(String name){
            for(Spectacle s : spectacles.values()){
                if(s.getTitleSpectacle().equalsIgnoreCase(name)){
                    return s;
                }
            }
            return null;
        }
    }
}