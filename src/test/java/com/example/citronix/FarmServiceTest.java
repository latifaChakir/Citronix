package com.example.citronix;

import com.example.citronix.domain.dtos.request.farm.CreateFarmRequestDto;
import com.example.citronix.domain.entity.Farm;
import com.example.citronix.domain.mapper.FarmMapper;
import com.example.citronix.domain.mapper.FieldMapper;
import com.example.citronix.domain.vm.FarmResponseVM;
import com.example.citronix.repository.FarmRepository;
import com.example.citronix.service.Impl.FarmServiceImpl;
import com.example.citronix.service.interfaces.FieldService;
import com.example.citronix.shared.exception.FarmAlreadyExistsException;
import com.example.citronix.shared.exception.FarmNotFoundException;
import com.example.citronix.shared.exception.InvalidFarmCreationDateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        farmRequestDto.setCreationDate(LocalDate.now().plusDays(1)); // Future date

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
        existingFarm.setName("Farm A");  // Ensure the existing name is set here
        existingFarm.setLocation("Location A");
        existingFarm.setArea(1000.0);

        // Mock the repository and service interactions
        when(farmRepository.findById(1L)).thenReturn(Optional.of(existingFarm));
        when(farmRepository.save(existingFarm)).thenReturn(existingFarm);
        when(farmMapper.toDto(existingFarm)).thenReturn(new FarmResponseVM());

        // Execute the update
        FarmResponseVM response = farmService.updateFarm(1L, farmRequestDto);

        // Assertions
        assertNotNull(response);
        assertEquals("Farm B", existingFarm.getName());  // Assert that the name has been updated
        assertEquals("Location B", existingFarm.getLocation()); // Check other properties if necessary
        verify(farmRepository).save(existingFarm);  // Ensure save was called
    }
}
