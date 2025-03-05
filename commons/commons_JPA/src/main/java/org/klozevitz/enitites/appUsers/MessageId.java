package org.klozevitz.enitites.appUsers;

import lombok.*;
import org.klozevitz.enitites.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "message_id_t")
public class MessageId extends BaseEntity {
    private Integer messageId;
    @ManyToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;
}
