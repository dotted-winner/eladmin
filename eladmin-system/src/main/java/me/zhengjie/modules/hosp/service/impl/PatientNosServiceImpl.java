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

import cn.hutool.core.util.IdUtil;
import me.zhengjie.modules.hosp.domain.PatientNos;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.modules.hosp.service.NosService;
import me.zhengjie.modules.hosp.service.dto.NosDto;
import me.zhengjie.utils.*;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.hosp.repository.PatientNosRepository;
import me.zhengjie.modules.hosp.service.PatientNosService;
import me.zhengjie.modules.hosp.service.dto.PatientNosDto;
import me.zhengjie.modules.hosp.service.dto.PatientNosQueryCriteria;
import me.zhengjie.modules.hosp.service.mapstruct.PatientNosMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @author luoqiz
 * @website https://el-admin.vip
 * @description 服务实现
 * @date 2020-10-01
 **/
@Service
@RequiredArgsConstructor
public class PatientNosServiceImpl implements PatientNosService {

    private final NosService nosService;
    private final PatientNosRepository patientNosRepository;
    private final PatientNosMapper patientNosMapper;

    private final RedisTemplate redisTemplate;


    @Override
    public Map<String, Object> queryAll(PatientNosQueryCriteria criteria, Pageable pageable) {
        Page<PatientNos> page = patientNosRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(patientNosMapper::toDto));
    }

    @Override
    public List<PatientNosDto> queryAll(PatientNosQueryCriteria criteria) {
        return patientNosMapper.toDto(patientNosRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public PatientNosDto findById(Long id) {
        PatientNos patientNos = patientNosRepository.findById(id).orElseGet(PatientNos::new);
        ValidationUtil.isNull(patientNos.getId(), "PatientNos", "id", id);
        return patientNosMapper.toDto(patientNos);
    }

    @Override
    public synchronized PatientNosDto create(PatientNos resources) {
        PatientNos patientNos = new PatientNos();
        patientNos.setNosId(resources.getNosId());
        Example<? extends PatientNos> ss = Example.of(patientNos);
        // 已经存在数量
        long exsitNum = patientNosRepository.count(ss);
        NosDto nosInfo = nosService.findById(resources.getNosId());
        if (exsitNum >= nosInfo.getNumbers()) {
            throw new RuntimeException("已无号源");
        }
        resources.setNoNo(StringUtils.makeUpFixedLength((exsitNum + 1) + "", 3));
        resources.setId(IdUtil.getSnowflake(1, 2).nextId());
        resources.setCreateTime(new Timestamp(System.currentTimeMillis()));
        resources.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        return patientNosMapper.toDto(patientNosRepository.save(resources));
    }

    @Override
    public PatientNosDto create1(PatientNos resources) {
        String nos = (String) redisTemplate.opsForList().rightPop(resources.getNosId());
        if (nos == null) {
            throw new RuntimeException("已无号源");
        }
        resources.setNoNo(StringUtils.makeUpFixedLength(nos, 3));
        resources.setId(IdUtil.getSnowflake(1, 2).nextId());
        resources.setCreateTime(new Timestamp(System.currentTimeMillis()));
        resources.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        return patientNosMapper.toDto(patientNosRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PatientNos resources) {
        PatientNos patientNos = patientNosRepository.findById(resources.getId()).orElseGet(PatientNos::new);
        ValidationUtil.isNull(patientNos.getId(), "PatientNos", "id", resources.getId());
        PatientNos patientNos1 = null;
        patientNos1 = patientNosRepository.findByUserId(resources.getUserId());
        if (patientNos1 != null && !patientNos1.getId().equals(patientNos.getId())) {
            throw new EntityExistException(PatientNos.class, "user_id", resources.getUserId() + "");
        }
        patientNos1 = patientNosRepository.findByUsername(resources.getUsername());
        if (patientNos1 != null && !patientNos1.getId().equals(patientNos.getId())) {
            throw new EntityExistException(PatientNos.class, "username", resources.getUsername());
        }
        patientNos.copy(resources);
        patientNosRepository.save(patientNos);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            patientNosRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<PatientNosDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PatientNosDto patientNos : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("ID", patientNos.getUserId());
            map.put("用户名", patientNos.getUsername());
            map.put("挂号日期", patientNos.getRegistration());
            map.put("挂号状态", patientNos.getStatus());
            map.put("号源id", patientNos.getNosId());
            map.put("号源编号", patientNos.getNoNo());
            map.put("创建者", patientNos.getCreateBy());
            map.put("更新者", patientNos.getUpdateBy());
            map.put("创建日期", patientNos.getCreateTime());
            map.put("更新时间", patientNos.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}