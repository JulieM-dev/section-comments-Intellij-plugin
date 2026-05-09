package com.sectioncomment;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import org.jetbrains.annotations.NotNull;

public class InsertSectionCommentAction extends AnAction {

    //Configurable in plugin.xml
    private static final int DEFAULT_LINE_LENGTH = 120;


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        Editor editor = e.getData(CommonDataKeys.EDITOR);

        if (project == null || editor == null) return;

        String label = Messages.showInputDialog(
            project,
            "Section name:",
            "Insert Section Comment",
            Messages.getQuestionIcon()
        );

        if (label == null || label.isBlank()) return;

        // Get settings line length
        SectionCommentSettings settings = SectionCommentSettings.getInstance(project);
        int lineLength = settings != null ? settings.getLineLength() : DEFAULT_LINE_LENGTH;

        // Detect comment style from current file
        String fileName = "";
        if (e.getData(CommonDataKeys.VIRTUAL_FILE) != null) {
            fileName = e.getData(CommonDataKeys.VIRTUAL_FILE).getName();
        }
        String[] commentTokens = getCommentTokens(fileName);
        String commentStart = commentTokens[0];
        String commentEnd = commentTokens[1];

        // Build the comment
        String comment = buildSectionComment(label.trim(), lineLength, commentStart, commentEnd);

        // Insert
        Document document = editor.getDocument();
        int offset = editor.getCaretModel().getOffset();
        int lineStart = document.getLineStartOffset(document.getLineNumber(offset));

        WriteCommandAction.runWriteCommandAction(project, () -> {
            document.insertString(lineStart, comment + "\n");
        });
    }

    static String buildSectionComment(String label, int lineLength, String commentStart, String commentEnd) {

        // Format: <start> ─── <label> ─── ... <end>
        String prefix = commentStart + " ─── " + label + " ";
        String endPart = commentEnd.isEmpty() ? "" : " " + commentEnd;

        int fillLength = lineLength - prefix.length() - endPart.length();
        if (fillLength < 1) fillLength = 1;

        String fill = "─".repeat(fillLength);

        return prefix + fill + endPart;
    }

    private String[] getCommentTokens(String fileName) {
        String ext = fileName.contains(".") ? fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase() : "";
        return switch (ext) {
            case "java", "kt", "js", "ts", "jsx", "tsx", "c", "cpp", "cs", "go", "swift" -> new String[]{"//", ""};
            case "py", "rb", "sh", "yml", "yaml" -> new String[]{"#", ""};
            case "html", "xml" -> new String[]{"<!--", "-->"};
            case "css", "scss", "less" -> new String[]{"/*", "*/"};
            case "lua" -> new String[]{"--", ""};
            default -> new String[]{"//", ""};
        };
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        Editor editor = e.getData(CommonDataKeys.EDITOR);
        e.getPresentation().setEnabledAndVisible(editor != null);
    }
}
