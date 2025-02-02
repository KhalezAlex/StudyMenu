package org.klozevitz.enitites.appUsers;

import javax.persistence.*;
import lombok.*;
import org.klozevitz.enitites.BaseEntity;
import org.klozevitz.enitites.appUsers.enums.states.DepartmentState;
import org.klozevitz.enitites.menu.Category;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "department_t")
public class Department extends BaseEntity {
    @Enumerated(EnumType.STRING)
    private DepartmentState state;

    @OneToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "department")
    private Set<Employee> employees;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "department")
    private Set<Category> menu;
}
