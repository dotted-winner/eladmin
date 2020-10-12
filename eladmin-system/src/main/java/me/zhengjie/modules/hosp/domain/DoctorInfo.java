/*
*  Copyright 2019-2020 Zheng Jie
*
*  Licensed under the Apache License, Version 2.0 (the "License");
*  you may not use this file except in compliance with the License.
*  You may obtain a copy of the License at
*
*  http://www.apache.org/licenses/LICENSE-2.0
*
*  Unless required by applicable law or agreed to in writing, software
*  distributed under the License is distributed on an "AS IS" BASIS,
*  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*  See the License for the specific language governing permissions and
*  limitations under the License.
*/
package me.zhengjie.modules.hosp.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import io.swagger.annotations.ApiModelProperty;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import javax.validation.constraints.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.hibernate.annotations.*;
import java.sql.Timestamp;
import java.io.Serializable;

/**
* @website https://el-admin.vip
* @description /
* @author luoqiz
* @date 2020-10-01
**/
@Entity
@Data
@Table(name="hosp_doctor_info")
public class DoctorInfo implements Serializable {

    @Id
    @Column(name = "user_id")
    @ApiModelProperty(value = "ID")
    private Long userId;

    @Column(name = "username",unique = true,nullable = false)
    @NotBlank
    @ApiModelProperty(value = "用户名")
    private String username;

    @Column(name = "school")
    @ApiModelProperty(value = "毕业院校")
    private String school;

    @Column(name = "from_date")
    @ApiModelProperty(value = "从业日期")
    private Timestamp fromDate;

    @Column(name = "good_at")
    @ApiModelProperty(value = "专业擅长")
    private String goodAt;

    @Column(name = "detail")
    @ApiModelProperty(value = "详细介绍")
    private String detail;

    @Column(name = "create_by")
    @ApiModelProperty(value = "创建者")
    private String createBy;

    @Column(name = "update_by")
    @ApiModelProperty(value = "更新着")
    private String updateBy;

    @Column(name = "create_time")
    @CreationTimestamp
    @ApiModelProperty(value = "创建日期")
    private Timestamp createTime;

    @Column(name = "update_time")
    @UpdateTimestamp
    @ApiModelProperty(value = "更新时间")
    private Timestamp updateTime;

    public void copy(DoctorInfo source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}