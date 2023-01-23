package model;

public class Training {
    public int pk_training;
    public int fk_trainer;
    public int fk_participant;

    public Training(int fk_trainer, int fk_participant) {
        this.fk_trainer = fk_trainer;
        this.fk_participant = fk_participant;
    }

    public Training(int pk_training, int fk_trainer, int fk_participant) {
        this.pk_training = pk_training;
        this.fk_trainer = fk_trainer;
        this.fk_participant = fk_participant;
    }

    @Override
    public String toString() {
        return this.fk_trainer + " " + this.fk_participant;
    }
}
