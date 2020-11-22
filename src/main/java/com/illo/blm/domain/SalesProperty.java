package com.illo.blm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;

import com.illo.blm.domain.enumeration.SalesType;

import com.illo.blm.domain.enumeration.SalesStatus;

/**
 * A SalesProperty.
 */
@Entity
@Table(name = "sales_property")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "salesproperty")
public class SalesProperty implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "date_posted")
    private Instant datePosted;

    @Column(name = "expiry_date")
    private Instant expiryDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private SalesType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private SalesStatus status;

    @Column(name = "default_price")
    private Double defaultPrice;

    @OneToOne(mappedBy = "salesProperty")
    @JsonIgnore
    private Property property;

    @ManyToOne
    @JsonIgnoreProperties(value = "salesProperties", allowSetters = true)
    private UserAccount account;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDatePosted() {
        return datePosted;
    }

    public SalesProperty datePosted(Instant datePosted) {
        this.datePosted = datePosted;
        return this;
    }

    public void setDatePosted(Instant datePosted) {
        this.datePosted = datePosted;
    }

    public Instant getExpiryDate() {
        return expiryDate;
    }

    public SalesProperty expiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
        return this;
    }

    public void setExpiryDate(Instant expiryDate) {
        this.expiryDate = expiryDate;
    }

    public SalesType getType() {
        return type;
    }

    public SalesProperty type(SalesType type) {
        this.type = type;
        return this;
    }

    public void setType(SalesType type) {
        this.type = type;
    }

    public SalesStatus getStatus() {
        return status;
    }

    public SalesProperty status(SalesStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(SalesStatus status) {
        this.status = status;
    }

    public Double getDefaultPrice() {
        return defaultPrice;
    }

    public SalesProperty defaultPrice(Double defaultPrice) {
        this.defaultPrice = defaultPrice;
        return this;
    }

    public void setDefaultPrice(Double defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public Property getProperty() {
        return property;
    }

    public SalesProperty property(Property property) {
        this.property = property;
        return this;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public UserAccount getAccount() {
        return account;
    }

    public SalesProperty account(UserAccount userAccount) {
        this.account = userAccount;
        return this;
    }

    public void setAccount(UserAccount userAccount) {
        this.account = userAccount;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SalesProperty)) {
            return false;
        }
        return id != null && id.equals(((SalesProperty) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SalesProperty{" +
            "id=" + getId() +
            ", datePosted='" + getDatePosted() + "'" +
            ", expiryDate='" + getExpiryDate() + "'" +
            ", type='" + getType() + "'" +
            ", status='" + getStatus() + "'" +
            ", defaultPrice=" + getDefaultPrice() +
            "}";
    }
}
