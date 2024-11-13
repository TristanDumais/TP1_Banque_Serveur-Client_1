package com.atoudeft.banque;

import java.io.Serializable;

public class CompteEpargne extends CompteBancaire implements Serializable {
    private final String numero;
    private double solde;
    private double tauxInteret;

    private static final double LIMITE_SOLDE = 1000.0;
    private static final double FRAIS_RETRAIT = 2.0;

    public CompteEpargne(String numero, double solde, double tauxInteret) {
        super(numero, TypeCompte.EPARGNE);
        this.numero = numero;
        this.solde = solde;
        this.tauxInteret = tauxInteret;
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
        if (montant > 0 || this.solde - montant > 0){
            if (this.solde < LIMITE_SOLDE){
                montant += FRAIS_RETRAIT;
            }
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


    public boolean ajouterInterets(){
        this.solde += this.solde * (this.tauxInteret);
        return false;
    }
}
