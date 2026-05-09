package com.sectioncomment;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class SectionCommentConfigurable implements Configurable {

    private final Project project;
    private JSpinner lineLengthSpinner;

    public SectionCommentConfigurable(Project project) {
        this.project = project;
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Section Comments";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(4, 4, 4, 4);

        // Label
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Right margin / line length:"), gbc);

        // Spinner
        SectionCommentSettings settings = SectionCommentSettings.getInstance(project);
        int current = settings != null ? settings.getLineLength() : 120;
        lineLengthSpinner = new JSpinner(new SpinnerNumberModel(current, 40, 300, 1));

        gbc.gridx = 1;
        panel.add(lineLengthSpinner, gbc);

        // Hint label
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        panel.add(new JLabel("<html><small>Should match Editor -> Code Style -> Hard wrap. IntelliJ Default: 120</small></html>"), gbc);

        // Preview
        gbc.gridy = 2;
        panel.add(new JLabel("Preview:"), gbc);

        JTextArea preview = new JTextArea(2, 80);
        preview.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        preview.setEditable(false);
        preview.setBackground(panel.getBackground());

        gbc.gridy = 3;
        panel.add(preview, gbc);

        // Update preview live
        lineLengthSpinner.addChangeListener(ev -> {
            int len = (Integer) lineLengthSpinner.getValue();
            preview.setText(InsertSectionCommentAction.buildSectionComment("Fields", len, "//", ""));
        });
        // Preview
        preview.setText(InsertSectionCommentAction.buildSectionComment("Fields", current, "//", ""));

        return panel;
    }

    @Override
    public boolean isModified() {
        SectionCommentSettings settings = SectionCommentSettings.getInstance(project);
        if (settings == null) return false;
        return (Integer) lineLengthSpinner.getValue() != settings.getLineLength();
    }

    @Override
    public void apply() {
        SectionCommentSettings settings = SectionCommentSettings.getInstance(project);
        if (settings != null) {
            settings.setLineLength((Integer) lineLengthSpinner.getValue());
        }
    }

    @Override
    public void reset() {
        SectionCommentSettings settings = SectionCommentSettings.getInstance(project);
        if (settings != null) {
            lineLengthSpinner.setValue(settings.getLineLength());
        }
    }
}
