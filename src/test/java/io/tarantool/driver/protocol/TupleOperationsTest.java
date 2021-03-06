package io.tarantool.driver.protocol;

import io.tarantool.driver.exceptions.TarantoolSpaceOperationException;
import io.tarantool.driver.protocol.operations.TarantoolUpdateOperationType;
import io.tarantool.driver.protocol.operations.TupleOperation;
import io.tarantool.driver.protocol.operations.TupleOperations;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TupleOperationsTest {

    @Test
    public void createInvalidOperationsList() {
        //double update of the same field by field number
        assertThrows(TarantoolSpaceOperationException.class,
                () -> TupleOperations.add(1, 10).andSubtract(1, 5));
        //double update of the same field by field name
        assertThrows(TarantoolSpaceOperationException.class,
                () -> TupleOperations.bitwiseOr("field_name", 10).andBitwiseAnd("field_name", 5));
    }

    @Test
    public void createOperationsList() {
        TupleOperations operations = TupleOperations
                .bitwiseXor(1, 5)
                .andBitwiseXor("field_1", 44)
                .andBitwiseAnd(2, 15)
                .andBitwiseAnd("field_2", 45)
                .andBitwiseOr(3, 10)
                .andBitwiseOr("field_3", 45)
                .andBitwiseXor(8, 12)
                .andBitwiseXor("field_4", 128)
                .andAdd(10, 10)
                .andAdd("field_5", 10)
                .andSet(11, 11)
                .andSet("field_6", "asdf")
                .andSubtract(15, 100)
                .andSubtract("field_7", 99)
                .andInsert(20, "kjlkj")
                .andInsert("field_8", "nhshs")
                ;

        assertEquals(16, operations.asList().size());

        operations.andSplice(22, 5, 1, "ndnd");
        operations.andSplice("field_9", 6, 10, "ndnd");

        assertEquals(18, operations.asList().size());

        List<TarantoolUpdateOperationType> expectedOperationTypes = Arrays.asList(
                TarantoolUpdateOperationType.BITWISEXOR, TarantoolUpdateOperationType.BITWISEXOR,
                TarantoolUpdateOperationType.BITWISEAND, TarantoolUpdateOperationType.BITWISEAND,
                TarantoolUpdateOperationType.BITWISEOR, TarantoolUpdateOperationType.BITWISEOR,
                TarantoolUpdateOperationType.BITWISEXOR, TarantoolUpdateOperationType.BITWISEXOR,
                TarantoolUpdateOperationType.ADD, TarantoolUpdateOperationType.ADD,
                TarantoolUpdateOperationType.SET, TarantoolUpdateOperationType.SET,
                TarantoolUpdateOperationType.SUBTRACT, TarantoolUpdateOperationType.SUBTRACT,
                TarantoolUpdateOperationType.INSERT, TarantoolUpdateOperationType.INSERT,
                TarantoolUpdateOperationType.SPLICE, TarantoolUpdateOperationType.SPLICE);

        List<TarantoolUpdateOperationType> actualOperationTypes = operations.asList().stream()
                .map(TupleOperation::getOperationType).collect(Collectors.toList());

        assertEquals(expectedOperationTypes, actualOperationTypes);
    }

    @Test
    public void convertIndexToPositionNumberOperationsList() {
        TupleOperations operations = TupleOperations
                .bitwiseXor(8, 5)
                .andBitwiseAnd(2, 15);

        assertEquals(2, operations.asList().size());

        List<Integer> fieldIndexes = operations.asList().stream().map(TupleOperation::getFieldIndex)
                .collect(Collectors.toList());

        assertEquals(fieldIndexes, Arrays.asList(8, 2));

        List<Integer> fieldNumbers = operations.asProxyOperationList().stream().map(TupleOperation::getFieldIndex)
                .collect(Collectors.toList());

        assertEquals(fieldNumbers, Arrays.asList(9, 3));
    }
}
