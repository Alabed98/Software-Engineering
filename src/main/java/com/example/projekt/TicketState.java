package com.example.projekt;

public abstract class TicketState {
    public Ticket ticket;
    public abstract void bezahlen();
    public abstract void verlassen();
    public String getState(Ticket t) {
        return t.currentState.getClass().getSimpleName();
    }


}
