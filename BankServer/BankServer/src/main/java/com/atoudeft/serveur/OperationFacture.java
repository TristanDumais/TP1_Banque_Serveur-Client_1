package com.atoudeft.serveur;

import com.atoudeft.banque.TypeOperation;

public class OperationFacture extends Operation {
    private double montant;
    private String numerodeFacture;
    private String description;

    public OperationFacture(double montant, String numeroFacture, String description) {
        super(TypeOperation.FACTURE);
        this.montant = montant;
        this.numerodeFacture = numeroFacture;
        this.description = description;
    }

    @Override
    public String toString() {
        return getDate() + " FACTURE " + montant + " " + numerodeFacture + " " + description;
    }
}

