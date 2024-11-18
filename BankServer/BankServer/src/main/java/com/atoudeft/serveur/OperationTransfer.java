package com.atoudeft.serveur;

import com.atoudeft.banque.TypeOperation;

public class OperationTransfer extends Operation {
    private double montant;
    private String numeroCompteDestinataire;

    public OperationTransfer(double montant, String numeroCompteDestinataire) {
        super(TypeOperation.TRANSFER);
        this.montant = montant;
        this.numeroCompteDestinataire = numeroCompteDestinataire;
    }

    @Override
    public String toString() {
        return getDate() + " TRANSFER " + montant + " " + numeroCompteDestinataire;
    }
}

