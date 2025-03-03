package org.klozevitz.enitites.appUsers;

import javax.persistence.*;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.klozevitz.enitites.BaseEntity;
import org.klozevitz.enitites.appUsers.enums.states.EmployeeState;
import org.klozevitz.enitites.appUsers.enums.views.EmployeeView;
import org.klozevitz.enitites.menu.resources.WorkBook;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Entity
@Table(name = "employee_t")
public class Employee extends BaseEntity {
    @Enumerated(EnumType.STRING)
    private EmployeeState state;
    @Enumerated(EnumType.STRING)
    private EmployeeView currentView;

//    @OneToOne(mappedBy = "employee")
//    @JoinColumn(name = "app_user_id")
//    private AppUser appUser;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private WorkBook workbook;
    @OneToMany(orphanRemoval = true)
    private Set<MessageId> messages;
}
