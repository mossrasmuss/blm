package com.illo.blm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Category.
 */
@Entity
@Table(name = "category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "category")
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "categor_name")
    private String categorName;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "category")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Attribute> attributes = new HashSet<>();

    @OneToMany(mappedBy = "category")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Property> properties = new HashSet<>();

    @OneToMany(mappedBy = "children")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Category> parents = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "parents", allowSetters = true)
    private Category children;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategorName() {
        return categorName;
    }

    public Category categorName(String categorName) {
        this.categorName = categorName;
        return this;
    }

    public void setCategorName(String categorName) {
        this.categorName = categorName;
    }

    public String getDescription() {
        return description;
    }

    public Category description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Attribute> getAttributes() {
        return attributes;
    }

    public Category attributes(Set<Attribute> attributes) {
        this.attributes = attributes;
        return this;
    }

    public Category addAttribute(Attribute attribute) {
        this.attributes.add(attribute);
        attribute.setCategory(this);
        return this;
    }

    public Category removeAttribute(Attribute attribute) {
        this.attributes.remove(attribute);
        attribute.setCategory(null);
        return this;
    }

    public void setAttributes(Set<Attribute> attributes) {
        this.attributes = attributes;
    }

    public Set<Property> getProperties() {
        return properties;
    }

    public Category properties(Set<Property> properties) {
        this.properties = properties;
        return this;
    }

    public Category addProperty(Property property) {
        this.properties.add(property);
        property.setCategory(this);
        return this;
    }

    public Category removeProperty(Property property) {
        this.properties.remove(property);
        property.setCategory(null);
        return this;
    }

    public void setProperties(Set<Property> properties) {
        this.properties = properties;
    }

    public Set<Category> getParents() {
        return parents;
    }

    public Category parents(Set<Category> categories) {
        this.parents = categories;
        return this;
    }

    public Category addParent(Category category) {
        this.parents.add(category);
        category.setChildren(this);
        return this;
    }

    public Category removeParent(Category category) {
        this.parents.remove(category);
        category.setChildren(null);
        return this;
    }

    public void setParents(Set<Category> categories) {
        this.parents = categories;
    }

    public Category getChildren() {
        return children;
    }

    public Category children(Category category) {
        this.children = category;
        return this;
    }

    public void setChildren(Category category) {
        this.children = category;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Category)) {
            return false;
        }
        return id != null && id.equals(((Category) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Category{" +
            "id=" + getId() +
            ", categorName='" + getCategorName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
