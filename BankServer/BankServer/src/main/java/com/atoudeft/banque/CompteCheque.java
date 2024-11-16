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

    /**
     * Crédite un montant sur le compte bancaire.
     *
     * @param montant Le montant à créditer sur le compte. Doit être supérieur à 0.
     * @return true si l'opération a été réalisée avec succès, false sinon.
     */
    @Override
    public boolean crediter(double montant){
        if (montant > 0){
           this.solde  += montant;
           return true;
        }
        return false;
    }

    /**
     * Débite un montant du compte bancaire.
     *
     * @param montant Le montant à débiter du compte. Doit être supérieur à 0 et ne doit pas dépasser le solde disponible.
     * @return true si l'opération a été réalisée avec succès, false sinon.
     */
    @Override
    public boolean debiter(double montant) {
        if (montant > 0 && this.solde - montant > 0){
            this.solde  -= montant;
            return true;
        }
        return false;
    }

    /**
     *Pour le moment, retourne false et ne fait rien????
     *
     * @param numeroFacture
     * @param montant
     * @param description
     * @return
     */
    @Override
    public boolean payerFacture(String numeroFacture, double montant, String description) {
        return false;
    }

    /**
     *Pour le moment, retourne false et ne fait rien????
     *
     * @param montant
     * @param numeroCompteDestinataire
     * @return
     */
    @Override
    public boolean transferer(double montant, String numeroCompteDestinataire) {
        return false;
    }
}
