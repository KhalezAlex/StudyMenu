package org.klozevitz.enitites.appUsers;

import javax.persistence.*;
import lombok.*;
import org.klozevitz.enitites.BaseEntity;
import org.klozevitz.enitites.appUsers.enums.states.EmployeeState;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "employee_t")
public class Employee extends BaseEntity {
    @Enumerated(EnumType.STRING)
    private EmployeeState state;

    @OneToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}
