package Pholiday01;

import java.io.Serializable;
import java.util.List;
import Pholiday01.enums.ResponseType;

public class Reponse implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private int nbPlacesReservees ;  
    private String message ;
    private ResponseType reponseType;
    private List<Integer> numerosSieges; // Numéros des sièges réservés

    public Reponse(int nbPlacesReservees, String message, ResponseType reponseType) {
        this.nbPlacesReservees = nbPlacesReservees;
        this.message = message;
        this.reponseType = reponseType;
        this.numerosSieges = null;
    }

    public Reponse (ResponseType reponseType, String message) {
        this.reponseType = reponseType;
        this.message = message;
        this.nbPlacesReservees = 0;
        this.numerosSieges = null;
    }
    
    // Constructeur avec numéros de sièges
    public Reponse(int nbPlacesReservees, String message, ResponseType reponseType, List<Integer> numerosSieges) {
        this.nbPlacesReservees = nbPlacesReservees;
        this.message = message;
        this.reponseType = reponseType;
        this.numerosSieges = numerosSieges;
    }

    public int getNbPlacesReservees() {return nbPlacesReservees;}
    public String getMessage() {return message;}
    public ResponseType getReponseType() {return reponseType;}
    public List<Integer> getNumerosSieges() {return numerosSieges;}

}
