package hac;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

//NOGA : https://blog.felix-seifert.com/limit-and-offset-spring-data-jpa-repositories/
public class OffsetBasedPageRequest implements Pageable {
    private final int offset;
    private final int limit;

    // Constructor could be expanded if sorting is needed
    private Sort sort = null;

    public OffsetBasedPageRequest(int limit, int offset) {
        if (limit < 1) {
            throw new IllegalArgumentException("Limit must not be less than one!");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("Offset index must not be less than zero!");
        }
        this.sort = sort.by(Sort.Direction.ASC, "id");
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    public int getPageNumber() { return offset/limit; }

    @Override
    public int getPageSize() { return limit; }

    @Override
    public long getOffset() { return offset; }

    @Override
    public Sort getSort() { return this.sort; }

    @Override
    public Pageable next() {
        return new OffsetBasedPageRequest(getPageSize(), (int) (getOffset() + getPageSize()));
    }

    public Pageable previous() {
        // The integers are positive. Subtracting does not let them become bigger than integer.
        return hasPrevious() ?
                new OffsetBasedPageRequest(getPageSize(), (int) (getOffset() - getPageSize())): this;
    }

    @Override
    public Pageable previousOrFirst() {
        return hasPrevious() ? previous() : first();
    }

    @Override
    public Pageable first() {
        return new OffsetBasedPageRequest(getPageSize(), 0);
    }

    @Override
    public Pageable withPage(int pageNumber) { return null; }

    @Override
    public boolean hasPrevious() {
        return offset > limit;
    }
}
