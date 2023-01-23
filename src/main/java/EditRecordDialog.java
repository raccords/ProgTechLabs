import model.Participant;
import model.Result;
import model.Trainer;
import model.Training;
import org.jdatepicker.DateModel;
import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.*;

public class EditRecordDialog {
    private final DbUtils dbUtils;
    private final ArrayList<Participant> Participants;
    private final ArrayList<Trainer> Trainers;
    private final ArrayList<Training> Trainings;
    private final ArrayList<Result> Results;
    protected JPanel MainPanel;
    private JButton editRecordButton;
    private JTabbedPane tabbedPane;
    private JPanel trainerPane;
    private JPanel trainingPane;
    private JComboBox participantTrainingComboBox;
    private JComboBox trainerTrainingComboBox;
    private JTextField trainerFNameField;
    private JTextField trainerLNameField;
    private JTextField trainerRankField;
    private JPanel paticipantPane;
    private JTextField participantRankField;
    private JTextField participantLNameField;
    private JTextField participantFNameField;
    private JDatePickerImpl participantBirthDatePicker;
    private JDatePickerImpl trainerBirthDatePicker;
    private JFormattedTextField weightField;
    private JFormattedTextField heightField;
    private JPanel resultsPane;
    private JComboBox participantResultComboBox;
    private JTextField scoreResultField;
    private JDatePickerImpl resultDatePickerImpl;
    private JButton deleteButton;
    private JComboBox participantParticipantComboBox;
    private JComboBox trainerTrainerCombobox;
    private JComboBox trainingTrainingComboBox;
    private JComboBox resultResultCombobox;

    private ActionListener closeListener;


    public EditRecordDialog(DbUtils dbUtil, ActionListener closeListener) {
        this.dbUtils = dbUtil;
        this.closeListener = closeListener;
        editRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editRecord();
            }
        });
        editRecordButton.addActionListener(closeListener);
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteRecord();
            }
        });
        deleteButton.addActionListener(closeListener);
        try {
            Participants = dbUtils.getParticipants();
            Trainers = dbUtils.getTrainers();
            Trainings = dbUtils.getTrainings();
            Results = dbUtils.getResults();

            participantTrainingComboBox.setModel(new DefaultComboBoxModel<>(Participants.toArray()));
            participantParticipantComboBox.setModel(new DefaultComboBoxModel<>(Participants.toArray()));
            participantResultComboBox.setModel(new DefaultComboBoxModel<>(Participants.toArray()));
            participantParticipantComboBox.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            updateParticipantFields();
                        }
                    }
            );
            trainerTrainingComboBox.setModel(new DefaultComboBoxModel<>(Trainers.toArray()));
            trainerTrainerCombobox.setModel(new DefaultComboBoxModel<>(Trainers.toArray()));
            trainerTrainerCombobox.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            updateTrainerFields();
                        }
                    }
            );

            trainingTrainingComboBox.setModel(new DefaultComboBoxModel<>(Trainings.stream()
                    .map(training ->
                            Trainers.stream().filter(trainer -> trainer.pk_trainer == training.fk_trainer).findFirst().get() + " <--> " +
                                    Participants.stream().filter(participant -> participant.pk_participant == training.fk_participant).findFirst().get()
                    ).toArray()));
            //делаем более красивое представление для внешних ключей
            trainingTrainingComboBox.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            updateTrainingFields();
                        }
                    }
            );


            resultResultCombobox.setModel(new DefaultComboBoxModel<>(Results.stream()
                    .map(result ->
                            Participants.stream().filter(participant -> participant.pk_participant == result.fk_participant).findFirst().get() + "" +
                                    result.date + " " +
                                    result.score + "pts.").
                    toArray()));
            resultResultCombobox.addActionListener(
                    new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            updateResultFields();
                        }
                    }
            );
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        updateTrainerFields();
        updateParticipantFields();
        updateTrainingFields();
        updateResultFields();
    }

    public void createUIComponents() {
        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(0);
        formatter.setMaximum(Integer.MAX_VALUE);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
        UtilDateModel model = new UtilDateModel();
        weightField = new JFormattedTextField(formatter);
        heightField = new JFormattedTextField(formatter);
        participantBirthDatePicker = new JDatePickerImpl(new JDatePanelImpl(model, new Properties()), new DateComponentFormatter());
        trainerBirthDatePicker = new JDatePickerImpl(new JDatePanelImpl(model, new Properties()), new DateComponentFormatter());
        resultDatePickerImpl = new JDatePickerImpl(new JDatePanelImpl(model, new Properties()), new DateComponentFormatter());
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this.MainPanel, message);
    }

    public void updateParticipantFields() {
        var participant = Participants.get(participantParticipantComboBox.getSelectedIndex());
        weightField.setText(Integer.toString(participant.weight));
        heightField.setText(Integer.toString(participant.height));
        DateModel<Date> dateModel = (DateModel<Date>) participantBirthDatePicker.getModel();
        dateModel.setValue(participant.birthDate);
        participantBirthDatePicker.getModel().setSelected(true);
        //JDatePicker может только так
        participantFNameField.setText(participant.f_name);
        participantLNameField.setText(participant.s_name);
        participantRankField.setText(participant.rank);
    }

    public void updateTrainerFields() {
        var trainer = Trainers.get(trainerTrainerCombobox.getSelectedIndex());
        DateModel<Date> dateModel = (DateModel<Date>) trainerBirthDatePicker.getModel();
        dateModel.setValue(trainer.birthDate);
        trainerBirthDatePicker.getModel().setSelected(true);
        //JDatePicker может только так
        trainerFNameField.setText(trainer.f_name);
        trainerLNameField.setText(trainer.s_name);
        trainerRankField.setText(trainer.rank);
    }

    public void updateTrainingFields() {
        var training = Trainings.get(trainingTrainingComboBox.getSelectedIndex());
        trainerTrainingComboBox.setSelectedItem(
                Trainers.stream().filter(trainer -> trainer.pk_trainer == training.fk_trainer).findFirst().get());
        participantTrainingComboBox.setSelectedItem(
                Participants.stream().filter(participant -> participant.pk_participant == training.fk_participant).findFirst().get());
    }

    public void updateResultFields() {
        var result = Results.get(resultResultCombobox.getSelectedIndex());
        participantResultComboBox.setSelectedItem(
                Participants.stream().filter(participant -> participant.pk_participant == result.fk_participant).findFirst().get());
        DateModel<Date> dateModel = (DateModel<Date>) resultDatePickerImpl.getModel();
        dateModel.setValue(result.date);
        resultDatePickerImpl.getModel().setSelected(true);
        scoreResultField.setText(Integer.toString(result.score));
        //JDatePicker может только так
    }

    public void editRecord() {
        try {
            switch (tabbedPane.getSelectedIndex()) {
                case 0: {
                    if (Arrays.stream(paticipantPane.getComponents())
                            .filter(c -> c instanceof JTextField)
                            .map(c -> ((JTextField) c).getText()).anyMatch(f -> f.isBlank()) ||
                            participantBirthDatePicker.getModel().getValue() == null) {
                        showMessage("Bad data, check fields");
                        return;
                    }
                    var date = new java.sql.Date(((Date) participantBirthDatePicker.getModel().getValue()).getTime());

                    dbUtils.updateParticipant(
                            new Participant(
                                    Integer.parseInt(weightField.getText()),
                                    Integer.parseInt(heightField.getText()),
                                    date,
                                    participantFNameField.getText(),
                                    participantLNameField.getText(),
                                    participantRankField.getText(),
                                    Participants.get(participantParticipantComboBox.getSelectedIndex()).pk_participant)
                    );
                    break;
                }
                case 1: {
                    if (Arrays.stream(trainerPane.getComponents())
                            .filter(c -> c instanceof JTextField)
                            .map(c -> ((JTextField) c).getText()).anyMatch(f -> f.isBlank()) ||
                            trainerBirthDatePicker.getModel().getValue() == null) {
                        showMessage("Bad data, check fields");
                        return;
                    }

                    var date = new java.sql.Date(((Date) trainerBirthDatePicker.getModel().getValue()).getTime());
                    dbUtils.updateTrainer(
                            new Trainer(
                                    date,
                                    participantFNameField.getText(),
                                    participantLNameField.getText(),
                                    participantRankField.getText(),
                                    Trainers.get(participantParticipantComboBox.getSelectedIndex()).pk_trainer)
                    );
                    break;
                }
                case 2: {
                    var training = new Training(
                            Trainings.get(trainingTrainingComboBox.getSelectedIndex()).pk_training,
                            Trainers.get(trainerTrainingComboBox.getSelectedIndex()).pk_trainer,
                            Participants.get(participantTrainingComboBox.getSelectedIndex()).pk_participant);
                    dbUtils.updateTraining(training);
                    break;
                }
                case 3: {
                    if (Arrays.stream(resultsPane.getComponents())
                            .filter(c -> c instanceof JTextField)
                            .map(c -> ((JTextField) c).getText()).anyMatch(f -> f.isBlank()) ||
                            resultDatePickerImpl.getModel().getValue() == null) {
                        showMessage("Bad data, check fields");
                        return;
                    }
                    var date = new java.sql.Date(((Date) participantBirthDatePicker.getModel().getValue()).getTime());
                    var result = new Result(
                            Results.get(resultResultCombobox.getSelectedIndex()).pk_result,
                            Participants.get(participantResultComboBox.getSelectedIndex()).pk_participant,
                            Integer.parseInt(scoreResultField.getText()),
                            date
                    );
                    dbUtils.updateResult(result);
                    break;
                }
            }
        } catch (SQLException e) {
            showMessage(e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public void deleteRecord() {
        try {
            switch (tabbedPane.getSelectedIndex()) {
                case 0: {
                    dbUtils.deleteParticipant(
                            Participants.get(participantParticipantComboBox.getSelectedIndex()).pk_participant
                    );
                    break;
                }
                case 1: {
                    dbUtils.deleteTrainer(
                            Trainers.get(participantParticipantComboBox.getSelectedIndex()).pk_trainer
                    );
                    break;
                }
                case 2: {

                    dbUtils.deleteTraining(
                            Participants.get(participantTrainingComboBox.getSelectedIndex()).pk_participant
                    );
                    break;
                }
                case 3: {
                    dbUtils.deleteResult(
                            Results.get(resultResultCombobox.getSelectedIndex()).pk_result
                    );
                    break;
                }
            }
        } catch (SQLException e) {
            showMessage(e.getMessage());
            throw new RuntimeException(e);
        }

    }
}

