package org.klozevitz.enitites.appUsers;

import javax.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.klozevitz.enitites.BaseEntity;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user_t")
public class AppUser extends BaseEntity {
    private Long telegramUserId;
    @CreationTimestamp
    private LocalDateTime firstLoginDate;
    private String username;

    @OneToOne(cascade = CascadeType.ALL)
    private Admin admin;
    @OneToOne(cascade = CascadeType.ALL)
    private Company company;
    @OneToOne(cascade = CascadeType.ALL)
    private Department department;
    @OneToOne(cascade = CascadeType.ALL)
    private Employee employee;

    public boolean isRegistered() {
        return admin != null ||
                company.getState() != null ||
                department.getState() != null ||
                employee.getState() != null;
    }
}
