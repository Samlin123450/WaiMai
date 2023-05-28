package com.sam.Reggie.dto;

import com.sam.Reggie.entity.Setmeal;
import com.sam.Reggie.entity.SetmealDish;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class MealDto extends Setmeal {
    private List<SetmealDish> setmealDishes = new ArrayList<>();



    private String CategoryName;

}
