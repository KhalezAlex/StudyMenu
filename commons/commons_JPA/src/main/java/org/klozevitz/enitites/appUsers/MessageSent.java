package org.klozevitz.enitites.appUsers;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.klozevitz.enitites.BaseEntity;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Entity
@Table(name = "message_sent_t")
public class MessageSent extends BaseEntity {
    private Integer messageId;
    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private SendMessage answer;
    @ManyToOne
    @JoinColumn(name = "app_user_id")
    private AppUser appUser;
}
