package com.atoudeft.serveur;

import com.atoudeft.banque.TypeOperation;


public class OperationDepot extends Operation {
    private double montant;

    public OperationDepot(double montant) {
        super(TypeOperation.DEPOT);
        this.montant = montant;
    }

    public double getMontant() {
        return montant;
    }

    @Override
    public String toString() {
        return getDate() + " DEPOT " + montant;
    }
}
