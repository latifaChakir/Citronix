package com.example.citronix;

import com.example.citronix.domain.dtos.request.tree.TreeRequestDto;
import com.example.citronix.domain.entity.Field;
import com.example.citronix.domain.entity.Tree;
import com.example.citronix.domain.enums.TreeStatus;
import com.example.citronix.domain.mapper.TreeMapper;
import com.example.citronix.domain.vm.TreeResponseVM;
import com.example.citronix.repository.FieldRepository;
import com.example.citronix.repository.TreeRepository;
import com.example.citronix.service.Impl.TreeServiceImpl;
import com.example.citronix.shared.exception.InvalidPlantationPeriodException;
import com.example.citronix.shared.exception.MaxTreeDensityExceededException;
import com.example.citronix.shared.exception.TreeNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TreeServiceTest {
    @InjectMocks
    private TreeServiceImpl treeService;

    @Mock
    private TreeRepository treeRepository;

    @Mock
    private FieldRepository fieldRepository;

    @Mock
    private TreeMapper treeMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testCreateTree_Success() {
        TreeRequestDto treeRequest = new TreeRequestDto();
        treeRequest.setPlantationDate(LocalDate.of(2024, 3, 15));
        treeRequest.setFieldId(1L);

        Field field = new Field();
        field.setId(1L);
        field.setArea(1000.0); // Area in m²
        field.setTrees(Collections.emptyList());

        Tree tree = new Tree();
        tree.setField(field);
        tree.setPlantationDate(treeRequest.getPlantationDate());
        tree.setStatus(TreeStatus.MATURE);

        Tree savedTree = new Tree();
        savedTree.setId(1L);
        savedTree.setField(field);

        TreeResponseVM treeResponse = new TreeResponseVM();
        treeResponse.setId(1L);

        when(fieldRepository.findById(1L)).thenReturn(Optional.of(field));
        when(treeMapper.toEntity(treeRequest)).thenReturn(tree);
        when(treeRepository.save(tree)).thenReturn(savedTree);
        when(treeMapper.toDto(savedTree)).thenReturn(treeResponse);

        TreeResponseVM result = treeService.createTree(treeRequest);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(treeRepository).save(tree);
    }


    @Test
    void testGetTreeById_Success() {
        Field field = new Field();
        field.setId(1L);

        Tree tree = new Tree();
        tree.setId(1L);
        tree.setField(field);
        tree.setStatus(TreeStatus.MATURE);

        TreeResponseVM treeResponse = new TreeResponseVM();
        treeResponse.setId(1L);

        when(treeRepository.findById(1L)).thenReturn(Optional.of(tree));
        when(treeMapper.toDto(tree)).thenReturn(treeResponse);

        TreeResponseVM result = treeService.getTreeById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(treeRepository).findById(1L);
    }
    @Test
    void testDeleteTree_Success() {
        when(treeRepository.findById(1L)).thenReturn(Optional.of(new Tree()));

        treeService.deleteTree(1L);

        verify(treeRepository).deleteById(1L);
    }
    @Test
    void testDeleteTree_TreeNotFound() {
        when(treeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TreeNotFoundException.class, () -> treeService.deleteTree(1L));
        verify(treeRepository, never()).deleteById(anyLong());
    }
    @Test
    public void testGetAllTrees_Success() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Tree> treePage = new PageImpl<>(List.of(new Tree(), new Tree()));

        when(treeRepository.findAll(pageRequest)).thenReturn(treePage);
        when(treeMapper.toDto(any(Tree.class))).thenReturn(new TreeResponseVM());

        Page<TreeResponseVM> result = treeService.getAllTrees(pageRequest);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
    }

    @Test
    public void testGetAllTrees_EmptyResult() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Tree> treePage = new PageImpl<>(List.of());

        when(treeRepository.findAll(pageRequest)).thenReturn(treePage);
        when(treeMapper.toDto(any(Tree.class))).thenReturn(new TreeResponseVM());

        Page<TreeResponseVM> result = treeService.getAllTrees(pageRequest);

        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
    }
    @Test
    public void testFindByFieldId_Success() {
        List<Tree> trees = List.of(new Tree(), new Tree());
        when(treeRepository.findByFieldId(1L)).thenReturn(trees);
        when(treeMapper.toDtoList(trees)).thenReturn(List.of(new TreeResponseVM(), new TreeResponseVM()));

        List<TreeResponseVM> result = treeService.findByFieldId(1L);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testFindByFieldId_NoTreesFound() {
        List<Tree> trees = List.of();
        when(treeRepository.findByFieldId(1L)).thenReturn(trees);

        List<TreeResponseVM> result = treeService.findByFieldId(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }



    @Test
    void testGetTreeById_TreeNotFound() {
        when(treeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TreeNotFoundException.class, () -> treeService.getTreeById(1L));
    }
    @Test
    public void testUpdateTree_Success() {
        TreeRequestDto treeRequestDto = new TreeRequestDto();
        treeRequestDto.setPlantationDate(LocalDate.of(2024, 3, 15));
        treeRequestDto.setFieldId(1L);

        Tree existingTree = new Tree();
        existingTree.setId(1L);
        existingTree.setPlantationDate(LocalDate.of(2024, 3, 15));

        Field field = new Field();
        field.setId(1L);

        Tree updatedTree = new Tree();
        updatedTree.setId(1L);
        updatedTree.setField(field);
        updatedTree.setPlantationDate(treeRequestDto.getPlantationDate());
        updatedTree.setStatus(TreeStatus.MATURE);

        TreeResponseVM treeResponseVM = new TreeResponseVM();
        treeResponseVM.setId(1L);

        when(treeRepository.findById(1L)).thenReturn(Optional.of(existingTree));
        when(fieldRepository.findById(1L)).thenReturn(Optional.of(field));
        when(treeRepository.save(existingTree)).thenReturn(updatedTree);
        when(treeMapper.toDto(updatedTree)).thenReturn(treeResponseVM);

        TreeResponseVM result = treeService.updateTree(1L, treeRequestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(treeRepository).save(existingTree);
    }






}
