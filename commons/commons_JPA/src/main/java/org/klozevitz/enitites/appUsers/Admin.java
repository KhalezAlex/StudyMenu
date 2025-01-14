package org.klozevitz.enitites.appUsers;

import javax.persistence.*;
import lombok.*;
import org.klozevitz.enitites.BaseEntity;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "admin_t")
public class Admin extends BaseEntity {
    @OneToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;
}
