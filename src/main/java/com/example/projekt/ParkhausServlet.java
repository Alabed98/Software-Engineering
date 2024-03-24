package com.example.projekt;
import java.util.List;

import java.io.*;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
@WebServlet(name = "ParkhausServlet", value = "/ParkhausServlet")
public class ParkhausServlet extends  HttpServlet{
    Parkhaus parkhaus = new Parkhaus();
    String TicketID;
    int zuzahlen; // gib die Kosten pro Ticket an


    long anzahl; // gibt die Anzahl der Autos im Parkhaus an
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String aktion = request.getParameter("aktion");
        PrintWriter out = response.getWriter();

        if  (aktion.equals("Infos erhalten")) { // Gibt Informationen über ein Ticket zurück
            Ticket t = null;
            try {
                t = parkhaus.ticket(request.getParameter("TicketID"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            out.println("ID: " + t.TicketID+" Zeit: " + t.time +" Zustand: "+ t.currentState.getClass().getSimpleName());
        }
        else if (aktion.equals("bezahlen")) {

            String time = request.getParameter("Ausfahrtzeit");
            TicketID = request.getParameter("TicketID");

            try {
                zuzahlen = parkhaus.kostenberechnen(TicketID, time);
                if (zuzahlen < 0) throw new ServletException("Die Zeit stimmt nicht");

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            out.println("<html><body>");
            out.println("<h1>Zuzahlen: " + zuzahlen + ",00 &#8364;" + " </h1>");
            out.println("<form action='ParkhausServlet' method='post' ><input type='hidden' name='aktion' value='KostenZahlen'><input type='submit' value='Jetzt Zahlen'><form>");

            out.println("</body></html>");
        }
        else if (aktion.equals("Hauptseite")) {
            anzahl = VerfuegbarePlaetze();
            request.setAttribute("FreiePlaetze", anzahl);

            RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
            dispatcher.forward(request,response);
        }
        else out.println("Aktion nicht gefunden");
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
        String aktion = request.getParameter("aktion");
        PrintWriter out = response.getWriter();


        if (aktion.equals("Parkhaus einfahren")){
            String time = request.getParameter("EinfahrtZeit");

            try {
                parkhaus.einfahrt(time);
                anzahl = VerfuegbarePlaetze();
                request.setAttribute("FreiePlaetze", anzahl);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
            dispatcher.forward(request,response);
        }
        else if(aktion.equals("gueltige Tickets")){

            ArrayList<Ticket> aktuelleAutos = parkhaus.parkplaetze2.stream()
                    .filter(x -> x.currentState.getState(x).equals("Ausgedruckt")  || x.currentState.getState(x).equals("Bezahlt"))
                    .collect(Collectors.toCollection(ArrayList::new));

            out.println("<html><body>");
            out.println("<h1>Liste der aktuell parkenden Autos</h1>");
            out.println("<ol>");
            if(aktuelleAutos.size() == 0){
                out.println("Liste ist leer");
                out.println("</ol>");
                out.println("</body></html>");
            }
            else {
                int i;
                for(i=0; i<10; i++){
                    if(aktuelleAutos.get(i)==null)
                        continue;
                    out.println("<li>"+ "Ticket-ID: "+ aktuelleAutos.get(i).TicketID + " Time:  " +aktuelleAutos.get(i).time+" Zustand: " + aktuelleAutos.get(i).currentState.getClass().getSimpleName() + "</li>");
                }
                out.println("</ol>");
                out.println("</body></html>");
            }

        }
        else if(aktion.equals("Alle Tickets")){
            out.println("<html><body>");
            out.println("<h1>Liste aller Tickets</h1>");
            out.println("<ol>");
            if(parkhaus.parkplaetze2.size() == 0){

                out.println("Liste ist leer");
                out.println("</ol>");
                out.println("</body></html>");
            }
            else {
                int i;
                for(i=0; i<parkhaus.parkplaetze2.size(); i++){
                    if(parkhaus.parkplaetze2.get(i)==null)
                        continue;
                    out.println("<li>"+ "Ticket-ID: "+ parkhaus.parkplaetze2.get(i).TicketID + " Time:  " +parkhaus.parkplaetze2.get(i).time+" Zustand: " + parkhaus.parkplaetze2.get(i).currentState.getClass().getSimpleName() + "</li>");
                }
            }
        }
        else if (aktion.equals("KostenZahlen")) {
            try {
                boolean res = parkhaus.kostenZahlen(TicketID);
                if(res==true){
                    out.println("<html><body>");
                    out.println("<h1>Zahlung erfolgt:");
                    out.println(zuzahlen + ",00 EURO </h1>");
                    out.println("<form action='ParkhausServlet' method='get' ><input type='hidden' name='aktion' value='Hauptseite'><input type='submit' value='Zur&uuml;ck zur Hauptseite'><form>");
                    out.println("</body></html>");

                }
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }

        } else if (aktion.equals("ParkhausVerlassen")) {
            try {
                TicketID = request.getParameter("TicketID2");
                boolean res = parkhaus.parkhausVerlassen(TicketID);
                anzahl = VerfuegbarePlaetze();
                //if(res == false) throw new Exception("TicketID ist falsch");
                request.setAttribute("FreiePlaetze", anzahl);
            }
            catch (Exception e) {
                throw new RuntimeException(e);
            }
            RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
            dispatcher.forward(request, response);

        } else out.println("Liste ist leer");
    }

    long VerfuegbarePlaetze  (){
        return 10 - parkhaus.parkplaetze2.stream()
                .filter(x -> x.currentState.getState(x).equals("Ausgedruckt")  || x.currentState.getState(x).equals("Bezahlt"))
                .count();
    }

}
