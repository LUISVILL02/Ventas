package com.ventas.tienda.service.impl;

import com.ventas.tienda.model.Dtos.save.ShippingDetailsDtoSave;
import com.ventas.tienda.model.Dtos.send.ShippingDetailsDtoSend;
import com.ventas.tienda.model.entities.Order;
import com.ventas.tienda.model.entities.ShippingDetails;
import com.ventas.tienda.model.mapper.ShippingDetailsMapper;
import com.ventas.tienda.repository.OrderRepository;
import com.ventas.tienda.repository.ShippingDetailsRepository;
import com.ventas.tienda.service.ShippingDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ShippingDetailsServiceImpl extends ServiceImp<ShippingDetailsDtoSave, ShippingDetailsDtoSend, ShippingDetails> implements ShippingDetailsService {
    private final ShippingDetailsRepository shippingDetailsRepository;
    private final ShippingDetailsMapper shippingDetailsMapper;
    private final OrderRepository  orderRepository;
    protected ShippingDetailsServiceImpl(ShippingDetailsRepository shippingDetailsRepository, ShippingDetailsMapper shippingDetailsMapper, OrderRepository orderRepository) {
        super(shippingDetailsRepository, shippingDetailsMapper);
        this.shippingDetailsRepository = shippingDetailsRepository;
        this.shippingDetailsMapper = shippingDetailsMapper;
        this.orderRepository = orderRepository;
    }

    @Override
    public List<ShippingDetailsDtoSend> findByOrder_IdOrder(Long idOrder) {
        List<ShippingDetails> shippingDetailsList = shippingDetailsRepository.findByOrder_IdOrder(idOrder);
        return shippingDetailsMapper.ListEntityToDtoSend(shippingDetailsList);
    }

    @Override
    public List<ShippingDetailsDtoSend> findByCarrier(String carrier) {
        List<ShippingDetails> shippingDetailsList = shippingDetailsRepository.findByCarrier(carrier);
        return shippingDetailsMapper.ListEntityToDtoSend(shippingDetailsList);
    }

    @Override
    public List<ShippingDetailsDtoSend> findByOrder_Status(Order.Status status) {
        List<ShippingDetails> shippingDetailsList = shippingDetailsRepository.findByOrder_Status(status);
        return shippingDetailsMapper.ListEntityToDtoSend(shippingDetailsList);
    }

    @Override
    public ShippingDetailsDtoSend save(ShippingDetailsDtoSave shippingDetailsDtoSave, Long idOrder) {
        Optional<Order> order = orderRepository.findById(idOrder);
        if (order.isEmpty())
            throw new RuntimeException("Order not found");
        ShippingDetails shippingDetails = shippingDetailsMapper.dtoSaveToEntity(shippingDetailsDtoSave);
        shippingDetails.setOrder(order.get());
        order.get().setDetails(shippingDetails);
        return shippingDetailsMapper.EntityToDtoSend(shippingDetailsRepository.save(shippingDetails));
    }

    @Override
    public ShippingDetailsDtoSend update(ShippingDetailsDtoSave shippingDetailsDtoSave, Long id) {
        Optional<ShippingDetails> shippingDetails = shippingDetailsRepository.findById(id);
        if (shippingDetails.isEmpty())
            throw new RuntimeException("ShippingDetails not found");
        ShippingDetails shippingDetails1 = shippingDetails.get().shippingDetailsUpdate(
                                            shippingDetailsMapper.dtoSaveToEntity(shippingDetailsDtoSave));
        return shippingDetailsMapper.EntityToDtoSend(shippingDetailsRepository.save(shippingDetails1));
    }
}
