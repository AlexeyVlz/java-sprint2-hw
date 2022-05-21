package model;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Objects;

public abstract class Records {
    protected String title;
    protected Status status;
    protected int id;
    protected String specification;
    protected Duration duration;
    protected ZonedDateTime startTime;
    protected ZonedDateTime endTime;

    public Records(String title, Status status, int id, String specification, ZonedDateTime startTime,
                   Duration duration) {
        this.title = title;
        this.status = status;
        this.id = id;
        this.specification = specification;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = getEndTime();

    }

    public Records(String title, int id, String specification) {
        this.title = title;
        this.id = id;
        this.specification = specification;
    }

    public String getSpecification() {
        return specification;
    }

    public Duration getDuration() {
        return duration;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public ZonedDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public void setEndTime () {
        this.endTime = getEndTime();
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
