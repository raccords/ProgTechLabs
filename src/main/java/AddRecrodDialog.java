import model.Participant;
import model.Result;
import model.Trainer;
import model.Training;
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

public class AddRecrodDialog {
    private final DbUtils dbUtils;
    protected JPanel MainPanel;
    private JButton addRecordButton;
    private JTabbedPane tabbedPane;
    private JPanel trainerPane;
    private JPanel trainingPane;
    private JComboBox participantTrainingComboBox;
    private JComboBox trainerTrainingComboBox;
    private JTextField trainerFNameField;
    private JTextField trainerLNameField;
    private JTextField trainerRankField;
    private JPanel paticipantPane;
    private JTextField participantRank;
    private JTextField participantLName;
    private JTextField participantFName;
    private JDatePickerImpl participantBirthDatePicker;
    private JDatePickerImpl trainerBirthDatePicker;
    private JFormattedTextField weightField;
    private JFormattedTextField heightField;
    private JPanel resultsPane;
    private JComboBox participantResultComboBox;
    private JTextField scoreResultField;
    private JDatePickerImpl resultDatePickerImpl;
    private final ArrayList<Participant> Participants;
    private final ArrayList<Trainer> Trainers;

    private ActionListener closeListener;

    public AddRecrodDialog(DbUtils dbUtil, ActionListener closeListener) {
        this.dbUtils = dbUtil;
        this.closeListener = closeListener;
        addRecordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createRecord();
            }
        });
        addRecordButton.addActionListener(closeListener);
        try {
            Participants = dbUtils.getParticipants();
            Trainers = dbUtils.getTrainers();
            participantTrainingComboBox.setModel(new DefaultComboBoxModel<>(Participants.toArray()));
            participantResultComboBox.setModel(new DefaultComboBoxModel<>(Participants.toArray()));
            trainerTrainingComboBox.setModel(new DefaultComboBoxModel<>(Trainers.toArray()));
        } catch (SQLException e) {
            showMessage(e.getMessage());
            throw new RuntimeException(e);
        }
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

    public void createRecord() {
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

                    dbUtils.insertParticipant(
                            new Participant(
                                    Integer.parseInt(weightField.getText()),
                                    Integer.parseInt(heightField.getText()),
                                    date,
                                    participantFName.getText(),
                                    participantLName.getText(),
                                    participantRank.getText())
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
                    dbUtils.insertTrainer(
                            new Trainer(
                                    date,
                                    participantFName.getText(),
                                    participantLName.getText(),
                                    participantRank.getText()
                            )
                    );
                    break;
                }
                case 2: {
                    var training = new Training(
                            Trainers.get(trainerTrainingComboBox.getSelectedIndex()).pk_trainer,
                            Participants.get(participantTrainingComboBox.getSelectedIndex()).pk_participant);
                    dbUtils.insertTraining(training);
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
                            Participants.get(participantTrainingComboBox.getSelectedIndex()).pk_participant,
                            Integer.parseInt(scoreResultField.getText()),
                            date
                    );
                    dbUtils.insertResult(result);
                    break;
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

