package Pholiday01;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Spectacle {
    private static Long compteurId = 0L;  

    private Long id;      
    private String titleSpectacle;
    private int placesTotales;
    private int placesDisponibles;
    private LocalDateTime dateTime;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private boolean[] siegesOccupes; // Array pour tracker les sièges occupés



    public Spectacle( String titleSpectacle, int placesDisponibles , LocalDateTime dateTime) {
        this.id = ++compteurId;
        this.titleSpectacle = titleSpectacle;
        this.placesTotales = placesDisponibles;
        this.placesDisponibles = placesDisponibles;
        this.dateTime = dateTime;
        this.siegesOccupes = new boolean[placesDisponibles]; // Initialiser l'array des sièges
    }

    public LocalDateTime getDateTime() {return dateTime;}
    public String  getFormatter() { return dateTime.format(formatter);}
    public Long getId() {return id;}
    public String getTitleSpectacle() {return titleSpectacle;}
    public synchronized int getPlacesDisponibles() {return placesDisponibles;}
    public void setTitleSpectacle(String titleSpectacle) {this.titleSpectacle = titleSpectacle;}
    public synchronized void setPlacesDisponibles(int placesDisponibles) {this.placesDisponibles = placesDisponibles;}
    public void setDateTime(LocalDateTime dateTime) {this.dateTime = dateTime;}
    
    @Override
    public String toString() {
        return "ID: " + id + ", Title: " + titleSpectacle + ", Places Available: " + placesDisponibles + ", Date & Time: " + getFormatter();
    }

    // Nouvelle méthode pour réserver des sièges spécifiques
    public synchronized List<Integer> reserverSieges(int nbPlaces){
        List<Integer> siegesReserves = new ArrayList<>();
        
        if(nbPlaces > placesDisponibles){
            return null; // Pas assez de places
        }
        
        int compteur = 0;
        for(int i = 0; i < siegesOccupes.length && compteur < nbPlaces; i++){
            if(!siegesOccupes[i]){ // Siège libre
                siegesOccupes[i] = true; // Marquer comme occupé
                siegesReserves.add(i + 1); // Numéro de siège (commence à 1)
                compteur++;
            }
        }
        
        placesDisponibles -= nbPlaces;
        return siegesReserves;
    }

    public synchronized boolean reserverPlaces(int nbPlaces){
        List<Integer> sieges = reserverSieges(nbPlaces);
        return sieges != null;
    }
    
}