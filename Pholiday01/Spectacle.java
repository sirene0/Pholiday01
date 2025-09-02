package Pholiday01;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Spectacle {
    private static Long compteurId = 0L;  

    private Long id;      
    private String titleSpectacle;
    private int placesDisponibles;
    private LocalDateTime dateTime;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");



    public Spectacle( String titleSpectacle, int placesDisponibles , LocalDateTime dateTime) {
        this.id = ++compteurId;
        this.titleSpectacle = titleSpectacle;
        this.placesDisponibles = placesDisponibles;
        this.dateTime = dateTime;
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

    public synchronized boolean reserverPlaces(int nbPlaces){
        if(nbPlaces <= placesDisponibles){
            placesDisponibles -= nbPlaces;
            return true;
        } else {
            return false;
        }
    }
    
}