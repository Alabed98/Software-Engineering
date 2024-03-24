package com.example.projekt;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.security.SecureRandom;
import java.util.stream.Collectors;


public class Parkhaus  {
    static ArrayList <Ticket> parkplaetze2 = new ArrayList<>();

    /**
     * Die Methode generiert für jedes Auto, das das Parkhaus einfährt ein Ticket
     * Sie merkt sich die TicketID in dem Array Parkplaetze
     * @param time
     * @author Abdulrahman Alabed (aalabe2s)
     */
    static void einfahrt(String time) throws Exception {
        // Random random= new Random(); Code smell, SecureRandom.getInstanceStrong() ist besser!!!
        // Ticket t=new Ticket(Integer.toString(random.nextInt(10000)), time );

        Random rand = SecureRandom.getInstanceStrong();  // SecureRandom ist besser als Random
        Ticket t=new Ticket(Integer.toString(Math.abs(rand.nextInt())), time); //Math.abs umd die Zahl in positive Zahl umzuwandeln

        List<Ticket> gueltigeTickets =parkplaetze2.stream()
                .filter(x -> x.currentState.getState(x).equals("Ausgedruckt")  || x.currentState.getState(x).equals("Bezahlt"))
                .collect(Collectors.toList());

        if(gueltigeTickets.size() > 9)
            throw new Exception("Parkhaus ist voll!");

        parkplaetze2.add(t);
    }
    /**
     * Gibt ein Ticket anhand der TicketID zurück
     * @param TicketID
     * @return Ticket
     * @author Abdulrahman Alabed (aalabe2s)
     */
    Ticket ticket(String TicketID) throws Exception{

        for(Ticket a:parkplaetze2){
            if(a==null)
                throw new Exception ("Ticket-ID nicht gefunden");

            if(a.TicketID.equals(TicketID)){
                return a;
            }
        }
        throw new Exception ("Ticket-ID nicht gefunden");
    }

    /**
     * Die Methode rechnet die Parkkosten für Autos anhand der TicketID
     * @param TicketID
     * @param aktuTime
     * @return gibt die Kosten zurück
     * @throws Exception
     * @author Abdulrahman Alabed
     */
    int kostenberechnen(String TicketID, String aktuTime) throws Exception {
        if (parkplaetze2.size() == 0) throw new Exception("Parkhaus ist leer");
        Ticket gesucht = null;

        for (int i = 0 ; i < parkplaetze2.size(); i++){

            if(parkplaetze2.get(i).TicketID.equals(TicketID)){
                gesucht = parkplaetze2.get(i);
                break;
            }
        }

        if(gesucht == null) throw new Exception("Ticket-ID nicht gefunden");


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");

        Calendar calender = Calendar.getInstance();
        calender.setTime(formatter.parse(gesucht.time));

        Calendar calender2 = Calendar.getInstance();
        calender2.setTime(formatter.parse(aktuTime));

        int einfahrtHour = calender.get(Calendar.HOUR_OF_DAY);
        int einfahrtMinuts = calender.get(Calendar.MINUTE);

        int aktuHour =calender2.get((Calendar.HOUR_OF_DAY));
        int aktuMinuts = calender2.get(Calendar.MINUTE);

        int stunden =aktuHour - einfahrtHour ;
        int minuten = aktuMinuts - einfahrtMinuts;

        while (minuten>=60){
            stunden++;
            minuten=-60;
        }
// Hier kann man den Preis pro Stunde ändern
        int zubezahen = stunden * 2;
        if(minuten != 0)
            zubezahen+=2;

        return zubezahen;
    }

    /**
     * Die Methode setzt den Zustand eines Ticket auf bezahlt
     * @param TicketID
     * @return gibt zurück, ob TicketID gefunde und deren Zustand geändert wurde
     * @throws Exception
     * @author Abdulrahman Alabed
     */
    boolean kostenZahlen(String TicketID) throws Exception {
        if (parkplaetze2.size() == 0) throw new Exception("Parkhaus ist leer");
        int i ;
        for(i = 0; i< parkplaetze2.size() ; i++){
            // if (parkplaetze.length == 0 || (parkplaetze[i]==null && i == parkplaetze.length-1)) throw new Exception("Liste ist leer oder TicketID ist nicht korrekt");

            if(parkplaetze2.get(i).TicketID.equals(TicketID)){
                parkplaetze2.get(i).currentState.bezahlen();
                return true;

            }
        }

        throw new Exception("Ticket-ID nicht gefunden");
    }
    /**
     * Die Methode setzt den Zustand eines Ticket auf Verlassen, also ungültig
     * @param TicketID
     * @return gibt zurück, ob TicketID gefunde und deren Zustand geändert wurde
     * @throws Exception
     * @author Abdulrahman Alabed
     */
    boolean parkhausVerlassen(String TicketID) throws Exception{
        if (parkplaetze2.size() == 0) throw new Exception("Parkhaus ist leer");
        int i ;
        for(i = 0; i< parkplaetze2.size() ; i++){
            // if (parkplaetze.length == 0 || (parkplaetze[i]==null && i == parkplaetze.length-1)) throw new Exception("Liste ist leer oder AutoID ist nicht korrekt");
            if(parkplaetze2.get(i).TicketID.equals(TicketID)){
                parkplaetze2.get(i).currentState.verlassen();
                return true;
            }
        }
        throw new Exception("TicketID nicht gefunden");
    }
}
