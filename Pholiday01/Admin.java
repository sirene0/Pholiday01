package Pholiday01;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import Pholiday01.enums.RequestType;
import Pholiday01.enums.ResponseType;

public class Admin {
    private String host = "localhost";
    private int port = 8081;
    
    private Scanner scanner ;

    public Admin(){
        scanner = new Scanner(System.in);
    }

    public void start(){
        System.out.println("Admin started...");
        System.out.println("Connecting to server at "+host+" : "+port);

        while(true){
            afficherMenu();
            int choice =scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    AjouterSpectacle();
                    break;
                case 2:
                    SupprimerSpectacle();
                    break;
                case 3:
                    ModifierSpectacle();
                    break;
                case 4:
                    listerSpectacles();
                    break;
                case 5:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
    }

    private void afficherMenu(){
        System.out.println(" Menu :");
        System.out.println("1. Add spectacle");
        System.out.println("2. Remove spectacle");
        System.out.println("3. Modify spectacle");
        System.out.println("4. List spectacles");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }
    
    private void AjouterSpectacle(){
        System.out.println("Adding spectacle...");
        
        String name;
        // Implementation for adding a spectacle
        do{
            System.out.println("Enter the name of the spectacle :");
            name = scanner.nextLine().trim();
            if(name.isEmpty()){
                System.out.println("Name of spectacle cannot be empty.");
                continue;
            }
            if(spectacleExists(name)){
                System.out.println("Name of spectacle already exists, please choose another name.");
            }
        }while(name.isEmpty() || spectacleExists(name));

        int totalPlaces;
        do {
            System.out.println("Enter the total number of places :");
            totalPlaces = scanner.nextInt();
            scanner.nextLine();
            if(totalPlaces <=0){
                System.out.println("Total number of places must be greater than zero.");
            }
        } while (totalPlaces <= 0);

        String date;
        do {
            System.out.println("Enter the date of the spectacle (dd/MM/yyyy HH:mm) :");
            date = scanner.nextLine();
            if(!verifiedDate(date)){
                System.out.println("Invalid date format. Please use dd/MM/yyyy HH:mm.");
                continue;
            }
            if(!dateIsFree(date)){
                System.out.println("Date already booked, please choose another date.");
                continue;
            }
            break;
        } while (true);
        
        // Send request to server to add spectacle
        Request request = new Request(RequestType.ADD_SPECTACLE, name, totalPlaces, date);
        Reponse reponse = envoyerRequest(request);
        if(reponse !=null){
            if(reponse.getReponseType() == ResponseType.SPECTACLE_ADDED){
                System.out.println("Spectacle added successfully: "+reponse.getMessage());
            } else {
                System.out.println("Error adding spectacle: "+reponse.getMessage());
            }
        }
    }
    
    private void SupprimerSpectacle(){
        System.out.println("Removing spectacle...");
        
        System.out.println("Enter the name of the spectacle to remove :");
        String name = scanner.nextLine();
        if(name.trim().isEmpty()){
            System.out.println("Name of spectacle cannot be empty.");
            return;
        }
        // Send request to server to remove spectacle
        Request request = new Request(RequestType.REMOVE_SPECTACLE, name);
        Reponse reponse = envoyerRequest(request);
        if(reponse !=null){
            if(reponse.getReponseType() == ResponseType.SPECTACLE_REMOVED){
                System.out.println("Spectacle removed successfully: "+reponse.getMessage());
            } else {
                System.out.println("Error removing spectacle: "+reponse.getMessage());
            }
        }
    }

    private void ModifierSpectacle(){
        System.out.println("Modifying spectacle...");
        listerSpectacles();
        System.out.println();
        
        System.out.println("Enter the name of the spectacle to modify :");
        String name = scanner.nextLine();
        if(name.trim().isEmpty()){
            System.out.println("Name of spectacle cannot be empty.");
            return;
        }
        
        Long idSpectacle = getIdSpectacle(name);
        if(idSpectacle == null){
            System.out.println("Spectacle not found.");
            return;
        }

        String newName;
        do{
            System.out.println("Enter the new name of the spectacle :");
            newName = scanner.nextLine().trim();
            if(newName.isEmpty()){
                System.out.println("Name of spectacle cannot be empty.");
                continue;
            }
            if(!newName.equalsIgnoreCase(name) && spectacleExists(newName)){
                System.out.println("Name of spectacle already exists, please choose another name.");
                continue;
            }
            break;
        }while(true);

        int totalPlaces;
        do {
            System.out.println("Enter the new total number of places :");
            totalPlaces = scanner.nextInt();
            scanner.nextLine();
            if(totalPlaces <=0){
                System.out.println("Total number of places must be greater than zero.");
            }
        } while (totalPlaces <= 0);

        String date;
        do {
            System.out.println("Enter the new date of the spectacle (dd/MM/yyyy HH:mm) :");
            date = scanner.nextLine().trim();
            if(!verifiedDate(date)){
                System.out.println("Invalid date format. Please use dd/MM/yyyy HH:mm.");
                continue;
            }
            if(!isDateFreeForModification(date, idSpectacle)){
                System.out.println("Date already booked by another spectacle.");
                continue;
            }
            break;
        } while (true);
        
        // Send request to server to modify spectacle
        Request request = new Request(RequestType.MODIFY_SPECTACLE, newName, totalPlaces, idSpectacle, date);
        Reponse reponse = envoyerRequest(request);
        if(reponse !=null){
            if(reponse.getReponseType() == ResponseType.SPECTACLE_MODIFIED){
                System.out.println("Spectacle modified successfully: "+reponse.getMessage());
            } else {
                System.out.println("Error modifying spectacle: "+reponse.getMessage());
            }
        }
    }

    private void listerSpectacles(){
        System.out.println("Listing spectacles...");
        Request request = new Request(RequestType.LIST_SPECTACLES);
        Reponse reponse = envoyerRequest(request);
        if(reponse !=null){
            if(reponse.getReponseType() == ResponseType.LIST_SPECTACLES){
                System.out.println("Spectacles:\n"+reponse.getMessage());
            } else {
                System.out.println("Error listing spectacles: "+reponse.getMessage());
            }
        }
    }
    
    private boolean spectacleExists(String name){
        Request request = new Request(RequestType.CONSULTATION, name);
        Reponse reponse = envoyerRequest(request);
        return reponse != null && reponse.getReponseType() == ResponseType.PLACES_DISPONIBLES;
    }

    private Long getIdSpectacle(String name){
        Request request = new Request(RequestType.LIST_SPECTACLES);
        Reponse reponse = envoyerRequest(request);
        if(reponse !=null && reponse.getReponseType() == ResponseType.LIST_SPECTACLES){
            String[] lines = reponse.getMessage().split("\n");
            for(String line : lines){
                line = line.trim(); // Remove leading/trailing spaces
                if(line.isEmpty()) continue; // Skip empty lines
                
                // More flexible search - look for the title anywhere in the line
                if(line.toLowerCase().contains("title: " + name.toLowerCase())){
                    // Extract ID from the line
                    int idStart = line.indexOf("ID: ");
                    if(idStart >= 0){
                        idStart += 4; // Move past "ID: "
                        int idEnd = line.indexOf(",", idStart);
                        if(idEnd > idStart){
                            try {
                                String idStr = line.substring(idStart, idEnd).trim();
                                return Long.parseLong(idStr);
                            } catch (NumberFormatException e) {
                                System.out.println("Error parsing ID from line: " + line);
                            }
                        }
                    }
                }
            }
            System.out.println("Spectacle '" + name + "' not found in the list.");
        }
        return null;
    }

    private boolean isDateFreeForModification(String date, Long idSpectacle){
        Request request = new Request(RequestType.LIST_SPECTACLES);
        Reponse reponse = envoyerRequest(request);
        if(reponse !=null && reponse.getReponseType() == ResponseType.LIST_SPECTACLES){
            String[] lines = reponse.getMessage().split("\n");
            for(String line : lines){
                if(line.contains(date)){
                    // Check if it's the same spectacle
                    try {
                        String idPart = line.substring(line.indexOf("ID: ") + 4, line.indexOf(","));
                        Long lineId = Long.parseLong(idPart.trim());
                        if(!lineId.equals(idSpectacle)){
                            return false; // Different spectacle has this date
                        }
                    } catch (Exception e) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    
    private boolean verifiedDate(String date){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            formatter.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    private boolean dateIsFree(String date){
        Request request = new Request(RequestType.LIST_SPECTACLES);
        Reponse reponse =envoyerRequest(request);
        if(reponse !=null && reponse.getReponseType() == ResponseType.LIST_SPECTACLES){
            String[] spectacles = reponse.getMessage().split("\n");
            for(String spectacle : spectacles){
                if(spectacle.contains(date)){
                    return false;
                }
            }
        }
        return true;
    }
    
    private Reponse envoyerRequest(Request request ){
        try (Socket socket =new Socket(host, port);
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ){

                out.writeObject(request);
                out.flush();
                Reponse reponse =(Reponse) in.readObject();
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
        System.out.println(" Admin is running...");
        Admin admin = new Admin();
        admin.start();
    }
}

