package model;

import java.sql.Date;

public class Participant {
    public int pk_participant;
    public int height;
    public int weight;
    public String rank;
    public String f_name;
    public String s_name;
    public String m_name;
    public Date birthDate;

    public Participant(int height, int weight, Date birthDate, String f_name, String s_name, String m_name, String rank) {
        this.height = height;
        this.weight = weight;
        this.f_name = f_name;
        this.s_name = s_name;
        this.m_name = m_name;
        this.birthDate = birthDate;
        this.rank = rank;
    }

    public Participant(int height, int weight, Date birthDate, String f_name, String s_name, String m_name, String rank, int pk_participant) {
        this.height = height;
        this.weight = weight;
        this.f_name = f_name;
        this.s_name = s_name;
        this.m_name = m_name;
        this.birthDate = birthDate;
        this.rank = rank;
        this.pk_participant = pk_participant;
    }

    @Override
    public String toString() {
        return this.f_name + " " + this.m_name + " " + this.s_name + " " + birthDate;
    }


}
