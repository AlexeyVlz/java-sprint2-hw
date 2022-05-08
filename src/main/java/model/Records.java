package model;

import java.util.Objects;

public abstract class Records {
    protected String title;
    protected Status status;
    protected int id;

    public Records(String title, Status status, int id) {
        this.title = title;
        this.status = status;
        this.id = id;
    }

    public Records(String title, int id) {
        this.title = title;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Status getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Records records = (Records) o;
        return id == records.id && Objects.equals(title, records.title) && status == records.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, status, id);
    }
}
