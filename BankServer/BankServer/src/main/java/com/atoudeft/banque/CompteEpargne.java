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

    /**
     * Crédite un montant sur le compte bancaire.
     *
     * @param montant Le montant à créditer sur le compte. Doit être strictement supérieur à 0.
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
     *
     * @param montant
     * @return
     */
    @Override
    public boolean debiter(double montant) {
        if (montant > 0 && this.solde - montant > 0){
            if (this.solde < LIMITE_SOLDE){
                montant += FRAIS_RETRAIT;
            }
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

    /**
     *
     * @return
     */
    aImplementerDansLeCode
    public boolean ajouterInterets(){
        this.solde += this.solde * (this.tauxInteret);
        return false;
    }
}