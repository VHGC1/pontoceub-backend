package br.com.ceub.timesheet.domain.dtos;

import java.util.List;

public record AuthenticatedUserDetails(Long id, String name, List<String> roles) {
}
