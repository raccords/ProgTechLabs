package model;

import java.sql.Date;

public class Result {
    public int pk_result;
    public int fk_participant;
    public int score;
    public Date date;

    public Result(int fk_participant, int score, Date date) {
        this.fk_participant = fk_participant;
        this.score = score;
        this.date = date;
    }

    public Result(int pk_result,int fk_participant, int score, Date date) {
        this.pk_result = pk_result;
        this.fk_participant = fk_participant;
        this.score = score;
        this.date = date;
    }

    @Override
    public String toString() {
        return this.pk_result + " " + this.score + " " + date;
    }
}
