package com.example.citronix;

import com.example.citronix.domain.dtos.request.field.FieldRequestDto;
import com.example.citronix.domain.entity.Farm;
import com.example.citronix.domain.entity.Field;
import com.example.citronix.domain.mapper.FarmMapper;
import com.example.citronix.domain.mapper.FieldMapper;
import com.example.citronix.domain.vm.FarmResponseVM;
import com.example.citronix.domain.vm.FieldResponseVM;
import com.example.citronix.repository.FieldRepository;
import com.example.citronix.service.Impl.FieldServiceImpl;
import com.example.citronix.service.interfaces.FarmService;
import com.example.citronix.shared.exception.FarmNotFoundException;
import com.example.citronix.shared.exception.FieldNotFoundException;
import com.example.citronix.shared.exception.TotalFieldAreaExceedsFarmAreaException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FieldServiceTest {
    @Mock
    private FieldRepository fieldRepository;

    @Mock
    private FarmService farmService;

    @Mock
    private FieldMapper fieldMapper;

    @Mock
    private FarmMapper farmMapper;

    @InjectMocks
    private FieldServiceImpl fieldService;
    @BeforeEach
    void beforeEach() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateField_Success() {
        FieldRequestDto fieldRequest = new FieldRequestDto();
        fieldRequest.setFarmId(1L);
        fieldRequest.setName("Champ 1");
        fieldRequest.setArea(1500.0);

        FarmResponseVM farmResponse = new FarmResponseVM();
        farmResponse.setId(1L);
        farmResponse.setArea(5000.0);
        farmResponse.setFields(List.of());

        Field fieldEntity = new Field();
        fieldEntity.setId(1L);
        fieldEntity.setName("Champ 1");
        fieldEntity.setArea(1500.0);

        FieldResponseVM fieldResponse = new FieldResponseVM();
        fieldResponse.setId(1L);
        fieldResponse.setName("Champ 1");
        fieldResponse.setArea(1500.0);

        when(farmService.getFarmById(1L)).thenReturn(farmResponse);
        when(fieldMapper.toEntity(fieldRequest)).thenReturn(fieldEntity);
        when(fieldRepository.save(fieldEntity)).thenReturn(fieldEntity);
        when(fieldMapper.toDto(fieldEntity)).thenReturn(fieldResponse);

        FieldResponseVM result = fieldService.createField(fieldRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Champ 1", result.getName());
        verify(farmService).getFarmById(1L);
        verify(fieldRepository).save(fieldEntity);
    }

    @Test
    void testCreateField_FarmNotFoundException() {
        FieldRequestDto fieldRequest = new FieldRequestDto();
        fieldRequest.setFarmId(1L);

        when(farmService.getFarmById(1L)).thenReturn(null);
        assertThrows(FarmNotFoundException.class, () -> fieldService.createField(fieldRequest));
    }

    @Test
    void testCreateField_TotalFieldAreaExceedsFarmAreaException() {
        FieldRequestDto fieldRequest = new FieldRequestDto();
        fieldRequest.setFarmId(1L);
        fieldRequest.setArea(1500.0);

        FarmResponseVM farmResponse = new FarmResponseVM();
        farmResponse.setId(1L);
        farmResponse.setArea(2000.0);
        farmResponse.setFields(List.of(new FieldResponseVM(1L, "Champ A", 1000.0)));

        when(farmService.getFarmById(1L)).thenReturn(farmResponse);

        assertThrows(TotalFieldAreaExceedsFarmAreaException.class, () -> fieldService.createField(fieldRequest));
    }

    @Test
    void testGetFieldById_Success() {
        Farm farm = new Farm();
        farm.setId(1L);
        farm.setName("Farm 1");

        Field fieldEntity = new Field();
        fieldEntity.setId(1L);
        fieldEntity.setName("Field 1");
        fieldEntity.setFarm(farm);

        FieldResponseVM fieldResponse = new FieldResponseVM();
        fieldResponse.setId(1L);
        fieldResponse.setName("Champ 1");

        when(fieldRepository.findById(1L)).thenReturn(Optional.of(fieldEntity));
        when(fieldMapper.toDto(fieldEntity)).thenReturn(fieldResponse);
        FieldResponseVM result = fieldService.getFieldById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Champ 1", result.getName());
        verify(fieldRepository).findById(1L);
        verify(fieldMapper).toDto(fieldEntity);
    }


    @Test
    void testGetFieldById_FieldNotFoundException() {
        when(fieldRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(FieldNotFoundException.class, () -> fieldService.getFieldById(1L));
    }

    @Test
    void testDeleteField_Success() {
        Field fieldEntity = new Field();
        fieldEntity.setId(1L);
        when(fieldRepository.findById(1L)).thenReturn(Optional.of(fieldEntity));

        fieldService.deleteField(1L);
        verify(fieldRepository).delete(fieldEntity);
    }

    @Test
    void testDeleteField_FieldNotFoundException() {
        when(fieldRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(FieldNotFoundException.class, () -> fieldService.deleteField(1L));
    }
}
