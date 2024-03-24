package com.example.projekt;

public class Ticket {


    String time;
    String TicketID;
    TicketState currentState;

    public Ticket(String TicketID, String time){
        currentState = new Ausgedruckt(this);
        this.TicketID = TicketID;
        this.time = time;
    }

    static class Ausgedruckt extends TicketState{
        public Ausgedruckt(Ticket ticket){
            this.ticket = ticket;
        }


        @Override
        public void bezahlen() {
            ticket.currentState = new Bezahlt(ticket);
        }

        @Override
        public void verlassen() {
            throw new IllegalStateException("Ticket ist noch nicht bezahlt");
        }


    }


    static class Bezahlt extends TicketState{

        public Bezahlt(Ticket ticket){
            this.ticket = ticket;
        }


        @Override
        public void bezahlen() {
            throw new IllegalStateException("Ticket ist schon bezahlt");
        }

        @Override
        public void verlassen() {
            // TODO Auto-generated method stub
            ticket.currentState = new Verlassen(ticket);
        }



    }


    static class Verlassen extends TicketState {

        public Verlassen(Ticket ticket){
            this.ticket = ticket;
        }
        @Override
        public void bezahlen() {
            // TODO Auto-generated method stub
            throw new IllegalStateException("Ticket ist nicht mehr gültig, Sie haben das Parkhaus verlassen");
        }


        @Override
        public void verlassen() {
            // TODO Auto-generated method stub
            throw new IllegalStateException("Ticket ist nicht mehr gültig, Sie haben das Parkhaus verlassen");
        }

    }


}