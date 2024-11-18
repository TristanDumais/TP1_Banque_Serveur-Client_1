package com.atoudeft.serveur;
import com.atoudeft.banque.TypeOperation;

import java.io.Serializable;
import java.util.Date;

public abstract class Operation implements Serializable {
    private TypeOperation type;
    private Date date;

    public Operation(TypeOperation type) {
        this.type = type;
        this.date = new Date(); // Utilise l'heure du système
    }

    public TypeOperation getType() {
        return type;
    }

    public Date getDate() {
        return date;
    }

    public abstract String toString();
}
