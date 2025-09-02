package Pholiday01;

import java.io.Serializable;

import Pholiday01.enums.RequestType;

public class Request  implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private RequestType type;
    private String nameSpectacle;
    private int numberPlace;
    private Long idSpectacle;
    
    public Request(RequestType type, String nameSpectacle, int numberPlace, Long idSpectacle) {
        this.type = type;
        this.nameSpectacle = nameSpectacle;
        this.numberPlace = numberPlace;
        this.idSpectacle = idSpectacle;
    }

    public Request(RequestType type, String nameSpectacle, int numberPlace) {
        this.type = type;
        this.nameSpectacle = nameSpectacle;
        this.numberPlace = numberPlace;
        this.idSpectacle = null;
    }

    public Request(RequestType type, String nameSpectacle) {
        this.type = type;
        this.nameSpectacle = nameSpectacle;
        this.numberPlace = 0;
        this.idSpectacle = null;
    }
    
    public Request(RequestType type) {
        this.type = type;
        this.nameSpectacle = "";
        this.numberPlace = 0;
        this.idSpectacle = null;
    }
    public RequestType getType() {return type;}
    public String getNameSpectacle() {return nameSpectacle;}
    public int getNumberPlace() {return numberPlace;}
    public Long getIdSpectacle() {return idSpectacle;}

    

}
