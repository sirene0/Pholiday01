package Pholiday01;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.util.Scanner;

import Pholiday01.enums.RequestType;
import Pholiday01.enums.ResponseType;

public class Client {
    private String host = "localhost";
    private int port = 8081;

    private Scanner scanner;
    
    public Client() {
        scanner = new Scanner(System.in);
    }
    
    public void start() {
        System.out.println("Client started...");
        System.out.println("Connecting to server at " + host + ":" + port);

        while (true){
            afficherMenu();
            int choice= scanner.nextInt();
            scanner.nextLine(); // Consume newline
            switch(choice){
                case 1:
                    ConsulterSpectacles();
                    break;
                case 2:
                    ReserverPlaces();
                    break;
                case 3:
                    ListerSpectacles();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }    
    }

    private void  afficherMenu() {
        System.out.println(" Menu :");
        System.out.println("1. Consultation of spectacles");
        System.out.println("2. Reservation of places");
        System.out.println("3. Listing of spectacles");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    private void ConsulterSpectacles() {
        System.out.println("Consulting spectacles...");
        
        System.out.println("Enter the name of the spectacle (or leave empty to view all): ");
        String nameSpectacle = scanner.nextLine();

        if(nameSpectacle.trim().isEmpty()){
            System.out.println("Name of spectacle cannot be empty for consultation.");
            return;
        }
        Request request = new Request(RequestType.CONSULTATION ,nameSpectacle);
        Reponse reponse =envoyerRequest(request);

        if(reponse != null){
            if(reponse.getReponseType() == ResponseType.PLACES_DISPONIBLES){
                System.out.println("Response: "+reponse.getMessage());
            } else {
                System.out.println("Error: "+reponse.getMessage());
            }
        }
    }

    private void ReserverPlaces() {
        System.out.println("Reserving places...");

        System.out.println("Enter the name of the spectacle: ");
        String nameSpectacle = scanner.nextLine();

        if(nameSpectacle.trim().isEmpty()){
            System.out.println("Name of spectacle cannot be empty for reservation.");
            return;
        }

        System.out.println("Enter the number of places to reserve: ");
        int numberPlace = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        if(numberPlace <=0){
            System.out.println("Invalid number of places. Must be greater than zero.");
            return;
        }

        Request request = new Request(RequestType.RESERVATION ,nameSpectacle, numberPlace);
        Reponse reponse =envoyerRequest(request);

        if(reponse != null){
            switch (reponse.getReponseType()){
                case RESERVATION_CONFIRMED:
                    System.out.println("Success : "+reponse.getMessage());
                    break;
                case INSUFFICIENT_PLACES:
                    System.out.println("Error : "+reponse.getMessage());
                    break;
                case SPECTACLE_NOT_FOUND:
                    System.out.println("Error : "+reponse.getMessage());
                    break;
                default:
                    System.out.println("Error : "+reponse.getMessage());
                    break;
            }
        }
    }

    private void ListerSpectacles() {
        System.out.println("Listing all spectacles...");

        Request request = new Request(RequestType.LIST_SPECTACLES);
        Reponse reponse =envoyerRequest(request);

        if(reponse != null){
            if(reponse.getReponseType() == ResponseType.LIST_SPECTACLES){
                System.out.println("Spectacles:\n"+reponse.getMessage());
            } else {
                System.out.println("Error: "+reponse.getMessage());
            }
        }
    }
    
    private Reponse envoyerRequest(Request request) {
        try (Socket socket = new Socket(host, port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

            out.writeObject(request);
            out.flush();

            Reponse reponse = (Reponse) in.readObject();
            return reponse;
            
        } catch (ConnectException e) {
            System.out.println("Connection refused. Is the server running?");
            System.out.println("Error communicating with server: " + e.getMessage() +" verified if server is running !");
            return null;
        } catch (IOException e){
            System.out.println("Error in I/O operation : "+e.getMessage());
            return null;
        } catch (ClassNotFoundException e){
            System.out.println("Error in reading response from server : "+e.getMessage());
            return null;  
        }  
    }
    
    public static void main(String[] args) {
        System.out.println("Client is running...");
        Client client = new Client();
        client.start();
    }

}
