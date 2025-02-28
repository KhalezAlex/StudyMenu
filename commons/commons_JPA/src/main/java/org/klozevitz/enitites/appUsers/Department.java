package org.klozevitz.enitites.appUsers;

import javax.persistence.*;
import lombok.*;
import org.klozevitz.enitites.BaseEntity;
import org.klozevitz.enitites.appUsers.enums.states.DepartmentState;
import org.klozevitz.enitites.appUsers.enums.views.DepartmentView;
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
    @Enumerated(EnumType.STRING)
    private DepartmentView currentView;

    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "department", cascade = CascadeType.ALL)
//    @JoinColumn(name = "app_user_id", insertable = false)
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "company_id", insertable = false)
    @JoinColumn(name = "company_id")
    private Company company;
    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "department", fetch = FetchType.EAGER)
    private Set<Employee> employees;
    @EqualsAndHashCode.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "department", fetch = FetchType.EAGER)
    private Set<Category> menu;

    // TODO:
}
