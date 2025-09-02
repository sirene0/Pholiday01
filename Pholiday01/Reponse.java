package Pholiday01;

import java.io.Serializable;
import Pholiday01.enums.ResponseType;

public class Reponse implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private int nbPlacesReservees ;  
    private String message ;
    private ResponseType reponseType;
    private String numeroReservation; // Nouveau champ pour le numéro de réservation

    public Reponse(int nbPlacesReservees, String message, ResponseType reponseType) {
        this.nbPlacesReservees = nbPlacesReservees;
        this.message = message;
        this.reponseType = reponseType;
        this.numeroReservation = null;
    }

    public Reponse (ResponseType reponseType, String message) {
        this.reponseType = reponseType;
        this.message = message;
        this.nbPlacesReservees = 0;
        this.numeroReservation = null;
    }
    
    // Constructeur avec numéro de réservation pour les confirmations
    public Reponse(int nbPlacesReservees, String message, ResponseType reponseType, String numeroReservation) {
        this.nbPlacesReservees = nbPlacesReservees;
        this.message = message;
        this.reponseType = reponseType;
        this.numeroReservation = numeroReservation;
    }

    public int getNbPlacesReservees() {return nbPlacesReservees;}
    public String getMessage() {return message;}
    public ResponseType getReponseType() {return reponseType;}
    public String getNumeroReservation() {return numeroReservation;}

}
