package com.example.citronix;

import com.example.citronix.domain.dtos.request.farm.CreateFarmRequestDto;
import com.example.citronix.domain.dtos.request.farm.FarmSearchCriteriaDto;
import com.example.citronix.domain.dtos.request.field.FramWithFieldRequestdTO;
import com.example.citronix.domain.entity.Farm;
import com.example.citronix.domain.mapper.FarmMapper;
import com.example.citronix.domain.mapper.FieldMapper;
import com.example.citronix.domain.vm.FarmResponseVM;
import com.example.citronix.repository.FarmRepository;
import com.example.citronix.repository.specification.FarmSpecification;
import com.example.citronix.service.Impl.FarmServiceImpl;
import com.example.citronix.service.interfaces.FieldService;
import com.example.citronix.shared.exception.FarmAlreadyExistsException;
import com.example.citronix.shared.exception.FarmNotFoundException;
import com.example.citronix.shared.exception.InvalidFarmCreationDateException;
import com.example.citronix.shared.exception.TotalFieldAreaExceedsFarmAreaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class FarmServiceTest {

    @InjectMocks
    private FarmServiceImpl farmService;
    @Mock
    private FarmRepository farmRepository;

    @Mock
    private FarmMapper farmMapper;

    @Mock
    private FieldService fieldService;

    @Mock
    private FieldMapper fieldMapper;

    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateFarm_ShouldThrowInvalidFarmCreationDateException_WhenCreationDateIsInTheFuture() {
        CreateFarmRequestDto farmRequestDto = new CreateFarmRequestDto();
        farmRequestDto.setCreationDate(LocalDate.now().plusDays(1));

        assertThrows(InvalidFarmCreationDateException.class, () -> farmService.createFarm(farmRequestDto));
    }

    @Test
    public void testCreateFarm_ShouldThrowFarmAlreadyExistsException_WhenFarmWithSameNameAndLocationExists() {
        CreateFarmRequestDto farmRequestDto = new CreateFarmRequestDto();
        farmRequestDto.setName("Farm A");
        farmRequestDto.setLocation("Location A");
        farmRequestDto.setCreationDate(LocalDate.now());

        when(farmRepository.existsByNameAndLocation(farmRequestDto.getName(), farmRequestDto.getLocation()))
                .thenReturn(true);

        assertThrows(FarmAlreadyExistsException.class, () -> farmService.createFarm(farmRequestDto));
    }

    @Test
    public void testCreateFarm_ShouldCreateFarmSuccessfully_WhenValidRequest() {
        CreateFarmRequestDto farmRequestDto = new CreateFarmRequestDto();
        farmRequestDto.setName("Farm A");
        farmRequestDto.setLocation("Location A");
        farmRequestDto.setArea(1000.0);
        farmRequestDto.setCreationDate(LocalDate.now());

        Farm farm = new Farm();
        FarmResponseVM farmResponseVM = new FarmResponseVM();
        when(farmMapper.toEntity(farmRequestDto)).thenReturn(farm);
        when(farmRepository.save(farm)).thenReturn(farm);
        when(farmMapper.toDto(farm)).thenReturn(farmResponseVM);

        FarmResponseVM response = farmService.createFarm(farmRequestDto);

        assertNotNull(response);
        verify(farmRepository).save(farm);
    }

    @Test
    public void testGetFarmById_ShouldThrowFarmNotFoundException_WhenFarmDoesNotExist() {
        when(farmRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FarmNotFoundException.class, () -> farmService.getFarmById(1L));
    }

    @Test
    public void testGetFarmById_ShouldReturnFarmSuccessfully_WhenFarmExists() {
        Farm farm = new Farm();
        FarmResponseVM farmResponseVM = new FarmResponseVM();
        when(farmRepository.findById(1L)).thenReturn(Optional.of(farm));
        when(farmMapper.toDto(farm)).thenReturn(farmResponseVM);

        FarmResponseVM response = farmService.getFarmById(1L);

        assertNotNull(response);
        verify(farmRepository).findById(1L);
    }

    @Test
    public void testDeleteFarm_ShouldThrowFarmNotFoundException_WhenFarmDoesNotExist() {
        when(farmRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FarmNotFoundException.class, () -> farmService.deleteFarm(1L));
    }

    @Test
    public void testDeleteFarm_ShouldDeleteFarmSuccessfully_WhenFarmExists() {
        Farm farm = new Farm();
        when(farmRepository.findById(1L)).thenReturn(Optional.of(farm));

        farmService.deleteFarm(1L);

        verify(farmRepository).delete(farm);
    }

    @Test
    public void testUpdateFarm_ShouldThrowFarmNotFoundException_WhenFarmDoesNotExist() {
        CreateFarmRequestDto farmRequestDto = new CreateFarmRequestDto();
        farmRequestDto.setName("Farm B");
        farmRequestDto.setLocation("Location B");

        when(farmRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(FarmNotFoundException.class, () -> farmService.updateFarm(1L, farmRequestDto));
    }

    @Test

    public void testUpdateFarm_ShouldUpdateFarmSuccessfully_WhenValidRequest() {
        CreateFarmRequestDto farmRequestDto = new CreateFarmRequestDto();
        farmRequestDto.setName("Farm B");
        farmRequestDto.setLocation("Location B");
        farmRequestDto.setCreationDate(LocalDate.now());
        farmRequestDto.setArea(2000.0);

        Farm existingFarm = new Farm();
        existingFarm.setId(1L);
        existingFarm.setName("Farm A");
        existingFarm.setLocation("Location A");
        existingFarm.setArea(1000.0);

        when(farmRepository.findById(1L)).thenReturn(Optional.of(existingFarm));
        when(farmRepository.save(existingFarm)).thenReturn(existingFarm);
        when(farmMapper.toDto(existingFarm)).thenReturn(new FarmResponseVM());

        FarmResponseVM response = farmService.updateFarm(1L, farmRequestDto);

        assertNotNull(response);
        assertEquals("Farm B", existingFarm.getName());
        assertEquals("Location B", existingFarm.getLocation());
        verify(farmRepository).save(existingFarm);
    }

    @Test
    public void testCreateFarm_ShouldThrowTotalFieldAreaExceedsFarmAreaException_WhenTotalFieldAreaExceedsFarmArea() {
        CreateFarmRequestDto farmRequestDto = new CreateFarmRequestDto();
        farmRequestDto.setName("Farm A");
        farmRequestDto.setLocation("Location A");
        farmRequestDto.setCreationDate(LocalDate.now());
        farmRequestDto.setArea(5000.0);

        FramWithFieldRequestdTO field1 = new FramWithFieldRequestdTO();
        field1.setArea(3000.0);

        FramWithFieldRequestdTO field2 = new FramWithFieldRequestdTO();
        field2.setArea(3000.0);

        farmRequestDto.setFields(List.of(field1, field2));

        assertThrows(TotalFieldAreaExceedsFarmAreaException.class, () -> farmService.createFarm(farmRequestDto));
    }

    @Test
    public void testGetAllFarms_ShouldReturnListOfFarms() {
        Farm farm1 = new Farm();
        farm1.setId(1L);
        farm1.setName("Farm A");

        Farm farm2 = new Farm();
        farm2.setId(2L);
        farm2.setName("Farm B");

        List<Farm> mockFarms = Arrays.asList(farm1, farm2);

        FarmResponseVM responseVM1 = new FarmResponseVM();
        responseVM1.setId(1L);
        responseVM1.setName("Farm A");

        FarmResponseVM responseVM2 = new FarmResponseVM();
        responseVM2.setId(2L);
        responseVM2.setName("Farm B");

        List<FarmResponseVM> mockResponseVMs = Arrays.asList(responseVM1, responseVM2);

        when(farmRepository.findAll()).thenReturn(mockFarms);
        when(farmMapper.toDtoList(mockFarms)).thenReturn(mockResponseVMs);
        List<FarmResponseVM> result = farmService.getAllFarms();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Farm A", result.get(0).getName());
        assertEquals("Farm B", result.get(1).getName());
        verify(farmRepository).findAll();
        verify(farmMapper).toDtoList(mockFarms);
    }





}