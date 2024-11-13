package com.atoudeft.banque;

import java.io.Serializable;

public class CompteCheque extends CompteBancaire implements Serializable {
    private final String numero;
    private double solde;

    public CompteCheque(String numero, double solde) {
        super(numero, TypeCompte.CHEQUE);
        this.numero = numero;
        this.solde = solde;
    }

    @Override
    public boolean crediter(double montant){
        if (montant > 0){
           this.solde  += montant;
        }
        return false;
    }

    @Override
    public boolean debiter(double montant) {
        if (montant > 0){
            this.solde  -= montant;
        }
        return false;
    }

    @Override
    public boolean payerFacture(String numeroFacture, double montant, String description) {
        return false;
    }

    @Override
    public boolean transferer(double montant, String numeroCompteDestinataire) {
        return false;
    }
}
