package de.erdesignerng.visual.common;

import java.util.Arrays;

import org.apache.log4j.Logger;

import de.erdesignerng.model.Attribute;
import de.erdesignerng.model.Model;
import de.erdesignerng.model.Table;
import de.erdesignerng.visual.editor.SQLAIEditor;

public class GenerateSQLAI extends UICommand {

    private static final Logger LOGGER = Logger.getLogger(GenerateSQLAI.class);

    public GenerateSQLAI() {
    }

    private String createInstruction(Model model) {
        String openAiPrompt = "Instruction: Given an input question, respond with syntactically correct "
                + model.getDialect().getUniqueName() + " when the schema is known to be,\n";
        int i = 1;
        for (final Table table : model.getTables()) {
            openAiPrompt += i + ". ";
            openAiPrompt += "Table \"" + table.getName() + "\" have collumns: ";

            for (Attribute<Table> attribute : table.getAttributes()) {
                openAiPrompt += attribute.getName() + "(" + attribute.getDatatype().getName() + "), ";
            }

            openAiPrompt = openAiPrompt.substring(0, openAiPrompt.length() - 2);
            openAiPrompt += "\n";
            i += 1;
        }

        openAiPrompt += "Be creative but the SQL must be correct.\n\n";
        for (final Table table : model.getTables()) {
            openAiPrompt += "Input: select all available columns from " + table.getName() + " table.\n";
            openAiPrompt += "Output: SELECT "
                    + Arrays.toString(table.getAttributes().toArray()).replace("[", "").replace("]", "") + " FROM "
                    + table.getName() + "\n";
            openAiPrompt += "END\n";
        }

        return openAiPrompt.trim();
    }

    @Override
    public void execute() {
        ERDesignerComponent component = ERDesignerComponent.getDefault();

        Model model = component.getModel();
        String openAiPrompt = createInstruction(model);

        LOGGER.info(openAiPrompt);

        SQLAIEditor editor = new SQLAIEditor(getDetailComponent(), openAiPrompt);
        editor.showModal();
    }

}
