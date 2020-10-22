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
@Table(name="hosp_patient_nos")
public class PatientNos implements Serializable {

    @Id
    @Column(name = "id")
    @ApiModelProperty(value = "号源id")
    private Long id;

    @Column(name = "user_id",unique = true,nullable = false)
    @NotNull
    @ApiModelProperty(value = "ID")
    private Long userId;

    @Column(name = "username",unique = true)
    @ApiModelProperty(value = "用户名")
    private String username;

    @Column(name = "registration")
    @ApiModelProperty(value = "挂号日期")
    private Timestamp registration;

    @Column(name = "status")
    @ApiModelProperty(value = "挂号状态")
    private String status;

    @Column(name = "nos_id")
    @ApiModelProperty(value = "号源id")
    private long nosId;

    @Column(name = "no_no")
    @ApiModelProperty(value = "号源编号")
    private String noNo;

    @Column(name = "create_by")
    @ApiModelProperty(value = "创建者")
    private String createBy;

    @Column(name = "update_by")
    @ApiModelProperty(value = "更新者")
    private String updateBy;

    @Column(name = "create_time")
    @ApiModelProperty(value = "创建日期")
    private Timestamp createTime;

    @Column(name = "update_time")
    @ApiModelProperty(value = "更新时间")
    private Timestamp updateTime;

    public void copy(PatientNos source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}