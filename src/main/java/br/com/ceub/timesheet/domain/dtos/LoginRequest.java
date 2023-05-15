package br.com.ceub.timesheet.domain.dtos;
import javax.validation.constraints.NotEmpty;

public record LoginRequest(@NotEmpty String email, @NotEmpty String password) {
}
