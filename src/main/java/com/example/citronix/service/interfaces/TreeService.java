package com.example.citronix.service.interfaces;

import com.example.citronix.domain.dtos.request.tree.TreeRequestDto;
import com.example.citronix.domain.vm.TreeResponseVM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface TreeService {
    TreeResponseVM createTree(TreeRequestDto treeRequestDto);
    Page<TreeResponseVM> getAllTrees(PageRequest pageRequest);
    TreeResponseVM getTreeById(Long id);
    TreeResponseVM updateTree(Long id, TreeRequestDto treeRequestDto);
    void deleteTree(Long id);
    List<TreeResponseVM> findByFieldId(Long fieldId);
}
