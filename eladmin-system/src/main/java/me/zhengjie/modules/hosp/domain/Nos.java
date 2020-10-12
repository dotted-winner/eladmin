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
@Table(name="hosp_nos")
public class Nos implements Serializable {

    @Id
    @Column(name = "id")
    @ApiModelProperty(value = "ID")
    private Long id;

    @Column(name = "user_id",unique = true,nullable = false)
    @NotNull
    @ApiModelProperty(value = "user_id")
    private Long userId;

    @Column(name = "username",unique = true,nullable = false)
    @NotBlank
    @ApiModelProperty(value = "用户名")
    private String username;

    @Column(name = "release_date")
    @ApiModelProperty(value = "发布时间")
    private Timestamp releaseDate;

    @Column(name = "numbers")
    @ApiModelProperty(value = "发布数量")
    private Integer numbers;

    @Column(name = "clinic_date")
    @ApiModelProperty(value = "就诊日期")
    private Timestamp clinicDate;

    @Column(name = "time_nodes")
    @ApiModelProperty(value = "时间节点")
    private String timeNodes;

    @Column(name = "take_date")
    @ApiModelProperty(value = "取号时间")
    private String takeDate;

    @Column(name = "status")
    @ApiModelProperty(value = "发布状态")
    private String status;

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

    public void copy(Nos source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}