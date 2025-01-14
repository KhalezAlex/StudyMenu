package org.klozevitz.enitites.menu;

import javax.persistence.*;
import lombok.*;
import org.klozevitz.enitites.BaseEntity;
import org.klozevitz.enitites.appUsers.Department;

import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category_t")
public class Category extends BaseEntity {
    private String name;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
    private Set<Item> list;
    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}
