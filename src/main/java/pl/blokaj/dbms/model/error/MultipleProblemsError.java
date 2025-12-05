package pl.blokaj.dbms.model.error;

import jakarta.annotation.Nonnull;

import java.util.List;

/**
 * Error containing multiple problems about request processing.
 * Useful when processing complex requests where multiple problems can occur at the same time.
 */
public class MultipleProblemsError {

    /** List of individual problems */
    public List<Problem> problems;

    // Default constructor
    public MultipleProblemsError() {}

    // Convenience constructor
    public MultipleProblemsError(List<Problem> problems) {
        this.problems = problems;
    }

    public List<Problem> getProblems() { return problems; }
    public void setProblems(List<Problem> problems) { this.problems = problems; }

    /**
     * Inner class representing a single problem
     */
    public static class Problem {

        /** Description of particular problem */
        public String error;

        /** Optional context for troubleshooting (e.g., which column caused the problem) */
        public String context;

        public Problem() {}

        public Problem(@Nonnull String error, String context) {
            this.error = error;
            this.context = context;
        }

        public String getError() { return error; }
        public void setError(String error) { this.error = error; }

        public String getContext() { return context; }
        public void setContext(String context) { this.context = context; }
    }
}