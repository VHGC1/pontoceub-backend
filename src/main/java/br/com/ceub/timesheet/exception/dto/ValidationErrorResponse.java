package br.com.ceub.timesheet.exception.dto;

import java.util.ArrayList;
import java.util.List;

public class ValidationErrorResponse {
    private List<Violation> violations = new ArrayList<>();

    public List<Violation> getViolations() { return violations; }

    public void setViolations(List<Violation> violations) { this.violations = violations; }
}
