package org.klozevitz.enitites.menu.resources;

import lombok.*;
import org.klozevitz.enitites.BaseEntity;
import org.klozevitz.enitites.appUsers.Employee;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Builder
@Entity
@Table(name = "test_result_t")
@AllArgsConstructor
@NoArgsConstructor
public class TestResult extends BaseEntity {
    private Integer result;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;
}
