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

import me.zhengjie.annotation.Log;
import me.zhengjie.modules.hosp.domain.DoctorInfo;
import me.zhengjie.modules.hosp.service.DoctorInfoService;
import me.zhengjie.modules.hosp.service.dto.DoctorInfoQueryCriteria;
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
@Api(tags = "医疗：医师信息管理")
@RequestMapping("/api/DoctorInfo")
public class DoctorInfoController {

    private final DoctorInfoService DoctorInfoService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('DoctorInfo:list')")
    public void download(HttpServletResponse response, DoctorInfoQueryCriteria criteria) throws IOException {
        DoctorInfoService.download(DoctorInfoService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询医师信息")
    @ApiOperation("查询医师信息")
    @PreAuthorize("@el.check('DoctorInfo:list')")
    public ResponseEntity<Object> query(DoctorInfoQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(DoctorInfoService.queryAll(criteria,pageable),HttpStatus.OK);
    }


    @GetMapping("/{userId}")
    @Log("查看医师信息")
    @ApiOperation("查看医师信息")
    @PreAuthorize("@el.check('DoctorInfo:list')")
    public ResponseEntity<Object> queryByUserId(@PathVariable Long userId){
        return new ResponseEntity<>(DoctorInfoService.findByUserId(userId),HttpStatus.OK);
    }

    @GetMapping("/listByRole")
    @Log("查询医师信息")
    @ApiOperation("查询医师信息")
    @PreAuthorize("@el.check('DoctorInfo:list')")
    public ResponseEntity<Object> queryByRole(DoctorInfoQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(DoctorInfoService.queryByRole(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增医师信息")
    @ApiOperation("新增医师信息")
    @PreAuthorize("@el.check('DoctorInfo:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody DoctorInfo resources){
        return new ResponseEntity<>(DoctorInfoService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改医师信息")
    @ApiOperation("修改医师信息")
    @PreAuthorize("@el.check('DoctorInfo:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody DoctorInfo resources){
        DoctorInfoService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除医师信息")
    @ApiOperation("删除医师信息")
    @PreAuthorize("@el.check('DoctorInfo:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        DoctorInfoService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}