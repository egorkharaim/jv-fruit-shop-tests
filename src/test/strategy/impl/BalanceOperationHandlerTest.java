package core.basesyntax.strategy.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import core.basesyntax.dao.FruitDao;
import core.basesyntax.dao.FruitDaoImpl;
import core.basesyntax.db.Storage;
import core.basesyntax.model.FruitTransaction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BalanceOperationHandlerTest {
    private FruitDao fruitDao;
    private BalanceOperationHandler handler;

    @BeforeEach
    void setUp() {
        fruitDao = new FruitDaoImpl();
        handler = new BalanceOperationHandler(fruitDao);
    }

    @AfterEach
    void tearDown() {
        Storage.fruits.clear();
    }

    @Test
    void handle_validBalance_ok() {
        FruitTransaction transaction = new FruitTransaction(
                FruitTransaction.Operation.BALANCE, "apple", 50);
        handler.handle(transaction);
        assertEquals(50, fruitDao.getQuantity("apple"));
    }

    @Test
    void handle_negativeQuantity_notOk() {
        FruitTransaction transaction = new FruitTransaction(
                FruitTransaction.Operation.BALANCE, "apple", -10);
        assertThrows(RuntimeException.class, () -> handler.handle(transaction));
    }
}
