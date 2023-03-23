package com.troian.bannerservice.model.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Table(name = "record")
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "ip_adress")
    @NotNull
    private String ip;

    @Column(name = "user_agent")
    @NotNull
    private String agent;

    @Column(name = "date")
    @NotNull
    private LocalDateTime date;

    @Column(name = "banner_id")
    private long bannerId;

    @Column(name = "category_ids")
    @ElementCollection
    private Set<Long> categoryIds;

    private float price;
    @Column(name = "no_content_reason")
    private String noContentReason;

    public Set<Long> getCategoryIds() {
        if(categoryIds == null)
            categoryIds = new HashSet<>();
        return categoryIds;
    }
}
