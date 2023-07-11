package com.assignment.promotion.service;

import com.assignment.entity.Promotion;
import com.assignment.promotion.repo.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PromotionService implements IPromotionService{

    private final PromotionRepository promotionRepository;

    @Autowired
    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Override
    public List<Promotion> findAllPromotion() {
        return promotionRepository.findAll();
    }

    @Override
    public Promotion findById(Integer id) {
        return promotionRepository.findById(id).get();
    }

    @Override
    public Promotion savePromotion(Promotion promotion) {
        return promotionRepository.save(promotion);
    }

    @Override
    public void deletePromotion(Integer id) {
        promotionRepository.deleteById(id);
    }

//    @Override
//    public void updateProductInPromotion(Integer id, Integer productId) {
//        promotionRepository.updateProductInPromotion(id, productId);
//    }
}
