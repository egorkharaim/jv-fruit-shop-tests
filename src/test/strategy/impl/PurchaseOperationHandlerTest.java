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

class PurchaseOperationHandlerTest {
    private FruitDao fruitDao;
    private PurchaseOperationHandler handler;

    @BeforeEach
    void setUp() {
        fruitDao = new FruitDaoImpl();
        handler = new PurchaseOperationHandler(fruitDao);
    }

    @Test
    void handle_enoughFruit_ok() {
        fruitDao.add("apple", 10);
        FruitTransaction transaction = new FruitTransaction(
                FruitTransaction.Operation.PURCHASE, "apple", 4);

        handler.handle(transaction);
        int actualQuantity = fruitDao.getQuantity("apple");
        assertEquals(6, actualQuantity);
    }

    @Test
    void handle_notEnoughFruit_throwsException() {
        fruitDao.add("banana", 5);
        FruitTransaction transaction = new FruitTransaction(
                FruitTransaction.Operation.PURCHASE, "banana", 10);

        assertThrows(RuntimeException.class, () -> handler.handle(transaction));
    }

    @Test
    void handle_fruitNotFound_throwsException() {
        FruitTransaction transaction = new FruitTransaction(
                FruitTransaction.Operation.PURCHASE, "nonExistent", 5);

        assertThrows(RuntimeException.class, () -> handler.handle(transaction));
    }

    @AfterEach
    void tearDown() {
        Storage.clear();
    }
}
