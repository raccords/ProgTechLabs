import model.Participant;
import model.Result;
import model.Trainer;
import model.Training;

import java.io.Console;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.sql.DriverManager;

public class DbUtils {
    private static Connection con;

    public DbUtils() throws SQLException {
        String url = "jdbc:postgresql://localhost:5432/olympiad";
        Properties props = new Properties();
        props.setProperty("user", "postgres");
        props.setProperty("password", "12345");
        props.setProperty("ssl", "false");
        con = DriverManager.getConnection(url, props);
    }

    public static ResultSet getTable(String tableName) throws SQLException {
        var statement = con.prepareStatement("SELECT * from " + tableName + ";");
        return statement.executeQuery();
    }

    public List<String> getDbList(Boolean justViews) throws SQLException {
        ArrayList<String> tables = new ArrayList<String>();
        DatabaseMetaData dbmd = con.getMetaData();
        String[] query = new String[]{"VIEW"};
        if (!justViews)
            query = new String[]{"VIEW", "TABLE"};
        ResultSet t = dbmd.getTables(null, null, "%", query);
        while (t.next()) {
            tables.add((t.getString("TABLE_NAME")));
        }
        return tables;
    }

    public void insertParticipant(Participant participant) throws SQLException {

        var statement = con.prepareStatement("INSERT INTO participants (height, weight, birth_date, f_name, s_name, rank) VALUES (?,?,?,?,?,?)");
        statement.setInt(1, participant.height);
        statement.setInt(2, participant.weight);
        statement.setDate(3, participant.birthDate);
        statement.setString(4, participant.f_name);
        statement.setString(5, participant.s_name);
        statement.setString(6, participant.rank);
        statement.execute();
    }

    public void updateParticipant(Participant participant) throws SQLException {
        var statement = con.prepareStatement("UPDATE participants SET height = ?, weight =? , birth_date= ?, f_name= ?, s_name= ?, rank=? WHERE participant_id = ?");
        statement.setInt(1, participant.height);
        statement.setInt(2, participant.weight);
        statement.setDate(3, participant.birthDate);
        statement.setString(4, participant.f_name);
        statement.setString(5, participant.s_name);
        statement.setString(6, participant.rank);
        statement.setInt(7, participant.pk_participant);
        statement.execute();
    }

    public void insertTrainer(Trainer trainer) throws SQLException {
        var statement = con.prepareStatement("INSERT INTO trainers (birth_date, f_name, s_name, rank) VALUES (?,?,?,?)");
        statement.setDate(1, trainer.birthDate);
        statement.setString(2, trainer.f_name);
        statement.setString(3, trainer.s_name);
        statement.setString(4, trainer.rank);
        statement.execute();
    }

    public void updateTrainer(Trainer trainer) throws SQLException {
        var statement = con.prepareStatement("UPDATE trainers SET birth_date= ?, f_name= ?, s_name= ?, rank=? WHERE trainer_id = ?");
        statement.setDate(1, trainer.birthDate);
        statement.setString(2, trainer.f_name);
        statement.setString(3, trainer.s_name);
        statement.setString(4, trainer.rank);
        statement.setInt(5, trainer.pk_trainer);
        statement.execute();
    }

    public void insertTraining(Training training) throws SQLException {
        var statement = con.prepareStatement("INSERT INTO training (trainer_id, participant_id) VALUES (?,?)");
        statement.setInt(1, training.fk_trainer);
        statement.setInt(2, training.fk_participant);
        statement.execute();
    }

    public void updateTraining(Training training) throws SQLException {
        var statement = con.prepareStatement("UPDATE training SET trainer_id=?, participant_id=? WHERE training_id = ? ");
        statement.setInt(1, training.fk_trainer);
        statement.setInt(2, training.fk_participant);
        statement.setInt(3, training.pk_training);
        statement.execute();
    }

    public void insertResult(Result result) throws SQLException {
        var statement = con.prepareStatement("INSERT INTO results (date, participant_id, score) VALUES (?,?,?)");
        statement.setDate(1, result.date);
        statement.setInt(2, result.fk_participant);
        statement.setInt(3, result.score);
        statement.execute();
    }

    public void updateResult(Result result) throws SQLException {
        var statement = con.prepareStatement("UPDATE results SET date=?, participant_id=?, score=? WHERE result_id = ?");
        statement.setDate(1, result.date);
        statement.setInt(2, result.fk_participant);
        statement.setInt(3, result.score);
        statement.setInt(4, result.pk_result);
        statement.execute();
    }




    public ArrayList<Participant> getParticipants() throws SQLException {
        var statement = con.prepareStatement("SELECT participants.height,\n" +
                "    participants.weight,\n" +
                "    participants.birth_date,\n" +
                "    participants.f_name,\n" +
                "    participants.s_name,\n" +
                "    participants.rank,\n" +
                "    participants.participant_id\n" +
                "    FROM participants;");
        ArrayList<Participant> participants = new ArrayList<Participant>();
        var result = statement.executeQuery();
        int i = 1;
        while (result.next()) {
            participants.add(new Participant(
                            result.getInt(1),
                            result.getInt(2),
                            result.getDate(3),
                            result.getString(4),
                            result.getString(5),
                            result.getString(6),
                            result.getInt(7)
                    )
            );
            i++;
        }
        return participants;
    }

    public ArrayList<Trainer> getTrainers() throws SQLException {
        var statement = con.prepareStatement("SELECT trainers.birth_date,\n" +
                "    trainers.f_name,\n" +
                "    trainers.s_name,\n" +
                "    trainers.rank,\n" +
                "    trainers.trainer_id\n" +
                "   FROM trainers;");
        ArrayList<Trainer> trainers = new ArrayList<Trainer>();
        var result = statement.executeQuery();
        while (result.next()) {
            trainers.add(new Trainer(
                            result.getDate(1),
                            result.getString(2),
                            result.getString(3),
                            result.getString(4),
                            result.getInt(5)
                    )
            );
        }
        return trainers;
    }

    public ArrayList<Training> getTrainings() throws SQLException {
        var statement = con.prepareStatement("SELECT training.training_id,\n" +
                "    training.trainer_id,\n" +
                "    training.participant_id\n" +
                "   FROM training;");
        ArrayList<Training> trainings = new ArrayList<Training>();
        var result = statement.executeQuery();
        while (result.next()) {
            trainings.add(new Training(
                            result.getInt(1),
                            result.getInt(2),
                            result.getInt(3)
                    )
            );
        }
        return trainings;
    }

    public ArrayList<Result> getResults() throws SQLException {
        var statement = con.prepareStatement("SELECT results.result_id,\n" +
                "    results.participant_id,\n" +
                "    results.score,\n" +
                "    results.date\n" +
                "   FROM results;");
        ArrayList<Result> results = new ArrayList<Result>();
        var result = statement.executeQuery();
        while (result.next()) {
            results.add(new Result(
                            result.getInt(1),
                            result.getInt(2),
                            result.getInt(3),
                            result.getDate(4)
                    )
            );
        }
        return results;
    }

    public void deleteResult(int id) throws SQLException {
        var statement = con.prepareStatement("DELETE FROM results WHERE result_id = ?");
        statement.setInt(1, id);
        statement.execute();
    }

    public void deleteParticipant(int id) throws SQLException {
        var statement = con.prepareStatement("DELETE FROM participants WHERE participant_id = ?");
        statement.setInt(1, id);
        statement.execute();
    }
    public void deleteTraining(int id) throws SQLException {
        var statement = con.prepareStatement("DELETE FROM results WHERE training_id = ?");
        statement.setInt(1, id);
        statement.execute();
    }

    public void deleteTrainer(int id) throws SQLException {
        var statement = con.prepareStatement("DELETE FROM results WHERE trainer_id = ?");
        statement.setInt(1, id);
        statement.execute();
    }

}
