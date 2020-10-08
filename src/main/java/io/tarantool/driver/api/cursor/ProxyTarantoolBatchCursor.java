package io.tarantool.driver.api.cursor;

import io.tarantool.driver.api.TarantoolIndexQuery;
import io.tarantool.driver.api.space.ProxyTarantoolSpace;
import io.tarantool.driver.mappers.TarantoolCallResultMapper;
import io.tarantool.driver.metadata.TarantoolSpaceMetadata;

/**
 * @author Sergey Volgin
 */
public class ProxyTarantoolBatchCursor<T> implements TarantoolCursor<T> {

    private final ProxyTarantoolBatchCursorIterator<T> iterator;

    public ProxyTarantoolBatchCursor(
            ProxyTarantoolSpace space,
            TarantoolIndexQuery indexQuery,
            TarantoolBatchCursorOptions options,
            TarantoolCallResultMapper<T> resultMapper,
            TarantoolSpaceMetadata spaceMetadata
    ) {
        this.iterator = new ProxyTarantoolBatchCursorIterator<>(
                space, indexQuery, options, resultMapper, spaceMetadata);
    }

    @Override
    public TarantoolIterator<T> iterator() {
        return iterator;
    }

    @Override
    public void close() {
    }

    @Override
    public boolean hasNext() {
        return iterator.hasNext();
    }

    @Override
    public T next() {
        return iterator.next();
    }
}
