package com.an.catalog.service;

import com.an.catalog.entity.ProductEntity;
import com.an.catalog.repository.ProductRepository;
import com.an.common.bean.Product;
import com.an.common.utils.Const;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductRepository repository;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public Date getSysdate() {
        return repository.getSysdate();
    }

    @Override
    @Cacheable(cacheNames = "product", key = "#productId", unless = "#result == null")
    public Product getProductById(Long productId) {
        Optional<ProductEntity> optional = repository.findById(productId);
        if (Objects.nonNull(optional) && optional.isPresent()){
            return modelMapper.map(optional.get(), Product.class);
        }
        return null;
    }

    @Override
    @Cacheable(cacheNames = "products", key = "#serviceId", unless = "#result == null")
    public List<Product> findProductByServiceId(Long serviceId) {
        List<Product> output = new ArrayList<>();
        List<ProductEntity> lst = repository.findByServiceIdAndStatus(serviceId, Const.PRODUCT.STATUS_ON);
        if (lst != null && !lst.isEmpty()){
            lst.forEach(x -> output.add(modelMapper.map(x,Product.class)));
        }
        return output;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Product createProduct(Long serviceId, String productName, String productType, Long productFee, Long discount, Long startMoney) {
        ProductEntity entity = new ProductEntity();
        entity.setServiceId(serviceId);
        entity.setProductName(productName);
        entity.setProductType(productType);
        entity.setProductFee(productFee);
        entity.setDiscount(discount);
        entity.setStartMoney(startMoney);
        entity.setCreateDatetime(repository.getSysdate());
        entity.setStatus(Const.PRODUCT.STATUS_ON);
        entity = repository.save(entity);
        if (Objects.nonNull(entity)){
            return modelMapper.map(entity, Product.class);
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Product updateProduct(Product product) {
        ProductEntity entity = modelMapper.map(product, ProductEntity.class);
        entity = repository.save(entity);
        if (Objects.nonNull(entity)){
            return modelMapper.map(entity, Product.class);
        }
        return null;
    }
}
