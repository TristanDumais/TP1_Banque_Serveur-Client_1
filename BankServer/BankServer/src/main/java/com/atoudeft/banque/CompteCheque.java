package com.atoudeft.banque;

public class CompteCheque extends CompteBancaire {

    public CompteCheque(String numero) {
        super(numero, TypeCompte.CHEQUE);
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
