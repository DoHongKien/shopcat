package com.assignment.promotion.service;

import com.assignment.entity.Promotion;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IPromotionService {

    List<Promotion> findAllPromotion();

    Promotion findById(Integer id);

    Promotion savePromotion(Promotion promotion);

    void deletePromotion(Integer id);

    void updateProductInPromotion(Integer id, Integer productId);
}
