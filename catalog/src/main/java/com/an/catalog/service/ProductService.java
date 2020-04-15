package com.an.catalog.service;

import com.an.common.bean.Product;

import java.util.Date;
import java.util.List;

public interface ProductService {

    public Date getSysdate();

    public Product getProductById (Long productId);

    public List<Product> findProductByServiceId (Long serviceId);

    public Product createProduct(Long serviceId, String productName, String productType, Long productFee, Long discount, Long startMoney);

    public Product updateProduct(Product product);
}
