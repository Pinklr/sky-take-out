package com.sky.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "EmployeeDTO", description = "员工信息")
public class EmployeeDTO implements Serializable {


    private Long id;
    @ApiModelProperty("用户名必须唯一")
    private String username;

    private String name;

    private String phone;

    private String sex;

    private String idNumber;

}
