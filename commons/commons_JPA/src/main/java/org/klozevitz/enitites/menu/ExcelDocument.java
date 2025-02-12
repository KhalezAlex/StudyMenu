package org.klozevitz.enitites.menu;

import lombok.*;
import org.klozevitz.enitites.BaseEntity;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(callSuper = false)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "excel_document_t")
public class ExcelDocument extends BaseEntity {
    private String telegramFileId;
    private String name;
    private String mimeType;
    private Long fileSize;
    private byte[] asByteArray;
    @EqualsAndHashCode.Exclude
    @OneToOne(mappedBy = "excelDoc", cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", insertable = false)
    private Category category;
}
