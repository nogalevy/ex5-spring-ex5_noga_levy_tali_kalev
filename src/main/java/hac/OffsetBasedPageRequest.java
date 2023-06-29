package hac;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class OffsetBasedPageRequest implements Pageable {
    private final static String ILLEGAL_LIMIT_ERROR_MSG = "Limit must not be less than one!";
    private final static String ILLEGAL_OFFSET_ERROR_MSG = "Offset index must not be less than zero!";
    private final int offset;
    private final int limit;
    private Sort sort = null;

    /**
     * constructor
     * @param limit int num of elements to get
     * @param offset int num of elements to skip
     */
    public OffsetBasedPageRequest(int limit, int offset) {
        if (limit < 1) {
            throw new IllegalArgumentException(ILLEGAL_LIMIT_ERROR_MSG);
        }
        if (offset < 0) {
            throw new IllegalArgumentException(ILLEGAL_OFFSET_ERROR_MSG);
        }
        this.sort = sort.by(Sort.Direction.ASC, "id");
        this.limit = limit;
        this.offset = offset;
    }

    // getters and setters
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
