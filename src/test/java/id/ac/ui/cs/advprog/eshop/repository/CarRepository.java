package id.ac.ui.cs.advprog.eshop.repository;

import id.ac.ui.cs.advprog.eshop.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Iterator;
import static org.junit.jupiter.api.Assertions.*;

class CarRepositoryTest {

    CarRepository carRepository;

    @BeforeEach
    void setUp() {
        carRepository = new CarRepository();
    }

    @Test
    void testCreateAndFind() {
        Car car = new Car();
        car.setCarId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        car.setCarName("Toyota Avanza");
        car.setCarColor("Black");
        car.setCarQuantity(10);

        carRepository.create(car);

        Car savedCar = carRepository.findById(car.getCarId());
        assertNotNull(savedCar);
        assertEquals(car.getCarId(), savedCar.getCarId());
        assertEquals(car.getCarName(), savedCar.getCarName());
    }

    @Test
    void testCreateWithoutIdShouldGenerateUuid() {
        Car car = new Car();
        car.setCarName("Honda Civic");

        carRepository.create(car);

        assertNotNull(car.getCarId());
        assertFalse(car.getCarId().isEmpty());
    }

    @Test
    void testFindAll() {
        Car car1 = new Car();
        car1.setCarId("id-1");
        carRepository.create(car1);

        Car car2 = new Car();
        car2.setCarId("id-2");
        carRepository.create(car2);

        Iterator<Car> carIterator = carRepository.findAll();
        assertTrue(carIterator.hasNext());

        int count = 0;
        while (carIterator.hasNext()) {
            carIterator.next();
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    void testFindByIdNotFound() {
        Car findResult = carRepository.findById("non-existent-id");
        assertNull(findResult);
    }

    @Test
    void testEditSuccess() {
        Car car = new Car();
        car.setCarId("id-1");
        car.setCarName("Tesla Model 3");
        carRepository.create(car);

        Car updatedCar = new Car();
        updatedCar.setCarId("id-1");
        updatedCar.setCarName("Tesla Model X");
        updatedCar.setCarColor("White");

        carRepository.edit(updatedCar);

        Car result = carRepository.findById("id-1");
        assertEquals("Tesla Model X", result.getCarName());
        assertEquals("White", result.getCarColor());
    }

    @Test
    void testEditFailedIfIdNotFound() {
        Car updatedCar = new Car();
        updatedCar.setCarId("missing-id");

        Car result = carRepository.edit(updatedCar);
        assertNull(result);
    }

    @Test
    void testDelete() {
        Car car = new Car();
        car.setCarId("id-delete");
        carRepository.create(car);

        carRepository.delete("id-delete");

        Car findResult = carRepository.findById("id-delete");
        assertNull(findResult);
    }

    @Test
    void testFindByIdIfIdNotFoundAfterIteratingAllData() {
        Car car1 = new Car();
        car1.setCarId("id-1");
        carRepository.create(car1);

        Car car2 = new Car();
        car2.setCarId("id-2");
        carRepository.create(car2);

        Car result = carRepository.findById("id-999");

        assertNull(result);
    }

    @Test
    void testEditIfIdNotFound() {
        Car car1 = new Car();
        car1.setCarId("id-existing");
        carRepository.create(car1);

        Car nonExistentCar = new Car();
        nonExistentCar.setCarId("id-not-found");
        nonExistentCar.setCarName("Invisible Car");

        Car result = carRepository.edit(nonExistentCar);

        assertNull(result);
    }
}