package com.example.citronix.service.Impl;

import com.example.citronix.domain.dtos.request.tree.TreeRequestDto;
import com.example.citronix.domain.entity.Field;
import com.example.citronix.domain.entity.Tree;
import com.example.citronix.domain.enums.TreeStatus;
import com.example.citronix.domain.mapper.TreeMapper;
import com.example.citronix.domain.vm.TreeResponseVM;
import com.example.citronix.repository.FieldRepository;
import com.example.citronix.repository.TreeRepository;
import com.example.citronix.service.interfaces.TreeService;
import com.example.citronix.shared.exception.FieldNotFoundException;
import com.example.citronix.shared.exception.InvalidPlantationPeriodException;
import com.example.citronix.shared.exception.MaxTreeDensityExceededException;
import com.example.citronix.shared.exception.TreeNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TreeServiceImpl implements TreeService {
    private final TreeRepository treeRepository;
    private final TreeMapper treeMapper;
    private final FieldRepository fieldRepository;

    @Override
    public TreeResponseVM createTree(TreeRequestDto treeRequestDto) {
        Tree tree = treeMapper.toEntity(treeRequestDto);
        tree.setStatus(tree.calculateStatus());
        if (tree.getPlantationDate().getMonthValue() < 3 || tree.getPlantationDate().getMonthValue() > 5) {
            throw new InvalidPlantationPeriodException("Les arbres ne peuvent être plantés qu'entre mars et mai.");
        }
        Field field = fieldRepository.findById(tree.getField().getId()).orElseThrow(()->new FieldNotFoundException("champ n'existe pas"));
        int currentCountTrees = field.getTrees().size();
        // fallait etre en m2
        int maxAllowedTrees = (int) (10 * field.getArea() / 1000);
        if (currentCountTrees >= maxAllowedTrees) {
            throw new MaxTreeDensityExceededException("Le champ ne peut pas contenir plus de " + maxAllowedTrees + " arbres.");
        }
        Tree saveTree = treeRepository.save(tree);
        return treeMapper.toDto(saveTree);
    }

    @Override
    public Page<TreeResponseVM> getAllTrees(PageRequest pageRequest) {
        Page<Tree> trees = treeRepository.findAll(pageRequest);
        return trees.map(treeMapper::toDto);
    }

    @Override
    public TreeResponseVM getTreeById(Long id) {
        Tree tree = treeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Arbre non trouvé."));

        TreeStatus status = tree.calculateStatus();

        return TreeResponseVM.builder()
                .plantationDate(tree.getPlantationDate())
                .status(status)
                .fieldId(tree.getField().getId())
                .age(tree.getAge())
                .productivity(tree.getProductivity())
                .build();
    }

    @Override
    public TreeResponseVM updateTree(Long id, TreeRequestDto treeRequestDto) {
        Tree existingTree = treeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Arbre non trouvé."));
        existingTree.setPlantationDate(treeRequestDto.getPlantationDate());
        existingTree.setStatus(existingTree.calculateStatus());

        if (treeRequestDto.getFieldId() != null) {
            Field field = fieldRepository.findById(treeRequestDto.getFieldId())
                    .orElseThrow(() -> new IllegalArgumentException("Champ non trouvé."));
            existingTree.setField(field);
        }
        if (existingTree.getPlantationDate().getMonthValue() < 3 || existingTree.getPlantationDate().getMonthValue() > 5) {
            throw new InvalidPlantationPeriodException("Les arbres ne peuvent être plantés qu'entre mars et mai.");
        }
        Tree savedTree = treeRepository.save(existingTree);
        return treeMapper.toDto(savedTree);
    }


    @Override
    public void deleteTree(Long id) {
        if (treeRepository.findById(id).isEmpty()) {
            throw new TreeNotFoundException("Arbre non trouvé");
        }
        treeRepository.deleteById(id);
    }
}
