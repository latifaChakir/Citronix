package com.example.citronix.domain.mapper;

import com.example.citronix.domain.dtos.request.tree.TreeRequestDto;
import com.example.citronix.domain.entity.Tree;
import com.example.citronix.domain.vm.TreeResponseVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")

public interface TreeMapper {
    @Mapping(source = "fieldId", target = "field.id")
    Tree toEntity(TreeRequestDto treeRequestDto);
    Tree toEntity(TreeResponseVM treeResponseVM);
    @Mapping(source = "field.id", target = "fieldId")
    @Mapping(source = "status", target = "status")
    TreeResponseVM toDto(Tree tree);
    @Mapping(source = "field.id", target = "fieldId")
    List<TreeResponseVM> toDtoList(List<Tree> trees);
}
