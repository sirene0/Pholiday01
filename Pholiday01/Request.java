package Pholiday01;

import java.io.Serializable;


import Pholiday01.enums.RequestType;

public class Request  implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private RequestType type;
    private String nameSpectacle;
    private int numberPlace;
    private Long idSpectacle;
    private String dateTime; // New field for date and time
    
    

    //add
    public Request(RequestType type, String nameSpectacle, int numberPlace,  String dateTime) {
        this.type = type;
        this.nameSpectacle = nameSpectacle;
        this.numberPlace = numberPlace;
        this.idSpectacle = null;
        this.dateTime = dateTime;
    }

    //modify
    public Request(RequestType type, String nameSpectacle, int numberPlace, Long idSpectacle, String dateTime) {
        this.type = type;
        this.nameSpectacle = nameSpectacle;
        this.numberPlace = numberPlace;
        this.dateTime = dateTime;
        this.idSpectacle = idSpectacle;
    }
    //reservation
    public Request(RequestType type, String nameSpectacle, int numberPlace) {
        this.type = type;
        this.nameSpectacle = nameSpectacle;
        this.numberPlace = numberPlace;
        this.idSpectacle = null;
    }
    //consultation
    public Request(RequestType type, String nameSpectacle) {
        this.type = type;
        this.nameSpectacle = nameSpectacle;
        this.numberPlace = 0;
        this.idSpectacle = null;
        this.dateTime = null;
    }
    //list spectacles
    public Request(RequestType type) {
        this.type = type;
        this.nameSpectacle = "";
        this.numberPlace = 0;
        this.idSpectacle = null;
        this.dateTime = null;
    }
    public RequestType getType() {return type;}
    public String getNameSpectacle() {return nameSpectacle;}
    public int getNumberPlace() {return numberPlace;}
    public Long getIdSpectacle() {return idSpectacle;}
    public String getDateTime() {return dateTime;}
    
    

    }
