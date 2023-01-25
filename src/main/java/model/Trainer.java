package model;

import java.sql.Date;

public class Trainer {
    public int pk_trainer;
    public String f_name;
    public String s_name;
    public Date birthDate;

    public Trainer(Date birthDate, String f_name, String s_name) {
        this.f_name = f_name;
        this.s_name = s_name;
        this.birthDate = birthDate;
    }

    public Trainer(Date birthDate, String f_name, String s_name, int pk_trainer) {
        this.f_name = f_name;
        this.s_name = s_name;
        this.birthDate = birthDate;
        this.pk_trainer = pk_trainer;
    }

    @Override
    public String toString() {
        return this.f_name + " " + this.s_name + " " + birthDate;
    }
}
