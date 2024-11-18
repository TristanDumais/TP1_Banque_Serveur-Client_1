package com.atoudeft.serveur;

import java.io.Serializable;

public class Noeud implements Serializable {
    private Operation operation;
    private Noeud next;

    public Noeud(Operation operation) {
        this.operation = operation;    // valeur gardé dans le noeud
        this.next = null;      // pointe au prochain noeud
    }

    // Getter et Setter pour operation
    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    // Getter et Setter pour Next
    public Noeud getNext() {
        return next;
    }

    public void setNext(Noeud next ) {
        this.next = next;

    }
}
