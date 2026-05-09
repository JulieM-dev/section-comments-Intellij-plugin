package com.sectioncomment;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

//Persists the setting per project.
@Service(Service.Level.PROJECT)
@State(
    name = "SectionCommentSettings",
    storages = @Storage("sectionComment.xml")
)
public final class SectionCommentSettings implements PersistentStateComponent<SectionCommentSettings.State> {

    public static class State {
        public int lineLength = 120;
    }

    private State myState = new State();

    @Nullable
    public static SectionCommentSettings getInstance(@NotNull Project project) {
        return project.getService(SectionCommentSettings.class);
    }

    @Override
    public @NotNull State getState() {
        return myState;
    }

    @Override
    public void loadState(@NotNull State state) {
        myState = state;
    }

    public int getLineLength() {
        return myState.lineLength;
    }

    public void setLineLength(int lineLength) {
        myState.lineLength = lineLength;
    }
}
