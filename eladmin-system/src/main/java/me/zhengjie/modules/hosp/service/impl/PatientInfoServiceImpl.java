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

import com.alibaba.fastjson.JSON;
import me.zhengjie.modules.hosp.domain.PatientInfo;
import me.zhengjie.exception.EntityExistException;
import me.zhengjie.modules.hosp.vo.PatientVO;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.service.UserService;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import me.zhengjie.modules.hosp.repository.PatientInfoRepository;
import me.zhengjie.modules.hosp.service.PatientInfoService;
import me.zhengjie.modules.hosp.service.dto.PatientInfoDto;
import me.zhengjie.modules.hosp.service.dto.PatientInfoQueryCriteria;
import me.zhengjie.modules.hosp.service.mapstruct.PatientInfoMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
public class PatientInfoServiceImpl implements PatientInfoService {

    private final PatientInfoRepository patientInfoRepository;
    private final PatientInfoMapper patientInfoMapper;
    private final UserService userService;

    @Override
    public Map<String,Object> queryAll(PatientInfoQueryCriteria criteria, Pageable pageable){
        Page<PatientInfo> page = patientInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(patientInfoMapper::toDto));
    }

    @Override
    public List<PatientInfoDto> queryAll(PatientInfoQueryCriteria criteria){
        return patientInfoMapper.toDto(patientInfoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Transactional
    public PatientInfoDto findById(Long id) {
        PatientInfo patientInfo = patientInfoRepository.findById(id).orElseGet(PatientInfo::new);
        ValidationUtil.isNull(patientInfo.getId(),"PatientInfo","id",id);
        return patientInfoMapper.toDto(patientInfo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PatientInfoDto create(PatientInfo resources) {
        if(patientInfoRepository.findByUserId(resources.getUserId()) != null){
            throw new EntityExistException(PatientInfo.class,"user_id",resources.getUserId()+"");
        }
        if(patientInfoRepository.findByUsername(resources.getUsername()) != null){
            throw new EntityExistException(PatientInfo.class,"username",resources.getUsername());
        }
        return patientInfoMapper.toDto(patientInfoRepository.save(resources));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(PatientInfo resources) {
        PatientInfo patientInfo = patientInfoRepository.findById(resources.getId()).orElseGet(PatientInfo::new);
        ValidationUtil.isNull( patientInfo.getId(),"PatientInfo","id",resources.getId());
        PatientInfo patientInfo1 = null;
        patientInfo1 = patientInfoRepository.findByUserId(resources.getUserId());
        if(patientInfo1 != null && !patientInfo1.getId().equals(patientInfo.getId())){
            throw new EntityExistException(PatientInfo.class,"user_id",resources.getUserId()+"");
        }
        patientInfo1 = patientInfoRepository.findByUsername(resources.getUsername());
        if(patientInfo1 != null && !patientInfo1.getId().equals(patientInfo.getId())){
            throw new EntityExistException(PatientInfo.class,"username",resources.getUsername());
        }
        patientInfo.copy(resources);
        patientInfoRepository.save(patientInfo);
    }

    @Override
    public void deleteAll(Long[] ids) {
        for (Long id : ids) {
            patientInfoRepository.deleteById(id);
        }
    }

    @Override
    public void download(List<PatientInfoDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (PatientInfoDto patientInfo : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("user_id", patientInfo.getUserId());
            map.put("用户名", patientInfo.getUsername());
            map.put("出生日期", patientInfo.getBirthday());
            map.put("过敏史", patientInfo.getAllergy());
            map.put("详细介绍", patientInfo.getDetail());
            map.put("创建者", patientInfo.getCreateBy());
            map.put("更新者", patientInfo.getUpdateBy());
            map.put("创建日期", patientInfo.getCreateTime());
            map.put("更新时间", patientInfo.getUpdateTime());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public PatientInfoDto createInfo(PatientVO patientVO) {
        String userTemp ="{\"dept\":{\"id\":20,\"subCount\":0},\"email\":\"18141916702@qq.com\",\"enabled\":true,\"gender\":\"男\",\"isAdmin\":false,\"jobs\":[{\"id\":14}],\"nickName\":\"患者2\",\"password\":\"$2a$10$moROz3egM/spBXexvp4Qq.dztRduveF4FDQL4YeJCGOegH4BP6z0i\",\"phone\":\"18141916702\",\"roles\":[{\"dataScope\":\"本级\",\"id\":3,\"level\":3}],\"username\":\"患者2\"}";
        User user = JSON.toJavaObject(JSON.parseObject(userTemp),User.class);
        BeanUtils.copyProperties(patientVO,user);
        userService.create(user);
        PatientInfo patientInfo = new PatientInfo();
        BeanUtils.copyProperties(patientVO,patientInfo);
        return patientInfoMapper.toDto(patientInfoRepository.save(patientInfo));
    }
}