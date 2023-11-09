package model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Company {
    private int id;
    private String name;
    private String description;
    @JsonProperty("isActive")
    private boolean isActive;
    @JsonProperty("deleted_at")
    private Object deletedAt;

    public Company() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Object getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Object deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Company company = (Company) o;
        return id == company.id && isActive == company.isActive && Objects.equals(name, company.name) && Objects.equals(description, company.description) && Objects.equals(deletedAt, company.deletedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, isActive, deletedAt);
    }

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isActive=" + isActive +
                ", deletedAt=" + deletedAt +
                '}';
    }
}