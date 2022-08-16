package com.example.iccmenber.demo03.service.impl;


import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.iccmenber.demo03.mapper.HotelMapper;
import com.example.iccmenber.demo03.pojo.Hotel;
import com.example.iccmenber.demo03.service.IHotelSerice;
import org.springframework.stereotype.Service;

@Service
public class HotelService extends ServiceImpl<HotelMapper, Hotel> implements IHotelSerice {
}
