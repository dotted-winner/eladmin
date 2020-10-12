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
package me.zhengjie.modules.hosp.service.impl;

import me.zhengjie.modules.hosp.domain.Nos;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.hosp.repository.NosRepository;
import me.zhengjie.modules.hosp.service.NosService;
import me.zhengjie.modules.hosp.service.dto.NosDto;
import me.zhengjie.modules.hosp.service.dto.NosQueryCriteria;
import me.zhengjie.modules.hosp.service.mapstruct.NosMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @website https://el-admin.vip
* @description 服务实现
* @author luoqiz
* @date 2020-10-01
**/
@Service
@RequiredArgsConstructor
public class NosServiceImpl implements NosService {

    private final NosRepository nosRepository;
    private final NosMapper nosMapper;

    @Override
    public Map<String,Object> queryAll(NosQueryCriteria criteria, Pageable pageable){
        Page<Nos> page = nosRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(nosMapper::toDto));
    }

    @Override
    public List<NosDto> queryAll(NosQueryCriteria criteria){
        return nosMapper.toDto(nosRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public NosDto findById(Long id) {
        Nos nos = nosRepository.findById(id).orElseGet(Nos::new);
        ValidationUtil.isNull(nos.getId(),"Nos","id",id);
        return nosMapper.toDto(nos);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public NosDto create(Nos resources) {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        resources.setId(snowflake.nextId()); 
        if(nosRepository.findByUserId(resources.getUserId()) != null){
            throw new EntityExistException(Nos.class,"user_id",resources.getUserId()+"");
        }
        if(nosRepository.findByUsername(resources.getUsername()) != null){
            throw new EntityExistException(Nos.class,"username",resources.getUsername());
        }
        return nosMapper.toDto(nosRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Nos resources) {
        Nos nos = nosRepository.findById(resources.getId()).orElseGet(Nos::new);
        ValidationUtil.isNull( nos.getId(),"Nos","id",resources.getId());
        Nos nos1 = null;
        nos1 = nosRepository.findByUserId(resources.getUserId());
        if(nos1 != null && !nos1.getId().equals(nos.getId())){
            throw new EntityExistException(Nos.class,"user_id",resources.getUserId()+"");
        }
        nos1 = nosRepository.findByUsername(resources.getUsername());
        if(nos1 != null && !nos1.getId().equals(nos.getId())){
            throw new EntityExistException(Nos.class,"username",resources.getUsername());
        }
        nos.copy(resources);
        nosRepository.save(nos);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            nosRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<NosDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (NosDto nos : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("user_id", nos.getUserId());
            map.put("用户名", nos.getUsername());
            map.put("发布时间", nos.getReleaseDate());
            map.put("发布数量", nos.getNumbers());
            map.put("就诊日期", nos.getClinicDate());
            map.put("时间节点", nos.getTimeNodes());
            map.put("取号时间", nos.getTakeDate());
            map.put("发布状态", nos.getStatus());
            map.put("创建者", nos.getCreateBy());
            map.put("更新者", nos.getUpdateBy());
            map.put("创建日期", nos.getCreateTime());
            map.put("更新时间", nos.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}