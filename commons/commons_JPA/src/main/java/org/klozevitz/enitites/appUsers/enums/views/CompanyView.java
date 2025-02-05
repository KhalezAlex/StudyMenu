package org.klozevitz.enitites.appUsers.enums.views;

import lombok.RequiredArgsConstructor;
import org.klozevitz.enitites.appUsers.enums.states.CompanyState;

import java.util.ArrayList;
import java.util.List;

import static org.klozevitz.enitites.appUsers.enums.states.CompanyState.*;

@RequiredArgsConstructor
public enum CompanyView {
    // TODO проверить
    NULL_COMPANY_STATE_NOTIFICATION_VIEW("", new ArrayList<>()),
    UNREGISTERED_WELCOME_VIEW("/start", List.of(UNREGISTERED_STATE)),
    EMAIL_REQUEST_VIEW("/register", List.of(UNREGISTERED_STATE, WAITING_FOR_EMAIL_STATE)),
    EMAIL_CONFIRMATION_REQUEST_VIEW("", List.of(BASIC_STATE)),
    // TODO проверить
    EMAIL_CONFIRMATION_NOTIFICATION_VIEW("", List.of(WAITING_FOR_EMAIL_CONFIRMATION_STATE)),
    REGISTERED_WELCOME_VIEW("/start", List.of(BASIC_STATE)),
    DEPARTMENTS_MANAGEMENT_VIEW("/departments_management", List.of(BASIC_STATE)),
    DEPARTMENT_TELEGRAM_USER_ID_REQUEST_VIEW("/add_department", List.of(BASIC_STATE)),
    NEW_DEPARTMENT_REGISTRATION_NOTIFICATION_VIEW("", List.of(BASIC_STATE));

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
