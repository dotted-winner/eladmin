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
package me.zhengjie.modules.hosp.rest;

import me.zhengjie.annotation.AnonymousAccess;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.hosp.domain.PatientInfo;
import me.zhengjie.modules.hosp.service.PatientInfoService;
import me.zhengjie.modules.hosp.service.dto.PatientInfoQueryCriteria;
import me.zhengjie.modules.hosp.vo.PatientVO;
import me.zhengjie.modules.system.domain.User;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://el-admin.vip
* @author luoqiz
* @date 2020-10-01
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "医疗：患者信息管理")
@RequestMapping("/api/patientInfo")
public class PatientInfoController {

    private final PatientInfoService patientInfoService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('patientInfo:list')")
    public void download(HttpServletResponse response, PatientInfoQueryCriteria criteria) throws IOException {
        patientInfoService.download(patientInfoService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询患者信息")
    @ApiOperation("查询患者信息")
    @PreAuthorize("@el.check('patientInfo:list')")
    public ResponseEntity<Object> query(PatientInfoQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(patientInfoService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增患者信息")
    @ApiOperation("新增患者信息")
    @PreAuthorize("@el.check('patientInfo:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody PatientInfo resources){
        return new ResponseEntity<>(patientInfoService.create(resources),HttpStatus.CREATED);
    }

    @PostMapping("/add")
    @Log("新增患者信息")
    @ApiOperation("新增患者信息")
    @AnonymousAccess
    public ResponseEntity<Object> create(@Validated @RequestBody PatientVO patientVO){
        return new ResponseEntity<>(patientInfoService.createInfo(patientVO),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改患者信息")
    @ApiOperation("修改患者信息")
    @PreAuthorize("@el.check('patientInfo:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody PatientInfo resources){
        patientInfoService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除患者信息")
    @ApiOperation("删除患者信息")
    @PreAuthorize("@el.check('patientInfo:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        patientInfoService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}