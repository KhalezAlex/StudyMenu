package org.klozevitz.enitites.appUsers.enums.views;

import lombok.RequiredArgsConstructor;
import org.klozevitz.enitites.appUsers.AppUser;
import org.klozevitz.enitites.appUsers.enums.states.CompanyState;

import java.util.List;

import static org.klozevitz.enitites.appUsers.enums.states.CompanyState.*;

@RequiredArgsConstructor
public enum CompanyView {
    UNREGISTERED_WELCOME_VIEW("/start", List.of(UNREGISTERED_STATE)),
    EMAIL_REQUEST_VIEW("/register", List.of(UNREGISTERED_STATE, WAITING_FOR_EMAIL_STATE)),
    EMAIL_CONFIRMATION_REQUEST_VIEW("", List.of(BASIC_STATE)),
    REGISTERED_WELCOME_VIEW("/start", List.of(BASIC_STATE));

    private final String command;
    private final List<CompanyState> permissions;

    public static CompanyView fromTextCommand(String command, CompanyState state) {

        for (CompanyView v: CompanyView.values()) {
            if (v.command.equals(command) && v.permissions.contains(state)) {
                return v;
            }
        }

        return null;
    }
}
