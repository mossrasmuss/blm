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
 * A Property.
 */
@Entity
@Table(name = "property")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "property")
public class Property implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "area")
    private Double area;

    @Column(name = "description")
    private String description;

    @OneToOne
    @JoinColumn(unique = true)
    private SalesProperty salesProperty;

    @OneToMany(mappedBy = "property")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<AttributeValue> attributeValues = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "properties", allowSetters = true)
    private Category category;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getArea() {
        return area;
    }

    public Property area(Double area) {
        this.area = area;
        return this;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public String getDescription() {
        return description;
    }

    public Property description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public SalesProperty getSalesProperty() {
        return salesProperty;
    }

    public Property salesProperty(SalesProperty salesProperty) {
        this.salesProperty = salesProperty;
        return this;
    }

    public void setSalesProperty(SalesProperty salesProperty) {
        this.salesProperty = salesProperty;
    }

    public Set<AttributeValue> getAttributeValues() {
        return attributeValues;
    }

    public Property attributeValues(Set<AttributeValue> attributeValues) {
        this.attributeValues = attributeValues;
        return this;
    }

    public Property addAttributeValue(AttributeValue attributeValue) {
        this.attributeValues.add(attributeValue);
        attributeValue.setProperty(this);
        return this;
    }

    public Property removeAttributeValue(AttributeValue attributeValue) {
        this.attributeValues.remove(attributeValue);
        attributeValue.setProperty(null);
        return this;
    }

    public void setAttributeValues(Set<AttributeValue> attributeValues) {
        this.attributeValues = attributeValues;
    }

    public Category getCategory() {
        return category;
    }

    public Property category(Category category) {
        this.category = category;
        return this;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Property)) {
            return false;
        }
        return id != null && id.equals(((Property) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Property{" +
            "id=" + getId() +
            ", area=" + getArea() +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
