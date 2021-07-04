package de.erdesignerng.visual.editor;

import java.awt.Component;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.plaf.DimensionUIResource;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;

import org.apache.log4j.Logger;

import de.erdesignerng.ERDesignerBundle;
import de.erdesignerng.util.ApplicationPreferences;
import de.mogwai.common.client.looks.UIInitializer;

public class SQLAIEditor extends BaseEditor {

    private final Logger LOGGER = Logger.getLogger(SQLAIEditor.class);

    private String aiPrompt;

    private JLabel tokenLabel;
    private JTextArea promptArea;
    private JLabel promptLabel;
    private JTextField tokenField;
    private JTextArea inputArea;
    private JLabel inputLabel;
    private JLabel generatedLabel;
    private JTextArea generatedArea;
    private JButton closeButton;
    private JButton executeButton;

    public SQLAIEditor(Component aParent, String aiPrompt) {
        super(aParent, ERDesignerBundle.SQLAIWINDOW);

        this.aiPrompt = aiPrompt;

        this.initialize();
    }

    private void initialize() {
        tokenLabel = new JLabel("OpenAI GPT-3 Token");
        promptArea = new JTextArea(5, 5);
        promptLabel = new JLabel("Prompt");
        tokenField = new JTextField(5);
        inputArea = new JTextArea(5, 5);
        inputLabel = new JLabel("Input");
        generatedLabel = new JLabel("Generated Query");
        generatedArea = new JTextArea(5, 5);
        closeButton = new JButton(new AbstractAction("Close") {

            @Override
            public void actionPerformed(ActionEvent e) {
                commandClose();
            }

        });
        executeButton = new JButton(new AbstractAction("Execute") {

            @Override
            public void actionPerformed(ActionEvent e) {
                executeButton.setEnabled(false);
                executeButton.setText("Loading...");

                LOGGER.info(inputArea.getText());
                LOGGER.info(tokenField.getText());

                try {
                    OpenAiService service = new OpenAiService(tokenField.getText());
                    CompletionRequest completionRequest = CompletionRequest.builder()
                            .prompt(aiPrompt.trim() + "\nInput: " + inputArea.getText().trim() + "\nOutput:")
                            .echo(false).bestOf(1).maxTokens(32).build();
                    CompletionChoice choice = service.createCompletion("davinci", completionRequest).getChoices()
                            .get(0);

                    generatedArea.setText(choice.getText());

                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(choice.getText()),
                            null);
                } catch (Exception err) {
                    LOGGER.error(err);

                    generatedArea.setText("Error has occured, please check and try again");
                }

                executeButton.setEnabled(true);
                executeButton.setText("Execute");
            }

        });

        // set components properties
        promptArea.setText(this.aiPrompt);
        promptArea.setEnabled(false);
        generatedArea.setEnabled(false);

        // adjust size and set layout
        setPreferredSize(new DimensionUIResource(589, 664));
        setLayout(null);

        // add components
        add(tokenLabel);
        add(promptArea);
        add(promptLabel);
        add(tokenField);
        add(inputArea);
        add(inputLabel);
        add(generatedLabel);
        add(generatedArea);
        add(closeButton);
        add(executeButton);

        // set component bounds (only needed by Absolute Positioning)
        tokenLabel.setBounds(25, 25, 185, 25);
        promptArea.setBounds(25, 115, 540, 220);
        promptLabel.setBounds(25, 90, 100, 25);
        tokenField.setBounds(25, 50, 540, 30);
        inputArea.setBounds(25, 370, 540, 30);
        inputLabel.setBounds(25, 345, 100, 25);
        generatedLabel.setBounds(25, 420, 100, 25);
        generatedArea.setBounds(25, 445, 540, 65);
        closeButton.setBounds(30, 550, 105, 50);
        executeButton.setBounds(155, 550, 110, 50);

        pack();

        setMinimumSize(new DimensionUIResource(300, 300));
        ApplicationPreferences.getInstance().setWindowSize(getClass().getSimpleName(), this);

        UIInitializer.getInstance().initialize(this);
    }

    private void commandClose() {
        ApplicationPreferences.getInstance().updateWindowSize(getClass().getSimpleName(), this);
        setModalResult(DialogConstants.MODAL_RESULT_OK);
    }

    @Override
    public void applyValues() throws Exception {
        return;
    }

}
