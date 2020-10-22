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
import me.zhengjie.modules.hosp.domain.PatientNos;
import me.zhengjie.modules.hosp.service.PatientNosService;
import me.zhengjie.modules.hosp.service.dto.PatientNosQueryCriteria;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletResponse;

/**
* @website https://el-admin.vip
* @author luoqiz
* @date 2020-10-01
**/
@RestController
@RequiredArgsConstructor
@Api(tags = "医疗：挂号管理管理")
@RequestMapping("/api/patientNos")
public class PatientNosController {

    private final PatientNosService patientNosService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('patientNos:list')")
    public void download(HttpServletResponse response, PatientNosQueryCriteria criteria) throws IOException {
        patientNosService.download(patientNosService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询挂号管理")
    @ApiOperation("查询挂号管理")
    @PreAuthorize("@el.check('patientNos:list')")
    public ResponseEntity<Object> query(PatientNosQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(patientNosService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增挂号管理")
    @ApiOperation("新增挂号管理")
    @PreAuthorize("@el.check('patientNos:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody PatientNos resources){
        return new ResponseEntity<>(patientNosService.create(resources),HttpStatus.CREATED);
    }

    @PostMapping("/tradition")
    @Log("新增挂号管理")
    @ApiOperation("新增挂号管理无权限验证")
    @AnonymousAccess
    public ResponseEntity<Object> create1(@Validated @RequestBody PatientNos resources){
        return new ResponseEntity<>(patientNosService.create(resources),HttpStatus.CREATED);
    }

    @PostMapping("/parallel")
    @Log("新增挂号管理")
    @ApiOperation("新增挂号管理无权限验证")
    @AnonymousAccess
    public ResponseEntity<Object> create2(@Validated @RequestBody PatientNos resources){
        return new ResponseEntity<>(patientNosService.create1(resources),HttpStatus.CREATED);
    }


    @PutMapping
    @Log("修改挂号管理")
    @ApiOperation("修改挂号管理")
    @PreAuthorize("@el.check('patientNos:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody PatientNos resources){
        patientNosService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除挂号管理")
    @ApiOperation("删除挂号管理")
    @PreAuthorize("@el.check('patientNos:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        patientNosService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}