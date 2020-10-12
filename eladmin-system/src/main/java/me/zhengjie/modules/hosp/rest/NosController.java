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
import me.zhengjie.modules.hosp.domain.Nos;
import me.zhengjie.modules.hosp.service.NosService;
import me.zhengjie.modules.hosp.service.dto.NosQueryCriteria;
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
@Api(tags = "医疗：号源管理管理")
@RequestMapping("/api/nos")
public class NosController {

    private final NosService nosService;

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('nos:list')")
    public void download(HttpServletResponse response, NosQueryCriteria criteria) throws IOException {
        nosService.download(nosService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询号源管理")
    @ApiOperation("查询号源管理")
    @PreAuthorize("@el.check('nos:list')")
    public ResponseEntity<Object> query(NosQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(nosService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增号源管理")
    @ApiOperation("新增号源管理")
    @PreAuthorize("@el.check('nos:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody Nos resources){
        return new ResponseEntity<>(nosService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改号源管理")
    @ApiOperation("修改号源管理")
    @PreAuthorize("@el.check('nos:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody Nos resources){
        nosService.update(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除号源管理")
    @ApiOperation("删除号源管理")
    @PreAuthorize("@el.check('nos:del')")
    @DeleteMapping
    public ResponseEntity<Object> delete(@RequestBody Long[] ids) {
        nosService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}