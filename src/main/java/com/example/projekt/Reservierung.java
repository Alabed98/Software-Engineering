
package com.example.projekt;
/*
 * @author Sedat  Coskun scosku2s
 * diese Klasse wird verwendet, um Reservierungsobjekte zu erstellen, die dann in der reservierungen-Liste in der Parkhaus-Klasse gespeichert werden. 
 */
 
public class Reservierung {
    String reservierungszeit;
    String autoID;

    public Reservierung(String reservierungszeit, String autoID) {
        this.reservierungszeit = reservierungszeit;
        this.autoID = autoID;
    }
}
