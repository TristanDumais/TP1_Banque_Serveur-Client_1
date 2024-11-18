package com.atoudeft.serveur;

import com.atoudeft.banque.TypeOperation;


public class OperationRetrait extends Operation {
    private double montant;

    public OperationRetrait(double montant) {
        super(TypeOperation.RETRAIT);
        this.montant = montant;
    }

    public double getMontant () {
        return montant;
    }

    @Override
    public String toString() {
        return getDate() + " RETRAIT " + montant;
    }
}

