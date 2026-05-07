public abstract class Entity {
    private String id;
    private long createdTime;

    public Entity(String id) {
        this.id = id;
        this.createdTime = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * Abstract method - To be implemented by subclasses
     * @return String representation of the entity
     */
    @Override
    public abstract String toString();
}
