package com.illo.blm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.FieldType;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A UserAccount.
 */
@Entity
@Table(name = "user_account")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "useraccount")
public class UserAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "usrname")
    private String usrname;

    @Column(name = "p_wd")
    private String pWd;

    @Column(name = "date_created")
    private Instant dateCreated;

    @OneToMany(mappedBy = "account")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<SalesProperty> salesProperties = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "accounts", allowSetters = true)
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsrname() {
        return usrname;
    }

    public UserAccount usrname(String usrname) {
        this.usrname = usrname;
        return this;
    }

    public void setUsrname(String usrname) {
        this.usrname = usrname;
    }

    public String getpWd() {
        return pWd;
    }

    public UserAccount pWd(String pWd) {
        this.pWd = pWd;
        return this;
    }

    public void setpWd(String pWd) {
        this.pWd = pWd;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public UserAccount dateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
        return this;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Set<SalesProperty> getSalesProperties() {
        return salesProperties;
    }

    public UserAccount salesProperties(Set<SalesProperty> salesProperties) {
        this.salesProperties = salesProperties;
        return this;
    }

    public UserAccount addSalesProperty(SalesProperty salesProperty) {
        this.salesProperties.add(salesProperty);
        salesProperty.setAccount(this);
        return this;
    }

    public UserAccount removeSalesProperty(SalesProperty salesProperty) {
        this.salesProperties.remove(salesProperty);
        salesProperty.setAccount(null);
        return this;
    }

    public void setSalesProperties(Set<SalesProperty> salesProperties) {
        this.salesProperties = salesProperties;
    }

    public Customer getCustomer() {
        return customer;
    }

    public UserAccount customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserAccount)) {
            return false;
        }
        return id != null && id.equals(((UserAccount) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserAccount{" +
            "id=" + getId() +
            ", usrname='" + getUsrname() + "'" +
            ", pWd='" + getpWd() + "'" +
            ", dateCreated='" + getDateCreated() + "'" +
            "}";
    }
}
