package com.troian.bannerservice.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@Table(name = "banner",
        uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class Banner implements Comparable<Banner>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank
    private String name;
    @NotBlank
    private String text;
    @NotNull
    private float price;

    @ManyToMany(cascade = {CascadeType.PERSIST,
            CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JoinTable(
            name = "category_banner",
            joinColumns = @JoinColumn(name = "banner_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories;
    private boolean isActive;

    public Banner(String name, String text, float price, boolean isActive) {
        this.name = name;
        this.text = text;
        this.price = price;
        this.isActive = isActive;
    }

    public void addCategoryToBanner(Category category) {
        if(categories == null) {
            categories = new ArrayList<>();
        }
        categories.add(category);
        category.getBanners().add(this);
    }

    @Override
    public int compareTo(Banner o) {
        Float f1 = this.price;
        Float f2 = o.price;
        return f2.compareTo(f1);
    }
}
