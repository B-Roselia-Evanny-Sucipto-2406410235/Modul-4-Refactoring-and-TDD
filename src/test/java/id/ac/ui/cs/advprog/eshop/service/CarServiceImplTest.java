package id.ac.ui.cs.advprog.eshop.service;

import id.ac.ui.cs.advprog.eshop.model.Car;
import id.ac.ui.cs.advprog.eshop.repository.BaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {

    @Mock
    private BaseRepository<Car> carRepository;

    @InjectMocks
    private CarServiceImpl carService;

    private Car car;

    @BeforeEach
    void setUp() {
        car = new Car();
        car.setCarId("eb558e9f-1c39-460e-8860-71af6af63bd6");
        car.setCarName("Toyota Supra");
        car.setCarColor("Red");
        car.setCarQuantity(1);
    }

    @Test
    void testCreate() {
        when(carRepository.create(car)).thenReturn(car);

        Car result = carService.create(car);

        assertNotNull(result);
        assertEquals(car.getCarId(), result.getCarId());
        verify(carRepository, times(1)).create(car);
    }

    @Test
    void testFindAll() {
        List<Car> carList = new ArrayList<>();
        carList.add(car);
        Iterator<Car> carIterator = carList.iterator();

        when(carRepository.findAll()).thenReturn(carIterator);

        List<Car> result = carService.findAll();

        assertEquals(1, result.size());
        assertEquals(car.getCarId(), result.get(0).getCarId());
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void testFindById() {
        when(carRepository.findById(car.getCarId())).thenReturn(car);

        Car result = carService.findById(car.getCarId());

        assertNotNull(result);
        assertEquals(car.getCarId(), result.getCarId());
        verify(carRepository, times(1)).findById(car.getCarId());
    }

    @Test
    void testEdit() {
        carService.edit(car);
        verify(carRepository, times(1)).edit(car);
    }

    @Test
    void testDeleteCarById() {
        String carId = car.getCarId();
        carService.deleteCarById(carId);

        verify(carRepository, times(1)).delete(carId);
    }
}