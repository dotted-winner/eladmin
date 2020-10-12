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

import me.zhengjie.modules.hosp.domain.DoctorInfo;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.hosp.repository.DoctorInfoRepository;
import me.zhengjie.modules.hosp.service.DoctorInfoService;
import me.zhengjie.modules.hosp.service.dto.DoctorInfoDto;
import me.zhengjie.modules.hosp.service.dto.DoctorInfoQueryCriteria;
import me.zhengjie.modules.hosp.service.mapstruct.DoctorInfoMapper;
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
 * @author luoqiz
 * @website https://el-admin.vip
 * @description 服务实现
 * @date 2020-10-01
 **/
@Service
@RequiredArgsConstructor
public class DoctorInfoServiceImpl implements DoctorInfoService {

    private final DoctorInfoRepository DoctorInfoRepository;
    private final DoctorInfoMapper DoctorInfoMapper;

    @Override
    public Map<String, Object> queryAll(DoctorInfoQueryCriteria criteria, Pageable pageable) {
        Page<DoctorInfo> page = DoctorInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder), pageable);
        return PageUtil.toPage(page.map(DoctorInfoMapper::toDto));
    }

    @Override
    public List<DoctorInfoDto> queryAll(DoctorInfoQueryCriteria criteria) {
        return DoctorInfoMapper.toDto(DoctorInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, criteria, criteriaBuilder)));
    }

    @Override
    @Transactional
    public DoctorInfoDto findById(Long userId) {
        DoctorInfo DoctorInfo = DoctorInfoRepository.findById(userId).orElseGet(DoctorInfo::new);
        ValidationUtil.isNull(DoctorInfo.getUserId(), "DoctorInfo", "userId", userId);
        return DoctorInfoMapper.toDto(DoctorInfo);
    }

    @Override
    @Transactional
    public DoctorInfoDto findByUserId(Long userId) {
        DoctorInfo DoctorInfo = DoctorInfoRepository.findById(userId).orElseGet(DoctorInfo::new);
        if (DoctorInfo == null) {
            return null;
        }
        return DoctorInfoMapper.toDto(DoctorInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DoctorInfoDto create(DoctorInfo resources) {
        Snowflake snowflake = IdUtil.createSnowflake(1, 1);
        resources.setUserId(snowflake.nextId());
        if (DoctorInfoRepository.findByUsername(resources.getUsername()) != null) {
            throw new EntityExistException(DoctorInfo.class, "username", resources.getUsername());
        }
        return DoctorInfoMapper.toDto(DoctorInfoRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(DoctorInfo resources) {
        DoctorInfo DoctorInfo = DoctorInfoRepository.findById(resources.getUserId()).orElseGet(DoctorInfo::new);
        if (DoctorInfo == null) {
            DoctorInfoRepository.save(DoctorInfo);
        } else {
//            ValidationUtil.isNull( DoctorInfo.getUserId(),"DoctorInfo","id",resources.getUserId());
            DoctorInfo DoctorInfo1 = null;
            DoctorInfo1 = DoctorInfoRepository.findByUsername(resources.getUsername());
            if (DoctorInfo1 != null && !DoctorInfo1.getUserId().equals(DoctorInfo.getUserId())) {
                throw new EntityExistException(DoctorInfo.class, "username", resources.getUsername());
            }
            DoctorInfo.copy(resources);
            DoctorInfoRepository.save(DoctorInfo);
        }

    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long userId : ids) {
            DoctorInfoRepository.deleteById(userId);
        }
    }

    @Override
    public void download(List<DoctorInfoDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (DoctorInfoDto DoctorInfo : all) {
            Map<String, Object> map = new LinkedHashMap<>();
            map.put("用户名", DoctorInfo.getUsername());
            map.put("毕业院校", DoctorInfo.getSchool());
            map.put("从业日期", DoctorInfo.getFromDate());
            map.put("专业擅长", DoctorInfo.getGoodAt());
            map.put("详细介绍", DoctorInfo.getDetail());
            map.put("创建者", DoctorInfo.getCreateBy());
            map.put("更新着", DoctorInfo.getUpdateBy());
            map.put("创建日期", DoctorInfo.getCreateTime());
            map.put("更新时间", DoctorInfo.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public Object queryByRole(DoctorInfoQueryCriteria criteria, Pageable pageable) {
        return null;
    }
}