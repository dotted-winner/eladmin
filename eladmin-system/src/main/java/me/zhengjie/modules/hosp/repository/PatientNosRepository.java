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
package me.zhengjie.modules.hosp.repository;

import me.zhengjie.modules.hosp.domain.PatientNos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @website https://el-admin.vip
* @author luoqiz
* @date 2020-10-01
**/
public interface PatientNosRepository extends JpaRepository<PatientNos, Long>, JpaSpecificationExecutor<PatientNos> {
    /**
    * 根据 UserId 查询
    * @param user_id /
    * @return /
    */
    PatientNos findByUserId(Long user_id);
    /**
    * 根据 Username 查询
    * @param username /
    * @return /
    */
    PatientNos findByUsername(String username);
}