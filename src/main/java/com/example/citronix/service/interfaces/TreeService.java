package com.example.citronix.service.interfaces;

import com.example.citronix.domain.dtos.request.tree.TreeRequestDto;
import com.example.citronix.domain.vm.TreeResponseVM;

import java.util.List;

public interface TreeService {
    TreeResponseVM createTree(TreeRequestDto treeRequestDto);
    List<TreeResponseVM> getAllTrees();
    TreeResponseVM getTreeById(Long id);
    TreeResponseVM updateTree(Long id, TreeRequestDto treeRequestDto);
    void deleteTree(Long id);
}
